package com.walmart.ticket.repository;

import java.io.IOException;
import java.util.*;

import com.walmart.ticket.TicketBooking;
import common.entity.Seat;
import common.entity.SeatHold;

import jdbm.RecordManager;
import jdbm.RecordManagerFactory;
import jdbm.PrimaryTreeMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by dliu15 on 3/10/2018.
 *
 * Default implementation for {@link TicketRepository}
 */
public class TicketRepositoryImpl implements com.walmart.ticket.repository.TicketRepository {

    private static final Logger LOGGER = LogManager.getLogger(TicketRepositoryImpl.class);

    private static final int secondsToExpire = 30;

    private int rows;
    private int seatsInRow;

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getSeatsInRow() {
        return seatsInRow;
    }

    public void setSeatsInRow(int seatsInRow) {
        this.seatsInRow = seatsInRow;
    }

    private PrimaryTreeMap<Double, Seat> seatAvailable;

    private PrimaryTreeMap<Integer, SeatHold> HoldSeats;

    private PrimaryTreeMap<String, SeatHold> ReserveSeats;

    private RecordManager recMan;

    public TicketRepositoryImpl(int row, int seatsInRow) {

        setRows(row);
        setSeatsInRow(seatsInRow);

        try {
            String fileName = "TicketBooking";

            recMan = RecordManagerFactory.createRecordManager(fileName);

            String seatRecordName = "seatAvailable";
            seatAvailable = recMan.treeMap(seatRecordName);

            String holdRecordName = "HoldSeats";
            HoldSeats = recMan.treeMap(holdRecordName);

            String reserverRecordName = "ReserveSeats";
            ReserveSeats = recMan.treeMap(reserverRecordName);

            //Initialize the available seats;
            for (int i=0; i<rows;i++) {
                for (int j=0; j<seatsInRow;j++) {
                    seatAvailable.put(getDistance(i,j), new Seat(i,j));
                }
            }

            /** Map changes are not persisted yet, commit them (save to disk) */
            try {
                recMan.commit();
            } catch (IOException e) {

            }
        }catch (IOException e) {
            LOGGER.debug("Exception is " + e.getMessage());
        }

    }

    //Close the recMan
    protected void finalize ()  {
        try {
            recMan.close();
        } catch (IOException e) {
            LOGGER.debug("Exception is " + e.getMessage());
        }
    }

    /**
     * Find the distance from (rowNum, colNum) to the center of the Venue.
     *
     * @param rowNum the row number of seat
     * @param colNum the col number of seat
     * @return the distance to the center of the Venue;
     */
    /*
      Get the distance for (rowNum,colNum) to the center of the all seats.
      The assumption is that the closer the distance to the center of the Venue, the better the seat.
      When the distance is the same, use the Math.random() to generate
      similar distance value to guarantee the unique Key value.
     */
    public Double getDistance(int rowNum, int colNum) {

        return Double.valueOf(
                Math.pow(rowNum - (rows/2),2)
                + Math.pow(colNum - seatsInRow/2,2)) + Math.random();
    }

    /**
     * Find a certain number of best seats
     *
     * @param seatNum the number of seats to find
     * @return a list of found seats
     */
    @Override
    public List<Seat> findBestSeat(int seatNum) {

        List<Seat> holdSeats = new ArrayList<Seat>();

        /*
         Here how to find the best seats is implemented by using acsending order Treemap (object: seatAvailable). As treeMap is
         ordered map by key. The lower value of the key, the better the seat. Please refer the getDistance function in this class.
         After understanind this, the first 'seatNum' elements in seatAvailable will be the best 'seatNum' seat.
        */

        Set set = seatAvailable.entrySet();
        Iterator i = set.iterator();
        int count =  0;

        // Display elements
        while(i.hasNext()) {
            Map.Entry eachSeat = (Map.Entry)i.next();

            holdSeats.add((Seat)eachSeat.getValue());
            count++;
            if (count == seatNum) {
                break;
            }
        }

        //Remove seatNum seat from availabe treeMap.
        count=0;
        for(Map.Entry<Double,Seat> entry : seatAvailable.entrySet()) {
            Double key = entry.getKey();
            seatAvailable.remove(key);
            count++;
            if (count ==seatNum) {
                break;
            }
        }

        /** Map changes are not persisted yet, commit them (save to disk) */
        try {
            recMan.commit();
        } catch (IOException e) {

        }


        return holdSeats;
    }

    /**
     * Add the (seatHoldId, SeatHold) pair to Hold map
     *
     * @param seatHoldId the seathold id
     * @param seatHold the seathold object

     * @return a list of found seats
     */
    @Override
    public void addHoldMap(int seatHoldId, SeatHold seatHold) {

        HoldSeats.put(seatHoldId, seatHold);

        /** Map changes are not persisted yet, commit them (save to disk) */
        try {
            recMan.commit();
        } catch (IOException e) {

        }
    }

    /**
     * Check whether the holds expire or not.
     * If yes, move the expired hold to available seats.
     *
     */
    @Override
    public void checkHoldExpire() {

        if (HoldSeats!=null) {

            for (Map.Entry<Integer, SeatHold> entry : HoldSeats.entrySet()) {
                Integer seatHoldId = entry.getKey();
                SeatHold seatHold = entry.getValue();

                if (seatHold.getHoldTime() < System.currentTimeMillis() - secondsToExpire * 1000) {

                    List<Seat> seats = seatHold.getSeatBookings();

                    for (int i = 0; i < seats.size(); i++) {
                        Seat eachSeat = seats.get(i);
                        seatAvailable.put(getDistance(eachSeat.getRowNum(), eachSeat.getRowNum()),
                                new Seat(eachSeat.getRowNum(), eachSeat.getRowNum()));
                    }

                    HoldSeats.remove(seatHoldId, seatHold);
                }
            }

            /** Map changes are not persisted yet, commit them (save to disk) */
            try {
                recMan.commit();
            } catch (IOException e) {

            }
        }
    }

    /**
     * Check whether the holds expire or not.
     * If yes, move the expired hold to available seats.
     *
     * @return the number of available seats
     */
    @Override
    public int findAvailableSeatNum() {
        //Check the expired hold and move back the available seats;
        checkHoldExpire();
        return seatAvailable.size();
    }

    /**
     * search seatHold in the hold map with seatHoldID
     *
     * @param seatHoldId the seathold id
     * @param customerEmail the email that is used to hold seats
     * @return the seatHold object
     */
    @Override
    public SeatHold searchHoldMap(int seatHoldId, String customerEmail) {

        if (HoldSeats.containsKey(seatHoldId)) {

            SeatHold seatHold = HoldSeats.get(seatHoldId);
            //Check whether the seatHoldId matches customerEmail.
            //If yes, return the corresponding seatHold
            //If no, return null
            if (seatHold.getCustomerEmail().compareTo(customerEmail)==0) {
                return seatHold;
            } else {
                return null;
            }

        } else {
            return null;
        }
    }

    /**
     * Move the seatHold from Hold Map to reserve Map
     *
     * @param seatHold the seathold
     */
    @Override
    public void moveFromHoldToReserveMap(SeatHold seatHold) {

        int seatHoldId = seatHold.getId();

        if (HoldSeats.containsKey(seatHoldId)) {
            HoldSeats.remove(seatHoldId);
        }

        ReserveSeats.put(seatHold.getBookingCode(), seatHold);

        /** Map changes are not persisted yet, commit them (save to disk) */
        try {
            recMan.commit();
        } catch (IOException e) {

        }

    }

    /**
     * Get the seconds threshold to expire for hold
     *
     * @return the seconds to expire hold
     */
    public int getExpireSeconds() {
        return secondsToExpire;
    }

}

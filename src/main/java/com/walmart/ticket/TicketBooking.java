package com.walmart.ticket;

import com.walmart.ticket.exception.ReservationNotValidException;
import com.walmart.ticket.exception.SeatHoldNotFoundException;
import com.walmart.ticket.repository.TicketRepository;
import com.walmart.ticket.repository.TicketRepositoryImpl;
import common.entity.Seat;
import common.entity.SeatHold;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by dliu15 on 3/10/18.
 */
public class TicketBooking implements TicketService {

    private static final int rowOffset = 1000;

    private static final Logger LOGGER = LogManager.getLogger(TicketBooking.class);

    private static TicketRepositoryImpl ticketRepo;

    public TicketBooking(TicketRepositoryImpl ticketRepo) {
        this.ticketRepo= ticketRepo;
    }

    /**
     * The number of seats in the venue that are neither held nor reserved
     *
     * @return the number of tickets available in the venue
     */
    @Override
    public int numSeatsAvailable() {
        ticketRepo.checkHoldExpire();

        return ticketRepo.findAvailableSeatNum();
    }
    /**
     * Find and hold the best available seats for a customer
     *
     * @param numSeats the number of seats to find and hold
     * @param customerEmail unique identifier for the customer
     * @return a SeatHold object identifying the specific seats and related
    information
     */
    @Override
    public SeatHold findAndHoldSeats(int numSeats, String customerEmail) {

        int availableTicket = numSeatsAvailable();
        if (availableTicket < numSeats) {
            return null;
        }

        List<Seat> bestSeats =  new ArrayList<Seat>();
        bestSeats = ticketRepo.findBestSeat(numSeats);

        if ((bestSeats==null) || bestSeats.isEmpty())
             return null;

        //Use first seat to generate the seatHold id;
        Seat oneSeat = bestSeats.get(0);

        SeatHold seatHold = new SeatHold();
        seatHold.setHoldTime(System.currentTimeMillis());
        seatHold.setSeatBookings(bestSeats);
        seatHold.setId(oneSeat.getRowNum()*rowOffset + oneSeat.getColNum());
        seatHold.setCustomerEmail(customerEmail);

        //Add the hold to the holdMap
        ticketRepo.addHoldMap(seatHold.getId(), seatHold);

        return seatHold;

    }
    /**
     * Commit seats held for a specific customer
     *
     * @param seatHoldId the seat hold identifier
     * @param customerEmail the email address of the customer to which the
    seat hold is assigned
     * @return a reservation confirmation code
     */
    @Override
    public String reserveSeats(int seatHoldId, String customerEmail) {

       SeatHold seatHold = ticketRepo.searchHoldMap(seatHoldId,customerEmail);

        if (seatHold!=null) {

            if (!customerEmail.equalsIgnoreCase(seatHold.getCustomerEmail())) {
                throw new ReservationNotValidException("Seathold not valid as input email doesn't match the hold email");
            }

            if (seatHold.getHoldTime() < System.currentTimeMillis() - ticketRepo.getExpireSeconds() * 1000) {
                    throw new ReservationNotValidException("SeatHold with" + seatHold + " is expired");
            }

            String bookingCode = UUID.randomUUID().toString();
            seatHold.setBookingTime(System.currentTimeMillis());
            seatHold.setBookingCode(bookingCode.toString());

            ticketRepo.moveFromHoldToReserveMap(seatHold);

            return bookingCode;
        } else {
            throw new SeatHoldNotFoundException("Seathold with" + seatHoldId + " not found");
        }

    }


}

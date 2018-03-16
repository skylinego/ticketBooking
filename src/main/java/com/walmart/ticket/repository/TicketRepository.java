package com.walmart.ticket.repository;


import common.entity.Seat;
import common.entity.SeatHold;

import java.util.List;

/**
 * Created by dliu15 on 03/10/2018.
 *
 * Repository interface for booking related functions
 */
public interface TicketRepository {

    /**
     * Find a certain of best seats
     *
     * @param seatNum the number of seats to find
     * @return a list of found seats
     */
    public List<Seat> findBestSeat(int seatNum);

    /**
     * Add the (seatHoldId, SeatHold) pair to Hold map
     *
     * @param seatHoldId the seathold id
     * @param seatHold the seathold object

     * @return a list of found seats
     */
    public void addHoldMap(int seatHoldId, SeatHold seatHold);

    /**
     * Check whether the holds expire or not.
     * If yes, move the expired hold to available seats.
     *
     */

    public void checkHoldExpire();

    /**
     * Check whether the holds expire or not.
     * If yes, move the expired hold to available seats.
     *
     * @return the number of available seats
     */
    public int findAvailableSeatNum();

    /**
     * search seatHold in the hold map with seatHoldID
     *
     * @param seatHoldId the seathold id
     * @param customerEmail the email that is used to hold seats
     * @return the seatHold object
     */
    public SeatHold searchHoldMap(int seatHoldId, String customerEmail);

    /**
     * Move the seatHold from Hold Map to reserve Map
     *
     * @param seatHold the seathold
     */
    public void moveFromHoldToReserveMap(SeatHold seatHold);

    }

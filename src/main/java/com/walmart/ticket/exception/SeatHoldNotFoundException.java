package com.walmart.ticket.exception;

/**
 * Created by dliu15 on 3/14/18.
 * {@link RuntimeException} that is thrown when a seat hold is not found
 */
public class SeatHoldNotFoundException extends RuntimeException{

    public SeatHoldNotFoundException(String message) {
        super(message);
    }
}


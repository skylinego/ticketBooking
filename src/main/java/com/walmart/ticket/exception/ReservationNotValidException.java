package com.walmart.ticket.exception;

/**
 * Created by dliu15 on 3/14/18.
 * {@link RuntimeException} that is thrown when a seat hold is not valid
 */
public class ReservationNotValidException extends RuntimeException {

    public ReservationNotValidException(String message) {
        super(message);
    }
}

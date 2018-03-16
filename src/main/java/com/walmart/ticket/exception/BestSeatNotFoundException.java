package com.walmart.ticket.exception;

/**
 * Created by dliu15 on 3/14/18.
 * {@link RuntimeException} that is thrown when enough seats are not found
 */
public class BestSeatNotFoundException extends RuntimeException{

    public BestSeatNotFoundException(String message) {
        super(message);
    }
}


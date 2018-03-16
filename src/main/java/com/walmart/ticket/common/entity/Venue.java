package com.walmart.ticket.common.entity;

/**
 * Created by dliu on  3/10/18..
 *
 * Entity that holds data related to a Venue
 */
public class Venue {

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

    @Override
    public String toString() {
        return "Venue{" +
                ", rows=" + rows +
                ", seatsInRow=" + seatsInRow +
                '}';
    }
}
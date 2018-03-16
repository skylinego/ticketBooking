package common.entity;


import common.entity.Seat;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by dliu on 3/10/18.
 *
 * Entity that identifies the specific seats and related information
 */
public class SeatHold implements Serializable {

    private int id;
    private long holdTime;
    private String customerEmail;
    private String bookingCode;
    private long bookingTime;
    private List<Seat> seatBookings;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getHoldTime() {
        return holdTime;
    }

    public void setHoldTime(long holdTime) {
        this.holdTime = holdTime;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getBookingCode() {
        return bookingCode;
    }

    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
    }

    public long getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(long bookingTime) {
        this.bookingTime = bookingTime;
    }

    public List<Seat> getSeatBookings() {
        return seatBookings;
    }

    public void setSeatBookings(List<Seat> seatBookings) {
        this.seatBookings = seatBookings;
    }

    @Override
    public String toString() {
        return "SeatHold{" +
                "id=" + id +
                ", holdTime=" + holdTime +
                ", customerEmail='" + customerEmail + '\'' +
                ", bookingTime=" + bookingTime +
                ", seatBookings=" + seatBookings +
                '}';
    }
}

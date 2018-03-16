package com.walmart.ticket;

import com.walmart.ticket.repository.TicketRepositoryImpl;
import com.walmart.ticket.TicketBooking;
import org.junit.Test;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.AfterClass;

import java.io.File;
import java.io.IOException;
import java.util.*;
import static org.junit.Assert.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.walmart.ticket.exception.SeatHoldNotFoundException;
import com.walmart.ticket.exception.ReservationNotValidException;
import com.walmart.ticket.exception.BestSeatNotFoundException;


public class TicketBookingTest {

  private static TicketRepositoryImpl ticketRepository;
  private static TicketBooking ticketSystem;
  private static final Logger LOGGER = LogManager.getLogger(TicketBookingTest.class);
  private static final int rowNum = 20;
  private static final int seatsInRow =20;

  @BeforeClass
  public static void prepareSystem() {

    ticketRepository = new TicketRepositoryImpl(rowNum,seatsInRow);
    ticketSystem = new TicketBooking(ticketRepository);

    LOGGER.info("available seat is " + ticketSystem.numSeatsAvailable());
  }

  @AfterClass
  public static void cleanSystem() {

    File dir = new File(".");
    for (File file:dir.listFiles()) {
      if (file.getName().startsWith("TicketBooking.")) {
        file.delete();
      }
    }

    LOGGER.info("database is deleted ");
  }

  @Test
  public void testSeatAvailable() throws Exception {

    String funcName = new Object(){}.getClass().getEnclosingMethod().getName();

    printTestHeader(funcName);

    LOGGER.info("available seat is " + ticketSystem.numSeatsAvailable());
    System.out.println("available seat is " + ticketSystem.numSeatsAvailable());
    assert(ticketSystem.numSeatsAvailable() < seatsInRow*rowNum);

  }

  @Test
  public void testreserveSeats() throws Exception {

    String funcName = new Object(){}.getClass().getEnclosingMethod().getName();

    printTestHeader(funcName);

    common.entity.SeatHold seatHold = ticketSystem.findAndHoldSeats(2, "customer@test.com");

    System.out.println("seathold is" + seatHold.toString());

    String code = ticketSystem.reserveSeats(seatHold.getId(), "customer@test.com");

    LOGGER.info("code is " + code);
    LOGGER.info("After available seat is " + ticketSystem.numSeatsAvailable());
    System.out.println("code is " + code);
    System.out.println("Final available seat is " + ticketSystem.numSeatsAvailable());

  }

  @Test(expected = BestSeatNotFoundException.class)
  public void testfindAndHoldSeatsLimit() throws Exception {

    String funcName = new Object(){}.getClass().getEnclosingMethod().getName();

    printTestHeader(funcName);

    common.entity.SeatHold seatHold = ticketSystem.findAndHoldSeats(2*rowNum*seatsInRow, "customer@test.com");

  }


  @Test
  public void testfindAndHoldSeats() throws Exception {

    String funcName = new Object(){}.getClass().getEnclosingMethod().getName();

    printTestHeader(funcName);

    common.entity.SeatHold seatHold = ticketSystem.findAndHoldSeats(2, "customer@test.com");

    System.out.println("seathold is" + seatHold.toString());
    //waitForExpirationTimeToComplete();
    System.out.println("Final available seat is " + ticketSystem.numSeatsAvailable());

  }

  @Test(expected = SeatHoldNotFoundException.class)
  public void testReserveSeatsWithNonExistingSeatHold() {

    String funcName = new Object(){}.getClass().getEnclosingMethod().getName();

    printTestHeader(funcName);

    common.entity.SeatHold seatHold = ticketSystem.findAndHoldSeats(2, "customer@test.com");
    ticketSystem.reserveSeats((int)(Math.random()*100), "customer@test.com");
  }

  @Test(expected = ReservationNotValidException.class)
  public void testReserveSeatsAfterExpiration() throws Exception {

    String funcName = new Object(){}.getClass().getEnclosingMethod().getName();

    printTestHeader(funcName);

    common.entity.SeatHold seatHold = ticketSystem.findAndHoldSeats(2, "customer@test.com");

    waitForExpirationTimeToComplete();

    ticketSystem.reserveSeats(seatHold.getId(), "customer@test.com");
  }

  @Test(expected = SeatHoldNotFoundException.class)
  public void testReserveSeatsWithAnotherEmail() throws Exception {

    String funcName = new Object(){}.getClass().getEnclosingMethod().getName();

    printTestHeader(funcName);

    common.entity.SeatHold seatHold = ticketSystem.findAndHoldSeats(2, "customer@test.com");

    ticketSystem.reserveSeats(seatHold.getId(), "anotheremail@test.com");
  }


  private void waitForExpirationTimeToComplete() throws InterruptedException {

    LOGGER.debug("Waiting for expiration to complete...");
    int secondsToExpire = ticketRepository.getExpireSeconds();
    Thread.sleep((secondsToExpire + 1) * 1000);
  }

  private void printTestHeader(String funcName) {
    System.out.println("****************************************");
    System.out.println(funcName);
    System.out.println("****************************************");
  }

}
# Ticket_booking Project
Implementation of a simple ticket booking service that facilitates the search, temporary hold, and final reservation of seats in a venue.

This project is developed by using Java, jdbm2, Maven.

### Assumptions
---
1. Users are provided seats based on the availability.
2. A simple method to find the best seats is: the closer the distance from seat to the center of venue seats, the better the seat. The method is based on the observation is: the best seat is in the center of the venue. To close or to far to the stage is not good. If there is another better alogrithm, the following function can be replaced: TicketRepositoryImpl::getDistance(rowNum, colNum). 
3. Hold time for the seats is 30 seconds. If the user doesn't reserve the seats before 30 seconds, the holds are removed and user has to send a request again to hold the seats.
4. Jdbm2 (https://code.google.com/archive/p/jdbm2/) is used to store the ticket booking information such as available seats, seathold and reservation on disk.
5. REST API can be provided by using PlayFramework or other framework. (Not implemented now).

### Building Project
---
1. Clone the project
 ```
 git clone https://github.com/skylinego/ticketBooking
 ```
2. Build the project
```
cd ticketBooking
```
```
mvn clean package
```

### Testing Results
---
Tests are done using JUnit. Tests are run using the command 
```
mvn test
```

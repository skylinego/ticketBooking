ticket_service System
Implementation of a simple ticket booking service that facilitates the search, temporary hold, and
final reservation of seats in a venue.

This application is developed by using Java, jdbm2, Maven.

Assumptions
1 Users are provided seats based on the availability.
2 The closer the distance from seat to the center of venue, the better the seat.
3 Hold time for the seats is 30 seconds. If the user doesn't reserve the seats before 30 seconds, then the holds are removed
and user has to send a request again to hold the seats.
4 Jdbm2 (https://code.google.com/archive/p/jdbm2/) is used to store the ticket booking information on disk.
5 REST API can be provided by using PlayFramework. (Not implemented this time).

Clone the project

git clone https://github.com/skyline2go/ticket_service.git

Build the project
cd ticket_service
mvn clean install

Testing Results
Tests are done using JUnit. Tests are run using the command mvn test


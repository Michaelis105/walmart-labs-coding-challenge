## Walmart Labs Ticket Service
Implementation of a simple ticket service that facilitates the discovery, temporary hold, and final reservation of seats
within a high-demand performance venue.

### Assumptions
1. The venue for which this ticket service facilitates has a seating arrangement reflecting close to that of the spec. Specifically, the seating arrangement is represented as N by M seats only. Other general venue keywords not specified in the spec will not be considered as part of seating arrangement.
2. No criteria for the "best" seat is defined in the spec for holding or reserving seats. The best will be defined
by filling in the front-left-most seat for each row from left to right. The last possible seat or the "least best" to be 
filled is the bottom-right-most one. Indirectly, all seats in a single hold will be in the same row except when there are 
not enough seats to satisfy request. If so, then the remaining seats in the next row (if any remaining) will be held.
This is not a viewing experience study but let's assume the best seats are "up close and personal" with the stage.
3. TicketService interface does not have proper signatures to handle exceptions. All erroring operations in regards to TicketService will either return null, an "error code", or do nothing (left comments for what the error is and what should be done to catch error).
4. TicketService should be used as an attachable service to a larger product. In other words, this project is not intended to be working standalone. Some other product must provide the TicketService a venue (and seats) to facilitate ticket operations.
4. A customer hold and reserve any number of seats any number of times.
5. A seat hold's expiration is determined by the duration of the current time and seat hold creation time exceeding the expiration time threshold (in seconds). Threshold is configurable in source code. 
6. The cleanup of expired seat holds would be run by some batch/cron job if this implementation was migrated to a web service.
7. Since the seat hold is assigned an ID, the caller should use this instead of the seat hold. The seat hold is for TicketService internal purposes only and should not be modified by anything outside it.
8. A venue will always have seats.
9. Internal strucutre is seatHold is same as ideal one for a reservation confirmation. That is, both of unique IDs that need recording into TicketService for future reference and each hold seats to which the customer is entitled to via purchase. Both a seat hold and confirmation will share the same object type.
10. Billing components are excluded for purpose of exercise.

### Instructions

#### Installing

Assuming Java and Git are setup on device:
```
git clone https://github.com/Michaelis105/walmart-labs-coding-challenge.git
cd walmart-labs-coding-challenge
```

#### Building

`./gradlew assemble`

#### Testing

`./gradlew check`

### Design Overview
Project not intended for standalone run. Ideally, some other service or web application would create instance of TicketService
for which to pass a venue instance to it. I apologize for the lack of diagrams as I ran out of time to properly document everything.

[Walmart Class Diagram]: (https://github.com/Michaelis105/walmart-labs-coding-challenge/blob/master/walmartClassDiagram.png "Walmart Class Diagram")

### Further actions
1. Migrate project to web service.
2. Externalize seatHold expiration.
3. Change TicketService interface to allow for proper exception catching.
4. Create some UML diagrams or something to illustrate system architecture and workflow.
5. Change TicketService to make recognizing seat arrangement more generic.

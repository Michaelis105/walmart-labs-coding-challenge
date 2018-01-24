## Walmart Labs Ticket Service
Implementation of a simple ticket service that facilitates the discovery, temporary hold, and final reservation of seats
within a high-demand performance venue.

### Assumptions
1. The venue for which this ticket service facilitates has a seating arrangement reflecting close to that of the problem 
state. Specifically, the seating arrangement is represented as N by M seats only. Concept of levels, sections, or any
other general venue keywords outside the purpose of will not be considered as part of seating arrangement.
2. No criteria for the "best" seat is defined in the spec for holding or reserving seats. The best will be defined
by filling in the front-left-most seat for each row from left to right. The last possible seat or the "least best" to be 
filled is the bottom-right-most one. Indirectly, all seats in a single hold will be in the same row except when there are 
not enough seats to satisfy request. If so, then the remaining seats in the next row (if any remaining) will be held.
This is not a viewing experience study but let's assume the best seats are "up close and personal" with the stage.
3. TODO: List more


### Instructions

#### Installing

Assuming Java and Git are setup on device:
```
1. git clone https://github.com/Michaelis105/walmart-labs-coding-challenge.git
2. cd walmart-labs-coding-challenge
```

#### Building

`./gradlew assemble`

#### Testing

`./gradlew check`

### Design Overview
Project not intended for standalone run. Ideally, some other
service or web application would create instance of TicketService
for which to pass a venue instance to it.

### Further actions
1. Migrate project to web service.
2. TODO: List more
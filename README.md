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

### Instructions

#### Building 

1. Ensure your JAVA_HOME to JDK 1.8.X path.
2. 

#### Testing

### Design Overview

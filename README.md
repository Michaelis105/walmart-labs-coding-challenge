## Walmart Labs Ticket Service Coding Challenge - 01/21/2018
Implementation of a simple ticket service that facilitates the discovery, temporary hold, and final reservation of seats within a high-demand performance
venue.

Specification attached as "Ticket Service Coding Challenge.pdf"

**I have not signed any non-disclosure agreement of any sort with Walmart or Walmart Labs and was not requested/required to do so
from the time the screening process started to the time of submission review.
Since only submissions via GitHub account are accepted, this repo and submission will suffice.**

## REVIEWERS read here

### Assumptions
1. The venue for which this ticket service facilitates has a seating arrangement reflecting close to that of the problem state.
Specifically, the seating arrangement is represented by N by M seats only. Concept of levels, sections, or any other general
venue keywords outside the purpose of will not be considered as part of seating arrangement.

2. No criteria for the "best" seat is defined in the spec for holding or reserving seats. For now the best will be defined
by filling in the front-left-most seat row by row. The last possible seat or the "least best" to be filled is the
bottom-right-most one. Indirectly, all seats in a single hold will be in the same row except when there are not enough seats
to satisfy request. If so, then the remaining seats in the next row (if any remaining) will be held.

### Instructions


### Design Overview

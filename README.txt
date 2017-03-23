# Assumptions

Prices:
-------
* Assumed prices are in whole pounds sterling and no other currencies supported yet and given that current operations
 consist of basic addition I decided that in this case using BigDecimal for monetary values would be overkill and opted
 for longs to keep things simple.

Currency:
---------
* Assumed only pounds sterling currently supported and that the UI would add the currency symbol.

Quantity:
* Assumed values are always relative to kilograms.
* Assumed UI would add the kg symbol.


# Choices:

@Immutable library
------------------
* Used the annotations on the Value Objects (Order and OrderMerge) to reduce the boilerplate code involved in creating
the builder, equals, hashcode and toString methods along with null checking.

Merging:
----------
* Decided to prevent BUY and SELL orders from merging together.  I felt that this kept the design
 simpler and also allows for more flexibility - the two summaries could subsequently be combined or kept separate
 (e.g. in the UI).

 Validation
 ----------
 * The Value Objects include null checks in the builders
 * I assume that a negative order quantity or price is not legal for either BUY or SELL and are therefore rejected at the
  time an order is constructed.
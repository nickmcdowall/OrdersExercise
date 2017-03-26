# Assumptions

### Prices:
I did not use  `BigDecimal ` for the monetary value because:
* I assumed prices are in whole numbers only
* Only basic addition is currently required

### Currency:
* Assumed only pounds sterling currently supported and that the UI would add the currency symbol.

### Quantity:
* Assumed values are always relative to kilograms.
* Assumed UI would add the kg symbol.

# Choices:

### Merging:
* Decided to prevent   `BUY` and  `SELL ` orders from merging together.  I felt that this kept the design
 simpler and also allows for more flexibility - the two summaries could subsequently be combined or kept separate
 (e.g. in the UI).

### @Immutable library
Decided on this occasion to use the  `@Immutable` library to generate the
boilerplate code for the value objects ( `Order` and  `OrderMerge`) so
that I could focus on the business logic of the live order board.  The resulting
functionality was provided:
* A builder with null checks on the mandatory fields
*  `hashCode()`
*  `equals()`
*  `toString()`

### Validation
 * Null checks on mandatory fields
 * I assume that a negative order quantity or price is not legal for either  `BUY` or  `SELL`
 and are therefore rejected by the builder.


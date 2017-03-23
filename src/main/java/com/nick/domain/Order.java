package com.nick.domain;

import org.immutables.value.Value;

@Value.Immutable
@Value.Style(
        builder = "anOrder",
        init = "with*"
)
public interface Order {

    String userId();

    double quantity();

    long price();

    OrderType orderType();

    @Value.Check
    default void quantityAndPriceArePositiveValues() {
        if (quantity() < 0 || price() < 0) {
            throw new IllegalStateException("Quantity and Price must not be negative.");
        }
    }

}

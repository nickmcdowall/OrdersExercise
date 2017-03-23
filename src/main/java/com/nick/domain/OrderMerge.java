package com.nick.domain;

import org.immutables.value.Value;

import java.math.BigDecimal;

@Value.Immutable
@Value.Style(
        builder = "anOrderMerge",
        init = "with*"
)
public interface OrderMerge {

    long price();

    double quanity();

}

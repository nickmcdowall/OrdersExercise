package com.nick.domain;

import java.util.*;

import static com.nick.domain.ImmutableOrderMerge.anOrderMerge;
import static com.nick.domain.OrderType.SELL;
import static java.util.Arrays.stream;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class LiveOrderBoard {

    private static final Comparator<OrderMerge> COMPARE_PRICE = comparing(OrderMerge::price);

    private Collection<Order> orders = new ArrayList<>();

    public void register(Order anOrder) {
        orders.add(anOrder);
    }

    public void register(Order... orders) {
        stream(orders).forEach(this::register);
    }

    public Collection<OrderMerge> summaryFor(OrderType orderType) {
        Map<Long, List<Order>> ordersGroupedByPrice = groupByPrice(orderType);

        Set<Long> distinctPrices = ordersGroupedByPrice.keySet();

        List<OrderMerge> mergedOrders = distinctPrices.stream()
                .map(price -> anOrderMerge()
                        .withPrice(price)
                        .withQuanity(sumQuantitiesFor(ordersGroupedByPrice.get(price)))
                        .build())
                .collect(toList());

        mergedOrders.sort(comparingPriceFor(orderType));

        return mergedOrders;
    }

    public void cancel(Order order) {
        orders.remove(order);
    }

    public Collection<Order> orders() {
        return orders;
    }

    private Comparator<OrderMerge> comparingPriceFor(OrderType orderType) {
        return orderType == SELL ? COMPARE_PRICE : COMPARE_PRICE.reversed();
    }

    private Map<Long, List<Order>> groupByPrice(OrderType orderType) {
        return orders.stream()
                .filter(order -> order.orderType() == orderType)
                .collect(groupingBy(Order::price));
    }

    private double sumQuantitiesFor(List<Order> orders) {
        return orders.stream()
                .mapToDouble(Order::quantity)
                .sum();
    }
}

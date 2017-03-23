package com.nick.domain;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.nick.domain.ImmutableOrder.anOrder;
import static com.nick.domain.ImmutableOrderMerge.anOrderMerge;
import static com.nick.domain.OrderType.BUY;
import static com.nick.domain.OrderType.SELL;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.Is.is;

@RunWith(JUnitParamsRunner.class)
public class LiveOrderBoardTest {

    private static final long DEFAULT_PRICE = 100L;
    private static final double DEFAULT_QUANTITY = 5.0;
    private static final String DEFAULT_USER = "User1";

    private LiveOrderBoard liveOrderBoard;

    @Before
    public void setUp() throws Exception {
        liveOrderBoard = new LiveOrderBoard();
    }

    @Test
    @Parameters(source = OrderType.class)
    public void canRegisterAnOrder(OrderType type) {
        Order order = anOrderForQuantity(3.5, type);

        liveOrderBoard.register(order);

        assertThat(liveOrderBoard.orders(), hasItem(order));
    }

    @Test
    @Parameters(source = OrderType.class)
    public void canCancelARegisteredOrder(OrderType type) {
        Order order = anOrderForQuantity(3.5, type);

        liveOrderBoard.register(order);
        liveOrderBoard.cancel(order);

        assertThat(liveOrderBoard.orders(), is(empty()));
    }

    @Test
    @Parameters(source = OrderType.class)
    public void canSummariseOneRegisteredOrder(OrderType type) {
        Order order = anOrderForQuantity(3.5, type);

        liveOrderBoard.register(order);

        assertThat(liveOrderBoard.summaryFor(type),
                hasItem(anOrderMerge()
                        .withPrice(order.price())
                        .withQuanity(order.quantity())
                        .build()));
    }

    @Test
    @Parameters(source = OrderType.class)
    public void sumOrderQuantityForOrdersWithSamePrice(OrderType type) {
        Order order1 = anOrderForQuantity(7, type);
        Order order2 = anOrderForQuantity(4, type);

        liveOrderBoard.register(order1, order2);

        assertThat(liveOrderBoard.summaryFor(type), hasItem(anOrderMergeForQuantity(11)));
    }

    @Test
    public void sortSummaryDetailsByAscendingPriceForSellOrders() throws Exception {
        Order order1 = anOrderForPrice(2, SELL);
        Order order2 = anOrderForPrice(3, SELL);
        Order order3 = anOrderForPrice(1, SELL);

        liveOrderBoard.register(order1, order2, order3);

        assertThat(liveOrderBoard.summaryFor(SELL), contains(
                anOrderMergeForPrice(1),
                anOrderMergeForPrice(2),
                anOrderMergeForPrice(3)
        ));
    }

    @Test
    public void sortSummaryDetailsByDescendingPriceForBuyOrders() {
        Order order1 = anOrderForPrice(2, BUY);
        Order order2 = anOrderForPrice(3, BUY);
        Order order3 = anOrderForPrice(1, BUY);

        liveOrderBoard.register(order1, order2, order3);

        assertThat(liveOrderBoard.summaryFor(BUY), contains(
                anOrderMergeForPrice(3),
                anOrderMergeForPrice(2),
                anOrderMergeForPrice(1)
        ));
    }

    @Test
    public void buyAndSellOrdersAreNotMerged() throws Exception {
        Order buy = anOrderForPrice(5, BUY);
        Order sell = anOrderForPrice(5, SELL);

        liveOrderBoard.register(buy, sell);

        assertThat(liveOrderBoard.summaryFor(BUY), contains(anOrderMergeForPrice(5)));
        assertThat(liveOrderBoard.summaryFor(SELL), contains(anOrderMergeForPrice(5)));
    }

    @Parameters(source = OrderType.class)
    @Test(expected = IllegalStateException.class)
    public void rejectNegativeOrderPrice(OrderType orderType) throws Exception {
        anOrderForPrice(-1, orderType);
    }

    @Parameters(source = OrderType.class)
    @Test(expected = IllegalStateException.class)
    public void rejectNegativeOrderQuantity(OrderType orderType) throws Exception {
        anOrderForQuantity(-1, orderType);
    }

    private OrderMerge anOrderMergeForPrice(long price) {
        return anOrderMerge().withPrice(price).withQuanity(DEFAULT_QUANTITY).build();
    }

    private OrderMerge anOrderMergeForQuantity(double quantity) {
        return anOrderMerge().withPrice(DEFAULT_PRICE).withQuanity(quantity).build();
    }

    private Order anOrderForPrice(long price, OrderType type) {
        return anOrder()
                .withOrderType(type)
                .withQuantity(DEFAULT_QUANTITY)
                .withPrice(price)
                .withUserId(DEFAULT_USER)
                .build();
    }

    private Order anOrderForQuantity(double quantity, OrderType type) {
        return anOrder()
                .withOrderType(type)
                .withQuantity(quantity)
                .withPrice(DEFAULT_PRICE)
                .withUserId(DEFAULT_USER)
                .build();
    }
}
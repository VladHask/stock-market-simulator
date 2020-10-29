package hask.stockmarketsimulator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;
import java.util.stream.Collectors;

/**
 * OrderBooks
 * Order Books are balanced
 * until there are no more matching orders in them.
 */

public class OrderBook {
    private boolean isBalanced;
    private String symbol; //STOCK : APPL, MSFT, NFXL ...
    private OrderTree bids; // (BUY)
    private OrderTree asks; // (SELL)
    private int id = 0;

    public OrderBook(boolean isBalanced, String symbol, OrderTree bids, OrderTree asks) {
        this.isBalanced = isBalanced;
        this.symbol = symbol;
        this.bids = bids;
        this.asks = asks;
    }

    public LinkedList<Order> getPriceQueueOrders(String action, Integer currentPrice) {
        LinkedList<Order> targetOrder;
        if (action.equals("BUY")) {
            targetOrder = this.getBids()
                    .getSortedOrderTree()
                    .get(currentPrice);

        } else { // (action.equals("SELL")
            targetOrder = this.getAsks()
                    .getSortedOrderTree()
                    .get(currentPrice);
        }
        return targetOrder;
    }

    public LinkedList<Order> getSuitPriceQueueOrders(String action, Integer currentPrice) {
        LinkedList<Order> targetOrders;
        if (action.equals("BUY")) {
            targetOrders = this.getBids()
                    .getSortedOrderTree().entrySet()
                    .stream()
                    .filter(price -> price.getKey() >= currentPrice)
                    .map(Map.Entry::getValue)
                    .flatMap(List::stream)
                    .collect(Collectors.toCollection(LinkedList::new));

        } else { // (action.equals("SELL")
            targetOrders = this.getAsks()
                    .getSortedOrderTree().entrySet()
                    .stream()
                    .filter(price -> price.getKey() <= currentPrice)
                    .map(Map.Entry::getValue)
                    .flatMap(List::stream)
                    .collect(Collectors.toCollection(LinkedList::new));
        }
        return targetOrders;
    }

    @JsonIgnore
    public boolean checkMinAskPrice(int price) {
        if (!this.getAsks().getSortedOrderTree().isEmpty()) {
            return (price >= this.getAsks().getSortedOrderTree().firstKey());
        } else return false;
    }

    @JsonIgnore
    public boolean getMaxBidPrice(int price) {
        if (!this.getBids().getSortedOrderTree().isEmpty()) {
            return (price <= this.getAsks().getSortedOrderTree().lastKey());
        } else return false;
    }

    @JsonIgnore
    public SortedMap<Integer, LinkedList<Order>> getAskMinPriceList() {
        return this.getAsks().getSortedOrderTree(); // sorted ask price tree
    }

    public void checkBalance() {
        if (bids.getSortedOrderTree().isEmpty() && asks.getSortedOrderTree().isEmpty()) {
            this.isBalanced = true;
        } else this.isBalanced = false;
    }

    public void cancelOrder(Order order) {
        if (orderExists(order)) {
            getOrderTreeByAction(order.getAction()).removeOrder(order);
        } else {
            System.out.println("ERROR: There is no order with this id");
        }
    }

    public boolean priceExists(int price, String action) {
        return getOrderTreeByAction(action).getSortedOrderTree().containsKey(price);
    }

    public boolean orderExists(Order order) {
        if (priceExists(order.getPrice(), order.getAction())) {
            return getOrderTreeByAction(order.getAction())
                    .getSortedOrderTree()
                    .get(order.getPrice()).contains(order);
        } else return false;
    }

    public OrderTree getOrderTreeByAction(String action) {
        return action.equals(OrderAction.BUY) ? this.getBids() : this.getAsks();
    }

    @JsonIgnore
    public int getBidMaxPriceList() {
        return this.getBids().getSortedOrderTree().firstKey();
    }

    public List<Order> getAllOrdersFromBook() {
        List<Order> obOrderList = new ArrayList<>();
        obOrderList.addAll(bids.getAllOrdersFromTree());
        obOrderList.addAll(asks.getAllOrdersFromTree());

        return obOrderList;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (bids.getAllOrdersFromTree().size() > 0) {
            sb.append(this.getSymbol()).append("\n")
                    .append(OrderAction.BUY).append("\n")
                    .append(bids.toString()).append("\n");
        }
        if (asks.getAllOrdersFromTree().size() > 0) {
            sb.append(this.getSymbol()).append("\n")
                    .append(OrderAction.SELL).append("\n")
                    .append(asks.toString()).append("\n");
        }
        return sb.toString();
    }

    @JsonIgnore
    public boolean isBalanced() {
        return isBalanced;
    }

    @JsonIgnore
    public void setBalanced(boolean balanced) {
        isBalanced = balanced;
    }

    @JsonIgnore
    public OrderTree getBids() {
        return bids;
    }

    @JsonIgnore
    public OrderTree getAsks() {
        return asks;
    }

    @JsonIgnore
    public String getSymbol() {
        return symbol;
    }

}

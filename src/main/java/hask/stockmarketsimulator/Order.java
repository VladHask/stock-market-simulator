package hask.stockmarketsimulator;

import java.util.Comparator;

/**
 * Represents an order to buy or sell a certain stock,
 * at a certain price and for a certain quantity.
 * <p>
 * Orders can be fully filled (total quantity),
 * or partially filled (only some part of the total quantity).
 *
 */

public class Order {
    private int price;
    private int quantity;
    private int id;
    private String action;
    private String timestamp;
    private OrderBook ob;

    public Order(int id, int quantity, int price, String action, String timestamp, OrderBook ob) {
        this.id = id;
        this.price = price;
        this.action = action;
        this.quantity = quantity;
        this.timestamp = timestamp;
        this.ob = ob;
    }

    static class SortByDate implements Comparator<Order> {
        @Override
        public int compare(Order o1, Order o2) {
            return o1.timestamp.compareTo(o2.timestamp);
        }
    }

    public String logOrder(String act) {
        StringBuilder sb = new StringBuilder();
        return sb.append("\n[")
                .append(timestamp)
                .append("]\tOrder with ID ").append(this.getId())
                .append(String.format(" %s ",act)).append(this.getOb().getSymbol())
                .append(" ").append(this.getAction())
                .append(" ").append(this.getPrice())
                .append(" @ ").append(this.getQuantity()).toString();
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() { return price; }

    public String getTimestamp() {
        return timestamp;
    }

    public int getId() {
        return id;
    }

    public OrderBook getOb() {
        return ob;
    }

    public String getAction() {
        return action;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (id != order.id) return false;
        return timestamp.equals(order.timestamp);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + timestamp.hashCode();
        return result;
    }


}

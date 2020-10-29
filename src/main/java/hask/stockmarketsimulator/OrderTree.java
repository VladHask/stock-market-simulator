package hask.stockmarketsimulator;

import java.util.*;
import java.util.stream.Collectors;

public class OrderTree {

    private SortedMap<Integer, LinkedList<Order>> sortedOrderTree = new TreeMap<>();

    public OrderTree(SortedMap<Integer, LinkedList<Order>> sortedOrderTree) {
        this.sortedOrderTree = sortedOrderTree;
    }

    public void insertOrder(Order order) {
        if (this.sortedOrderTree.containsKey(order.getPrice())) {
            this.sortedOrderTree.get(order.getPrice()).push(order);
        } else {
            LinkedList<Order> orderQueue = new LinkedList<>();
            orderQueue.push(order);
            this.sortedOrderTree.put(order.getPrice(), orderQueue);
        }
        order.getOb().checkBalance();
        System.out.println(order.logOrder("added"));
    }

    public void removeOrder(Order order) {
        for (Iterator<Order> iter = this.sortedOrderTree
                .get(order.getPrice())
                .iterator();
             iter.hasNext(); ) {
            Order iOrder = iter.next();
            if (iOrder.getId() == order.getId()) {
                iter.remove();
                checkForEmptyPriceValues(order.getPrice(), order.getAction());
            }
        }
        order.getOb().checkBalance();
        System.out.println(order.logOrder("removed"));
    }

    public void checkForEmptyPriceValues(int price, String action) {
        if (this.getSortedOrderTree().get(price).isEmpty()) {
            this.getSortedOrderTree().remove(price);
        }
    }

    public List<Order> getAllOrdersFromTree() {
        List<Order> orderList = new ArrayList<>();
        for (Map.Entry<Integer, LinkedList<Order>> entry : this.getSortedOrderTree().entrySet()) {
            LinkedList<Order> value = entry.getValue();
            orderList.addAll(value);
        }
        return orderList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("prices with ID orders: ");
        for (Map.Entry<Integer, LinkedList<Order>> entry : this.getSortedOrderTree().entrySet()) {
            sb.append("price: ").append(entry.getKey().toString()).append("\n")
                    .append("order: ").append(entry.getValue().stream().map(order -> order.getId() + "(Qty:" + order.getQuantity() + ")").collect(Collectors.toList())).append("\n");
        }
        return sb.toString();
    }

    public void updateOrderQty(int qty, Order order) {
        ListIterator<Order> itr = order.getOb().getOrderTreeByAction(order.getAction()).getSortedOrderTree().get(order.getPrice()).listIterator();
        while (itr.hasNext()) {
            Order iOrder = itr.next();
            if (order.getId() == iOrder.getId())
                iOrder.setQuantity(qty);
                System.out.println(order.logOrder("quantity updated"));
        }
    }

    public SortedMap<Integer, LinkedList<Order>> getSortedOrderTree() {
        return sortedOrderTree;
    }
}

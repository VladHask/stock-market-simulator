package hask.stockmarketsimulator;

import java.text.SimpleDateFormat;
import java.util.*;

public class OrderService {
    private static int orderId = 0;

    public static Order mapOrder(Map<String, String> allParams) {
        return new Order(createNextOrderId(),
                Integer.parseInt(allParams.get("quantity")),
                Integer.parseInt(allParams.get("price")),
                allParams.get("action"),
                takeCurrentTimestamp(),
                OrderBookLib.getOrderBookBySymbol(allParams.get("symbol")));
    }

    public static Order getOrderById(int id) {
        for (OrderBook orderBook : OrderBookLib.getOrderBooks()) {
            for (Order order : orderBook.getAllOrdersFromBook()) {
                if (order.getId() == id) {
                    return order;
                }
            }
        }
        throw new NoSuchElementException();
    }

    public static List<Order> getAllOrders() {
        List<Order> listAllOrders = new ArrayList<>();
        for (OrderBook ob : OrderBookLib.getOrderBooks()) {
            listAllOrders.addAll(ob.getAllOrdersFromBook());
        }
        return listAllOrders;
    }

    public static void removeOrderById(int id) {
        try {
            Order order = getOrderById(id);
            order.getOb().getOrderTreeByAction(order.getAction()).removeOrder(order);
        } catch (NoSuchElementException e) {
            System.out.println("Order with this ID is not exist");
        }
    }

    public static String takeCurrentTimestamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ").format(Calendar.getInstance().getTime());
    }

    public static int createNextOrderId() {
        return ++orderId;
    }
}

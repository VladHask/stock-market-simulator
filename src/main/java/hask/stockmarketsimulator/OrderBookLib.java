package hask.stockmarketsimulator;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Class with OrderBook related static fields
 */

public final class OrderBookLib {
    private final static List<OrderBook> orderBooks = new ArrayList<>();

        // pre added books
    public static void initOBList() {
        orderBooks.add(new OrderBook(true, Symbol.AMAZON, new OrderTree(new TreeMap<>()), new OrderTree(new TreeMap<>())));
        orderBooks.add(new OrderBook(true, Symbol.APPLE, new OrderTree(new TreeMap<>()), new OrderTree(new TreeMap<>())));
        orderBooks.add(new OrderBook(true, Symbol.GOOGLE, new OrderTree(new TreeMap<>()), new OrderTree(new TreeMap<>())));
        orderBooks.add(new OrderBook(true, Symbol.MICROSOFT, new OrderTree(new TreeMap<>()), new OrderTree(new TreeMap<>())));
        orderBooks.add(new OrderBook(true, Symbol.NETFLIX, new OrderTree(new TreeMap<>()), new OrderTree(new TreeMap<>())));
        orderBooks.add(new OrderBook(true, Symbol.NVIDIA, new OrderTree(new TreeMap<>()), new OrderTree(new TreeMap<>())));
        orderBooks.add(new OrderBook(true, Symbol.TINKOFF, new OrderTree(new TreeMap<>()), new OrderTree(new TreeMap<>())));
        orderBooks.add(new OrderBook(true, Symbol.YANDEX, new OrderTree(new TreeMap<>()), new OrderTree(new TreeMap<>())));
    }



    public static List<OrderBook> getOrderBooks() {
        return orderBooks;
    }

    public static OrderBook getOrderBookBySymbol(String symbol) {
        for (OrderBook ob : orderBooks) {
            if (ob.getSymbol().equals(symbol)) {
                return ob;
            }
        }
        OrderBook newOB = new OrderBook(true, symbol, new OrderTree(new TreeMap<>()), new OrderTree(new TreeMap<>()));
        orderBooks.add(newOB);
        return newOB;
    }


}
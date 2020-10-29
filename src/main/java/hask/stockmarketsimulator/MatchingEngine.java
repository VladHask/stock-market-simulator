package hask.stockmarketsimulator;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

@Component
public class MatchingEngine {

    public static void runMatching() {
        while (true) {
            try {
                List<TradeLedger> trades = new ArrayList<>();
                Set<Integer> prices = getPriceSet(OrderAction.BUY); // all unique and ordered prices (from all OrderBooks)
                for (int currentPrice : prices) {
                    List<Order> orderList = findTargetQueueOrder(OrderAction.BUY, currentPrice); // collect orders with nicest price
                    for (Order order : orderList) {
                        OrderBook ob = order.getOb();
                        int bidQuantity = order.getQuantity();
                        while (!ob.getBids().getSortedOrderTree().isEmpty() && ob.checkMinAskPrice(order.getPrice()) && bidQuantity > 0) {
                            bidQuantity = matchingOrders(trades, bidQuantity, order);
                        }
                        // order remains
                        if (bidQuantity > 0) { // order partially filled
                            order.setQuantity(bidQuantity);
                            if (bidQuantity != order.getQuantity()) {
                                ob.getBids().updateOrderQty(bidQuantity, order);
                            }
                        } else {
                            order.getOb().getOrderTreeByAction(order.getAction())
                                    .removeOrder(order); // order full filled
                        }
                    }
                }
                Thread.sleep(1000);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }
    }

    /**
     * Processing the main matching logic
     */
    public static int matchingOrders(List<TradeLedger> trades, int qtyRemaining, Order order) {
        String action = order.getAction();
        int buyReportId = 0;
        int sellReportId = 0;
        int endPrice = 0;
        Queue<Order> orderQueue = order.getOb().getSuitPriceQueueOrders(action.equals("BUY") ? "SELL" : "BUY", order.getPrice());
        while (orderQueue.size() > 0 && qtyRemaining > 0) {
            int qtyTraded = 0;
            Order headOrder = orderQueue.poll();
            if (qtyRemaining < headOrder.getQuantity()) {
                qtyTraded = qtyRemaining;
                headOrder.getOb().getOrderTreeByAction(headOrder.getAction())
                        .updateOrderQty(headOrder.getQuantity() - qtyRemaining, headOrder);
                qtyRemaining = 0;
            } else {
                qtyTraded = headOrder.getQuantity();
                headOrder.getOb().getOrderTreeByAction(headOrder.getAction()).removeOrder(headOrder);
                qtyRemaining = qtyRemaining - qtyTraded;
            }
            if (order.getAction().equals(OrderAction.BUY)) {
                buyReportId = order.getId();
                sellReportId = headOrder.getId();
                endPrice = headOrder.getPrice();
            } else {
                buyReportId = headOrder.getId();
                sellReportId = order.getId();
                endPrice = order.getPrice();
            }

            TradeLedger trade = new TradeLedger(order.getTimestamp(), endPrice, qtyTraded,
                    buyReportId, sellReportId, order.getOb().getSymbol());
            sendTrade(trade);
            System.out.println(trade.toString());
        }
        return qtyRemaining;
    }

    /**
     * Triggering controller gateway to trades broadcasting
     * @param trade match order report
     */

    private static void sendTrade(TradeLedger trade) {
        {
            try {
                URL url = new URL("http://localhost:8080/market");
                //make connection
                URLConnection urlc = null;
                urlc = url.openConnection();
                ObjectMapper obj = new ObjectMapper();
                String jsonStr = obj.writeValueAsString(trade);
                urlc.setDoOutput(true);    //use post mode
                urlc.setAllowUserInteraction(false);
                urlc.setRequestProperty("Content-Type", "application/json;");
                PrintStream ps = new PrintStream(urlc.getOutputStream());   //send query
                ps.print(jsonStr);
                ps.close();
                BufferedReader br = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
                String result;
                while ((result = br.readLine()) != null) {  //some feedback
                    System.out.println(result);
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Collecting sorted set of prices
     * @param action
     * @return Sorted List of prices in all OrderBooks of a certain action(BUY/SELL)
     */
    private static Set<Integer> getPriceSet(String action) {
        if (action.equals("SELL")) {
            Set<Integer> askPrices = new TreeSet<>();
            for (OrderBook ob : OrderBookLib.getOrderBooks()) {
                if (!ob.isBalanced()) {
                    for (Map.Entry<Integer, LinkedList<Order>> entry : ob.getAsks().getSortedOrderTree().entrySet()) {
                        askPrices.add(entry.getKey()); // collect the ask prices
                    }
                }
            }
            return askPrices;
        } else {
            NavigableSet<Integer> bidPrices = new TreeSet<>();
            for (OrderBook ob : OrderBookLib.getOrderBooks()) {
                if (!ob.isBalanced()) {
                    for (Map.Entry<Integer, LinkedList<Order>> entry : ob.getBids().getSortedOrderTree().entrySet()) {
                        bidPrices.add(entry.getKey()); // collect the bid prices
                    }
                }
            }
            return bidPrices.descendingSet();
        }
    }

    /**
     * Collect orders from OrderBooks with a certain price.
     * @param action
     * @param currentPrice price which will be use for collecting orders
     * @return
     */
    private static List<Order> findTargetQueueOrder(String action, int currentPrice) {
        List<Order> targetPriceOrderList = new ArrayList<>();
        if (action.equals("BUY")) {
            for (OrderBook ob : OrderBookLib.getOrderBooks()) {
                if (!ob.isBalanced() && !ob.getBids().getSortedOrderTree().isEmpty()) {
                    LinkedList<Order> orderLinkedList = ob.getPriceQueueOrders("BUY", currentPrice);
                    if (orderLinkedList != null) {
                        targetPriceOrderList.addAll(orderLinkedList);
                    }
                }
                targetPriceOrderList.sort(new Order.SortByDate()); // sort to oldest
            }
        } else { // (action.equals("SELL")
            for (OrderBook ob : OrderBookLib.getOrderBooks()) {
                if (!ob.isBalanced() && !ob.getAsks().getSortedOrderTree().isEmpty()) {
                    LinkedList<Order> orderLinkedList = ob.getPriceQueueOrders("SELL", currentPrice);
                    if (orderLinkedList != null) {
                        targetPriceOrderList.addAll(orderLinkedList);
                    }
                }
                targetPriceOrderList.sort(new Order.SortByDate()); // sort to oldest
            }
        }
        return targetPriceOrderList;
    }
}

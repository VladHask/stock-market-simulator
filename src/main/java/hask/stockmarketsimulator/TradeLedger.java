package hask.stockmarketsimulator;

import java.util.ArrayList;
import java.util.List;

/**
 * In such a case, when two orders cross,
 * a Trade (also called Fill or Execution) happens
 * Trades are stored in the Trade Ledger.
 */
public class TradeLedger {

    private static List<TradeLedger> tape = new ArrayList<>();
    private int id;
    private String timestamp;
    private int price;
    private int qty;
    private int buyer;
    private int seller;
    private static int idTradeCounter = 0;
    private String symbolTrade;

    public TradeLedger(String time, int price, int qty,
                       int buyer, int seller, String symbolTrade) {
        this.timestamp = time;
        this.price = price;
        this.qty = qty;
        this.buyer = buyer;
        this.seller = seller;
        this.symbolTrade = symbolTrade;
        this.id = idTradeCounter++;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        return sb.append("\n[")
                .append(timestamp)
                .append("]\tNew execution with ID ")
                .append(id).append(": ")
                .append(symbolTrade).append(" ")
                .append(price).append(" @ ")
                .append(qty).append(" (orders ")
                .append(buyer).append(" and ")
                .append(seller).append(")").toString();
    }

    public String getTimestamp() {
        return timestamp;
    }

    public double getPrice() {
        return price;
    }

    public int getQty() {
        return qty;
    }

    public int getBuyer() {
        return buyer;
    }

    public int getSeller() {
        return seller;
    }

    public static List<TradeLedger> getTape() { return tape; }

    public static void setTape(List<TradeLedger> tape) { TradeLedger.tape = tape; }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public void setPrice(int price) { this.price = price; }

    public void setQty(int qty) { this.qty = qty; }

    public void setBuyer(int buyer) { this.buyer = buyer; }

    public void setSeller(int seller) { this.seller = seller; }

    public static int getIdTradeCounter() { return idTradeCounter; }

    public static void setIdTradeCounter(int idTradeCounter) { TradeLedger.idTradeCounter = idTradeCounter; }

    public String getSymbolTrade() { return symbolTrade; }

    public void setSymbolTrade(String symbolTrade) { this.symbolTrade = symbolTrade; }
}

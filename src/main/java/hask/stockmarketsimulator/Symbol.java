package hask.stockmarketsimulator;

import java.util.ArrayList;
import java.util.List;

public class Symbol {
    public static final String APPLE = "APPL";
    public static final String MICROSOFT = "MSFT";
    public static final String NETFLIX = "NTLX";
    public static final String GOOGLE = "GOOG";
    public static final String AMAZON = "AMZN";
    public static final String YANDEX = "YNDX";
    public static final String TINKOFF = "TIFF";
    public static final String NVIDIA = "NVDA";

    public static List<String> symbolList = new ArrayList<>();

    public static void addSymbol(String symbol){
        symbolList.add(symbol);
    }
}

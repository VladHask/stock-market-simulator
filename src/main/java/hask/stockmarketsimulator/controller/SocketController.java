package hask.stockmarketsimulator.controller;

import hask.stockmarketsimulator.TradeLedger;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
public class SocketController {

    private SimpMessagingTemplate template;

    public SocketController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @PostMapping(value = "/market")
    public void sendTradeInfo(@RequestBody TradeLedger trade) {
        String text = trade.toString();
        this.template.convertAndSend("/topic/market", text);
    }
}


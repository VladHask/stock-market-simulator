package hask.stockmarketsimulator.controller;

import hask.stockmarketsimulator.Order;
import hask.stockmarketsimulator.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("trade")
public class TradingGatewayController {

    @GetMapping(value = "/order")
    private String getAllOrders() {
        StringBuilder sb = new StringBuilder();
        for (Order order : OrderService.getAllOrders()) {
            sb.append(order.logOrder("in")).append("\n");
        }
        return sb.toString();
    }

    @PostMapping(value = "/order")
    public void createOrder(@RequestParam Map<String, String> allParams) {
        Order order = OrderService.mapOrder(allParams);
        order.getOb().getOrderTreeByAction(order.getAction()).insertOrder(order);
    }

    @DeleteMapping("/order")
    public void delete(@RequestParam int id) {
        OrderService.removeOrderById(id);
    }
}

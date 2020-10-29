# stock-market-simulator
Simple market simulator with limit orders.


web-socket trade broadcast on home page (localhost:8080)

for add order
http://localhost:8080/trade/order 
with body: quantity,price,action(BUY/SELL), symbol

for remove order
http://localhost:8080/trade/order?id={orderId}

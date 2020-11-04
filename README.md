# stock-market-simulator
Simple market simulator with limit orders. 
Java, Spring Boot, REST, Websockets (with STOMP and Sockjs)


For web-socket trade broadcast:
#### home page (localhost:8080)

for add order:
#### /trade/order 
with body: quantity,price,action(BUY/SELL), symbol

for remove order:
#### /trade/order?id={orderId}

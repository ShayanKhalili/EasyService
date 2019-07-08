package com.shayank.foodtruckhelper;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;

public class Order {
    private StringBuilder orderText;
    private Map<String, ItemPair> orderDetails;
    private String customerName;

    public Order(String customerName) {
        this.customerName = customerName;
        this.orderText = new StringBuilder();
        this.orderDetails = new HashMap<>();
    }

    public void add(String itemName, String itemPrice, int itemQuantity) {
        this.orderDetails.put(itemName, new ItemPair(itemPrice, itemQuantity));
        this.orderText.append(itemQuantity + " " + itemName + ", ");
    }

    public String getOrderText() {
        return orderText.toString() + "for " + customerName;
    }

    public Map<String, ItemPair> getOrderDetails() {
        return orderDetails;
    }

    public boolean isEmpty() {
        return this.orderDetails.isEmpty();
    }

    public int size() {
        return this.orderDetails.size();
    }

    public class ItemPair {
        private BigDecimal price;
        private BigDecimal quantity;

        public ItemPair(String price, int quantity) {
            this.price = new BigDecimal(price).setScale(2, BigDecimal.ROUND_HALF_EVEN);
            this.quantity = new BigDecimal(quantity).setScale(0, BigDecimal.ROUND_HALF_EVEN);
        }

        public BigDecimal getPrice() {
            return price;
        }

        public BigDecimal getQuantity() {
            return quantity;
        }
    }
}

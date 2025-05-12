package org.example.order.activity;


import io.temporal.activity.ActivityInterface;
import org.example.order.model.Order;

@ActivityInterface
public interface OrderActivities {

    boolean checkInventory(Order order);

    void chargePayment(Order order);

    void shipOrder(Order order);

    void refund(Order order);
}
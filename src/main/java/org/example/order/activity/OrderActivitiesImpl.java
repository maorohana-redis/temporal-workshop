package org.example.order.activity;

import io.temporal.spring.boot.ActivityImpl;
import io.temporal.workflow.Workflow;
import org.example.order.model.Order;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@ActivityImpl(taskQueues = "order-queue")
@Component
public class OrderActivitiesImpl implements OrderActivities {

    private static final Logger logger = Workflow.getLogger(OrderActivitiesImpl.class);

    @Override
    public boolean checkInventory(Order order) {
        return true;
    }

    @Override
    public void chargePayment(Order order) {
        logger.info("Charging payment for order: {}", order.id());
    }

    @Override
    public void shipOrder(Order order) {
        logger.info("Shipping order: {}", order.id());
    }

    @Override
    public void refund(Order order) {
        logger.info("Refunding order: {}", order.id());
    }
}
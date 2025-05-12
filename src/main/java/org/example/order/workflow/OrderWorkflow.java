package org.example.order.workflow;

import io.temporal.workflow.*;
import org.example.order.model.Order;

@WorkflowInterface
public interface OrderWorkflow {

    @WorkflowMethod
    void startOrder(Order order);

    @SignalMethod
    void cancelOrder();

    @QueryMethod
    String getOrderStatus();
}
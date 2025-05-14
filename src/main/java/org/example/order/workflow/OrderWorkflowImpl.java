package org.example.order.workflow;

import java.time.Duration;

import io.temporal.activity.ActivityOptions;
import io.temporal.failure.ActivityFailure;
import io.temporal.failure.ApplicationFailure;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Workflow;
import org.example.order.activity.OrderActivities;
import org.example.order.model.Order;

@WorkflowImpl(taskQueues = "order-queue")
public class OrderWorkflowImpl implements OrderWorkflow {

    private final OrderActivities activities =
        Workflow.newActivityStub(OrderActivities.class,
            ActivityOptions.newBuilder()
                .setStartToCloseTimeout(Duration.ofMinutes(1))
                .build());

    private String status = "Order Received";
    private boolean cancelled = false;

    @Override
    public void startOrder(Order order) {
        status = "Checking Inventory";
        if (!activities.checkInventory(order)) {
            status = "Failed: Out of Stock";
            throw ApplicationFailure.newNonRetryableFailure("Out of Stock", "out-of-stock-failure");
        }

        status = "Processing Payment";
        try {
            activities.chargePayment(order);
        } catch (ActivityFailure activityFailure) {
            status = "Failed: Payment Error";
            throw activityFailure;
        }

        status = "Awaiting Cancellation";
        Workflow.await(Duration.ofSeconds(10), () -> cancelled);

        if (cancelled) {
            status = "Cancelled";
            activities.refund(order);
            return;
        }

        status = "Shipping Order";
        activities.shipOrder(order);

        status = "Completed";
    }

    @Override
    public void cancelOrder() {
        cancelled = true;
    }

    @Override
    public String getOrderStatus() {
        return status;
    }
}
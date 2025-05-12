package com.example.order.workflow;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;

import io.temporal.client.WorkflowClient;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.testing.TestWorkflowExtension;
import io.temporal.worker.Worker;
import org.example.order.activity.OrderActivitiesImpl;
import org.example.order.model.Order;
import org.example.order.workflow.OrderWorkflow;
import org.example.order.workflow.OrderWorkflowImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

public class OrderWorkflowTest {

    @RegisterExtension
    public static final TestWorkflowExtension TEST_WORKFLOW_EXTENSION =
        TestWorkflowExtension.newBuilder()
            .registerWorkflowImplementationTypes(OrderWorkflowImpl.class)
            .setDoNotStart(true)
            .build();

    @BeforeEach
    void setUp(Worker worker, TestWorkflowEnvironment testEnv) {
        worker.registerActivitiesImplementations(new OrderActivitiesImpl());
        testEnv.start();
    }

    @Test
    void testOrderWorkflowCompleted(TestWorkflowEnvironment testEnv, OrderWorkflow workflow) {
        Order order = new Order("001", "Book", 1);
        WorkflowClient.start(workflow::startOrder, order);

        testEnv.sleep(Duration.ofSeconds(12)); // simulate time passing

        String status = workflow.getOrderStatus();
        assertEquals("Completed", status);
    }

    @Test
    void testOrderWorkflowCancelled(TestWorkflowEnvironment testEnv, OrderWorkflow workflow) {
        Order order = new Order("001", "Book", 1);
        WorkflowClient.start(workflow::startOrder, order);

        testEnv.sleep(Duration.ofSeconds(5));

        workflow.cancelOrder();

        String status = workflow.getOrderStatus();
        assertEquals("Cancelled", status);
    }
}
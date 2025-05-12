package org.example.order.api;

import java.util.List;

import io.temporal.api.common.v1.WorkflowExecution;
import io.temporal.api.workflow.v1.WorkflowExecutionInfo;
import io.temporal.api.workflowservice.v1.ListOpenWorkflowExecutionsRequest;
import io.temporal.api.workflowservice.v1.ListOpenWorkflowExecutionsResponse;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.client.WorkflowStub;
import io.temporal.serviceclient.WorkflowServiceStubs;
import org.example.order.model.Order;
import org.example.order.workflow.OrderWorkflow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final WorkflowClient workflowClient;

    @Autowired
    public OrderController(WorkflowClient workflowClient) {
        this.workflowClient = workflowClient;
    }

    @GetMapping("/in-process")
    public List<String> getInProcessOrders() {
        WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
        ListOpenWorkflowExecutionsRequest request = ListOpenWorkflowExecutionsRequest.newBuilder()
            .setNamespace("default")
            .build();

        ListOpenWorkflowExecutionsResponse response = service.blockingStub().listOpenWorkflowExecutions(request);

        return response.getExecutionsList().stream()
            .map(WorkflowExecutionInfo::getExecution)
            .map(WorkflowExecution::getWorkflowId)
            .toList();
    }

    @PostMapping("/cancel/{workflowId}")
    public String cancelOrder(@PathVariable String workflowId) {
        WorkflowStub stub = workflowClient.newUntypedWorkflowStub(workflowId);
        stub.signal("cancelOrder");
        return "Cancellation signal sent to workflow: " + workflowId;
    }

    @PostMapping("/create")
    public String createOrder(Order order) {
        OrderWorkflow workflow = workflowClient.newWorkflowStub(
            OrderWorkflow.class,
            WorkflowOptions.newBuilder()
                .setTaskQueue("order_queue")
                .build()
        );

        WorkflowClient.start(workflow::startOrder, order);
        return workflow.getOrderStatus();
    }
}
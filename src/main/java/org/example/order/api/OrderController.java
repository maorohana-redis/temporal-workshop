package org.example.order.api;

import io.temporal.client.WorkflowClient;
import org.springframework.beans.factory.annotation.Autowired;
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
}
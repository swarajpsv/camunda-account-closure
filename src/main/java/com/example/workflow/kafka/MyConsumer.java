package com.example.workflow.kafka;

import com.example.workflow.services.CamundaService;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MyConsumer {

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    CamundaService camundaService;

    @KafkaListener(topics = "taxes", groupId = "foo")
    public void listenGroupFoo(String message) {
        System.out.println("Tax Calculated: " + message);
        camundaService.registerTax("kafkaCheck", message);
    }
}

package com.example.workflow.services;

import com.example.workflow.models.AccountHolder;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CamundaService {

    @Autowired
    RuntimeService runtimeService;

    public void startProcess(AccountHolder accountHolder) {
        System.out.println("Trying to initiate account closure for: " + accountHolder.getName());
        Map<String, Object> variables = new HashMap<>();
        variables.put("account", accountHolder);
        variables.put("balance", accountHolder.getBalance());
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("accountClosure", variables);
        System.out.println("Workflow started");
    }

    public void startKafkaProcess(AccountHolder accountHolder) {
        System.out.println("Trying to initiate account closure for: " + accountHolder.getName());
        Map<String, Object> variables = new HashMap<>();
        variables.put("account", accountHolder);
        variables.put("balance", accountHolder.getBalance());
        runtimeService.startProcessInstanceByKey("kafkaCheck", variables);
    }

    public void broadcastSignal(String signal) {
        System.out.println("Tax Received from Tax Entity! Broadcasting a signal...");
        runtimeService.signalEventReceived(signal);
    }
}

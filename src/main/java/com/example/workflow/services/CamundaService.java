package com.example.workflow.services;

import com.example.workflow.models.AccountHolder;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
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

    public void registerTax(String processKey, String tax) {
        System.out.println("Generating the signal to proceed...");
        List<Execution> processes = runtimeService.createExecutionQuery().processDefinitionKey(processKey).list();
        Execution execution = processes.get(0);
        runtimeService.setVariable(execution.getId(), "tax", tax);
        runtimeService.signalEventReceived("taxCalculated");
    }
}

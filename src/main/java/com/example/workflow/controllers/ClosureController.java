package com.example.workflow.controllers;

import com.example.workflow.models.AccountHolder;
import com.example.workflow.services.CamundaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClosureController {

    @Autowired
    CamundaService camundaService;

    @PostMapping(value = "/closeAnAccount")
    public String closeAnAccount(@RequestBody AccountHolder accountHolder) {
        camundaService.startProcess(accountHolder);
        return "Request Initiated";
    }

    @PostMapping(value = "/kafkaCheck")
    public String kafkaCheck(@RequestBody AccountHolder accountHolder) {
        camundaService.startKafkaProcess(accountHolder);
        return "Process Started";
    }

    @PostMapping(value = "/receiveSignal")
    public String receiveSignal() {
        camundaService.broadcastSignal("taxCalculated");
        return "Signal Received";
    }
}

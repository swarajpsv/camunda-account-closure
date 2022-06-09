package com.example.workflow.delegates;

import com.example.workflow.models.AccountHolder;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;
import org.json.JSONObject;

import java.io.IOException;

@Component
public class MainDelegate {
//
//    @Autowired
//    KafkaTemplate<String, String> kafkaTemplate;
//
//    public void publishTaxMsg(DelegateExecution execution) {
//        System.out.println("About to publish a message to Tax Calculation Topic...");
//        kafkaTemplate.send("experiment", "Amount = 10000");
//    }

    public void computeKafkaAmount(DelegateExecution execution) {
        System.out.println("Tax Calculated! Closing Account...");
    }

    public void computeFees(DelegateExecution execution) throws IOException {
        System.out.println("Computing Fees...\n");
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://localhost:3000/computeFees");

        AccountHolder accountHolder = (AccountHolder) execution.getVariable("account");
        JSONObject requestJSON = new JSONObject().put("balance", accountHolder.getBalance());
        httpPost.setEntity(new StringEntity(requestJSON.toString()));
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");

        CloseableHttpResponse response = client.execute(httpPost);
        String jsonStr = EntityUtils.toString(response.getEntity());
        client.close();

        float fees = new JSONObject(jsonStr).getFloat("feeAmount");
        execution.setVariable("fees", fees);
    }

    public void computeTax(DelegateExecution execution) throws IOException {
        System.out.println("Computing Taxes...");
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://localhost:3000/computeTax");

        AccountHolder accountHolder = (AccountHolder) execution.getVariable("account");
        JSONObject requestJSON = new JSONObject().put("balance", accountHolder.getBalance());
        httpPost.setEntity(new StringEntity(requestJSON.toString()));
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");

        CloseableHttpResponse response = client.execute(httpPost);
        String jsonStr = EntityUtils.toString(response.getEntity());
        client.close();

        float tax = new JSONObject(jsonStr).getFloat("taxAmount");
        execution.setVariable("tax", tax);
    }

    public void computeNetAmount(DelegateExecution execution) {
        float balance = Float.parseFloat(execution.getVariable("balance").toString());
        float interest = Float.parseFloat(execution.getVariable("interest").toString());
        float fees = Float.parseFloat(execution.getVariable("fees").toString());
        float tax = Float.parseFloat(execution.getVariable("tax").toString());
        System.out.println(String.format("Balance: %.2f\nInterest: %.2f\nFees: %.2f\nTax: %.2f", balance, interest, fees, tax));
        float netAmount = balance + interest - (fees + tax);
        System.out.println("\nNet Amount to be received: " + netAmount);
    }
}

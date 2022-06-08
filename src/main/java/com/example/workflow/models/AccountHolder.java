package com.example.workflow.models;

public class AccountHolder {

    private String name;
    private float balance;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "User [Name=" + name + ", Balance=" + balance + "]";
    }

}

package com.appbanco.mi_appbank.model;

public class Money {

    private double amount;
    private String currency;

    //Constructores.

    public Money() {
    }

    public Money(double amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    //Getters y setters.

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
}

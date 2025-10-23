package com.logsoluprobl.appbank.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Account {
    private String id;
    private Customer owner;
    private Money balance;
    private List<Transaction> transactions;

    public Account() {
        this.transactions = new ArrayList<>();
        this.balance = new Money(0.0, "USD");
    }

    public Account(String id, Customer owner, Money balance) {
        this.id = id;
        this.owner = owner;
        this.balance = balance;
        this.transactions = new ArrayList<>();
    }

    // Métodos abstractos
    public abstract void applyInterest();

    // Operaciones básicas
    public void deposit(Money amount) {
        balance.setAmount(balance.getAmount() + amount.getAmount());
        transactions.add(new Transaction("DEP", amount, this.id));
    }

    public boolean withdraw(Money amount) {
        if (balance.getAmount() >= amount.getAmount()) {
            balance.setAmount(balance.getAmount() - amount.getAmount());
            transactions.add(new Transaction("WDR", amount, this.id));
            return true;
        }
        return false;
    }

    // Getters y setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Customer getOwner() { return owner; }
    public void setOwner(Customer owner) { this.owner = owner; }

    public Money getBalance() { return balance; }
    public void setBalance(Money balance) { this.balance = balance; }

    public List<Transaction> getTransactions() { return transactions; }
    public void setTransactions(List<Transaction> transactions) { this.transactions = transactions; }
}

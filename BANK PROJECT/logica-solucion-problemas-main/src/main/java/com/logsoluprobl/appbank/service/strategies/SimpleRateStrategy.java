package com.logsoluprobl.appbank.service.strategies;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.logsoluprobl.appbank.service.InterestStrategy;

@Component
public class SimpleRateStrategy implements InterestStrategy {

    @Value("${bank.interest.rate:0.01}") 
    private double rate;

    public SimpleRateStrategy() {

    }

    @Override
    public double calculateInterest(double balance) {
        return balance * rate;
    }

    public double getRate() { return rate; }
    
}


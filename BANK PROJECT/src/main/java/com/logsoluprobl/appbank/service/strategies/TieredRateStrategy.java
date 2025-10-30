package com.logsoluprobl.appbank.service.strategies;

import com.logsoluprobl.appbank.service.InterestStrategy;

public class TieredRateStrategy implements InterestStrategy {

    @Override
    public double calculateInterest(double balance) {
        if (balance <= 1000) {
            return balance * 0.01;  // 1% para saldos hasta 1000
        } else if (balance <= 5000) {
            return balance * 0.02;  // 2% para saldos de 1001 a 5000
        } else {
            return balance * 0.03;  // 3% para saldos mayores a 5000
        }
    }
}

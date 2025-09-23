package com.appbanco.mi_appbank.model;

public class CheckingAccount extends Account {

    private double overdraftLimit;

    public CheckingAccount(){

        super(); //Heredar método constructor de clase.

    }

    public CheckingAccount(String id, Customer owner, Money balance, double overdraftLimit){

        super(id, owner, balance);

    }

    @Override
    public void applyInterest(){}
 
    public boolean withdraw(Money amount){ //obtener ~ sacar.

        double available = getBalance().getAmount() + overdraftLimit;
        if (available >= amount.getAmount()) {
            getBalance().setAmount(getBalance().getAmount() - amount.getAmount());
            getTransactions().add(new Transaction("WDR", amount, getId()));
            return true;
        }
        return false;

    }

    public double getOverdraftLimit(){return overdraftLimit;}
    public void setOverdraftLimit(double overdraftLimit){this.overdraftLimit = overdraftLimit;}
    
}

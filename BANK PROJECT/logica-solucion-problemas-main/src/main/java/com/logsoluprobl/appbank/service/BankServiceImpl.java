package com.logsoluprobl.appbank.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.logsoluprobl.appbank.model.Account;
import com.logsoluprobl.appbank.model.CheckingAccount;
import com.logsoluprobl.appbank.model.Customer;
import com.logsoluprobl.appbank.model.Money;
import com.logsoluprobl.appbank.model.SavingsAccount;
import com.logsoluprobl.appbank.model.Transaction;

@Service
public class BankServiceImpl implements BankService {

    private List<Customer> customers;
    private List<Account> accounts;
    private InterestStrategy interestStrategy;

    public BankServiceImpl(InterestStrategy interestStrategy) {
        this.customers = new ArrayList<>();
        this.accounts = new ArrayList<>();
        this.interestStrategy = interestStrategy;
    }

    // ----------- Gestión de Clientes -----------
    @Override
    public Customer createCustomer(String id, String name, String email) {
        Customer customer = new Customer(id, name, email);
        customers.add(customer);
        return customer;
    }

    @Override
    public Customer findCustomerById(String customerId) {
        return customers.stream()
                .filter(c -> c.getId().equals(customerId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customers;
    }

    // ----------- Gestión de Cuentas -----------
    @Override
    public Account createSavingsAccount(String accountId, Customer customer, double interestRate) {
        SavingsAccount account = new SavingsAccount(accountId, customer, new Money(0.0, "USD"), interestRate);
        accounts.add(account);
        return account;
    }

    @Override
    public Account createCheckingAccount(String accountId, Customer customer, double overdraftLimit) {
        CheckingAccount account = new CheckingAccount(accountId, customer, new Money(0.0, "USD"), overdraftLimit);
        accounts.add(account);
        return account;
    }


    @Override
    public Account findAccountById(String accountId) {
        return accounts.stream()
                .filter(a -> a.getId().equals(accountId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Account> getAllAccounts() {
        return accounts;
    }

    // ----------- Operaciones Bancarias -----------
    @Override
    public boolean deposit(String accountId, double amount) {
        Account account = findAccountById(accountId);
        if (account != null) {
            account.deposit(new Money(amount, "USD"));
            return true;
        }
        return false;
    }

    @Override
    public boolean withdraw(String accountId, double amount) {
        Account account = findAccountById(accountId);
        if (account != null) {
            return account.withdraw(new Money(amount, "USD"));
        }
        return false;
    }

    @Override
    public boolean transfer(String fromAccountId, String toAccountId, double amount) {
        Account fromAccount = findAccountById(fromAccountId);
        Account toAccount = findAccountById(toAccountId);

        if (fromAccount != null && toAccount != null) {
            if (fromAccount.withdraw(new Money(amount, "USD"))) {
                toAccount.deposit(new Money(amount, "USD"));
                return true;
            }
        }
        return false;
    }

    // ----------- Intereses -----------
    @Override
    public void applyInterest(String accountId) {
        Account account = findAccountById(accountId);
        if (account != null) {
            double interest = interestStrategy.calculateInterest(account.getBalance().getAmount());
            account.deposit(new Money(interest, "USD"));
        }
    }

    // ----------- Consultas -----------
    @Override
    public List<Transaction> getAccountTransactions(String accountId) {
        Account account = findAccountById(accountId);
        return account != null ? account.getTransactions() : new ArrayList<>();
    }

}

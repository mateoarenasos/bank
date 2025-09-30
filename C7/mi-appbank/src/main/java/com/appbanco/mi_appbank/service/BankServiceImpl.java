package com.appbanco.mi_appbank.service;

import java.util.ArrayList;
import java.util.List;

import com.appbanco.mi_appbank.model.Account;
import com.appbanco.mi_appbank.model.Customer;
import com.appbanco.mi_appbank.service.strategies.InterestStrategy;

public class BankServiceImpl implements BankService {

    private List<Customer> customers;
    private List<Account> accounts;
    private InterestStrategy interestStrategy; //Pendiente de crear.

    public BankServiceImpl(InterestStrategy interestStrategy) {
        this.customers = new ArrayList<>();
        this.accounts = new ArrayList<>();
        this.interestStrategy = interestStrategy;
    }

    //Implementación del método create cliente.
    @Override //Le dice al método que ya existe en el servicio, que se va a implementar específicamente con esta lógica.
    public Customer createCustomer (String id, String name, String email){

        Customer customer = new Customer(id,  name, email);
        customers.add(customer);
        return customer;

    }

    //Implementación del método buscar cliente por el id.
    @Override
    public Customer findCustomerById(String customerId){

        return customers.stream()
            .filter(c -> c.getId().equals(customerId))
            .findFirst().orElse(null);

    }

    //Implementación del método obtener todos los clientes.
    @Override
    public List<Customer> getAllCustomers(){

        return customers;

    }

}
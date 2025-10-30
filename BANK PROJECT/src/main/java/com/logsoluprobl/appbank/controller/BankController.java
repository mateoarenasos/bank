package com.logsoluprobl.appbank.controller;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.logsoluprobl.appbank.exception.DomainException;
import com.logsoluprobl.appbank.model.Account;
import com.logsoluprobl.appbank.model.Customer;
import com.logsoluprobl.appbank.model.Transaction;
import com.logsoluprobl.appbank.service.BankService;

@RestController
@RequestMapping("/api/bank")
public class BankController {

    private final BankService bankService;

    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    // DTO (Data Transfer Object) para crear cuentas (simplificación)
    public static class AccountCreationRequest {
        public String type; // "SAVINGS" o "CHECKING"
        public String accountId;
        public double parameter; // Tasa de interés o Límite de sobregiro
    }

    // ------------------------------
    // Gestión de clientes
    // ------------------------------

    /**
     * Crear un nuevo cliente.
     * @param customer Datos del cliente.
     * @return Cliente creado.
     */

    @PostMapping("/customers")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        try {
            // Se usan los datos del objeto 'customer' recibido
            Customer createdCustomer = bankService.createCustomer(
                customer.getId(), 
                customer.getName(), 
                customer.getEmail()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
        } catch (DomainException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Listar todos los clientes.
     * @return Lista de clientes.
     */
    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = bankService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }
    /**
     * Buscar cliente por id.
     * @return Cliente por id.
     */

    @GetMapping("/customers/{customerId}")
    public ResponseEntity<Customer> findCustomerById(@PathVariable String customerId) {
        Customer customer = bankService.findCustomerById(customerId);
        return customer != null ? ResponseEntity.ok(customer) : ResponseEntity.notFound().build();
    }

    // ------------------------------
    // Gestión de cuentas
    // ------------------------------

    /**
     * Crear una cuenta (Savings o Checking) asociada a un cliente.
     * @param customerId ID del cliente.
     * @param request Datos de la cuenta (tipo y parámetro).
     */
    @PostMapping("/customers/{customerId}/accounts")
    public ResponseEntity<Account> createAccount(@PathVariable String customerId, @RequestBody AccountCreationRequest request) {
        try {
            Customer customer = bankService.findCustomerById(customerId);
            if (customer == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            Account createdAccount;
            if ("SAVINGS".equalsIgnoreCase(request.type)) {
                createdAccount = bankService.createSavingsAccount(
                    request.accountId, customer, request.parameter
                );
            } else if ("CHECKING".equalsIgnoreCase(request.type)) {
                createdAccount = bankService.createCheckingAccount(
                    request.accountId, customer, request.parameter
                );
            } else {
                throw new DomainException("Tipo de cuenta no válido.");
            }
            
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
        } catch (DomainException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Nueva ruta para obtener cuentas
    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<Account> findAccountById(@PathVariable String accountId) {
        Account account = bankService.findAccountById(accountId);
        return account != null ? ResponseEntity.ok(account) : ResponseEntity.notFound().build();
    }

    /**
     * Listar todas las cuentas de un cliente.
     * @param customerId ID del cliente.
     * @return Lista de cuentas.
     */
    @GetMapping("/customers/{customerId}/accounts")
    public ResponseEntity<List<Account>> getAccountsByCustomer(@PathVariable Long customerId) {
        List<Account> accounts = bankService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }

    // ------------------------------
    // Operaciones de transacciones
    // ------------------------------

    /**
     * Realizar un depósito en una cuenta.
     * @param accountId ID de la cuenta.
     * @param amount Monto a depositar.
     * @return Transacción creada.
     */
    @PostMapping("/accounts/{accountId}/deposit")
    public ResponseEntity<Boolean> deposit(@PathVariable String accountId, @RequestParam double amount) {
        try {
            boolean success = bankService.deposit(accountId, amount);
            if(success) {
                return ResponseEntity.status(HttpStatus.CREATED).body(true);
            } else {
                throw new DomainException("Cuenta no encontrada o error al depositar.");
            }
        } catch (DomainException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
    }

    /**
     * Realizar un retiro de una cuenta.
     * @param accountId ID de la cuenta.
     * @param amount Monto a retirar.
     * @return Transacción creada.
     */
    @PostMapping("/accounts/{accountId}/withdraw")
    public ResponseEntity<Boolean> withdraw(@PathVariable String accountId, @RequestParam double amount) {
        try {
            boolean success = bankService.withdraw(accountId, amount);
            if(success) {
                return ResponseEntity.status(HttpStatus.CREATED).body(true);
            } else {
            
                throw new DomainException("Retiro fallido: Saldo insuficiente o cuenta no encontrada.");
            }
        } catch (DomainException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
    }

    public static class TransferRequest {
        public String toAccountId;
        public double amount;
    }

    @PostMapping("/accounts/{fromAccountId}/transfer")
    public ResponseEntity<Boolean> transfer(@PathVariable String fromAccountId, @RequestBody TransferRequest request) {
        try {
            boolean success = bankService.transfer(fromAccountId, request.toAccountId, request.amount);
            if(success) {
                return ResponseEntity.ok(true);
            } else {
                throw new DomainException("Transferencia fallida: Cuentas inválidas o fondos insuficientes.");
            }
        } catch (DomainException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
    }

    /**
     * Consultar todas las transacciones de una cuenta.
     * @param accountId ID de la cuenta.
     * @return Lista de transacciones.
     */
    @GetMapping("/accounts/{accountId}/transactions")
    public ResponseEntity<List<Transaction>> getTransactions(@PathVariable String accountId) {
        List<Transaction> transactions = bankService.getAccountTransactions(accountId);
        // Si la cuenta no existe, el servicio devuelve una lista vacía, lo cual es manejable.
        return ResponseEntity.ok(transactions);
    }

    // ------------------------------
    // Intereses
    // ------------------------------
    
    @PostMapping("/accounts/{accountId}/apply-interest")
    public ResponseEntity<Void> applyInterest(@PathVariable String accountId) {
        bankService.applyInterest(accountId);
        return ResponseEntity.ok().build();
    }

    // ------------------------------
    // Manejo global de errores
    // ------------------------------

    /**
     * Manejo global de errores DomainException para devolver mensajes legibles.
     */
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<String> handleDomainException(DomainException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}

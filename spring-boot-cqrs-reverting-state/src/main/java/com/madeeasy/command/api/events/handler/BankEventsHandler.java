package com.madeeasy.command.api.events.handler;

import com.madeeasy.command.api.data.BankAccount;
import com.madeeasy.command.api.data.BankAccountRepository;
import com.madeeasy.command.api.events.AccountCreatedEvent;
import com.madeeasy.command.api.events.AccountFrozenEvent;
import com.madeeasy.command.api.events.AccountUnfrozenEvent;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
public class BankEventsHandler {
    private final BankAccountRepository bankAccountRepository;

    public BankEventsHandler(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    @EventHandler
    public void on(AccountCreatedEvent event) {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountId(event.getAccountId());
        bankAccountRepository.save(bankAccount);
    }

    @EventHandler
    public void on(AccountFrozenEvent event) {
        BankAccount bankAccount = bankAccountRepository.findById(event.getAccountId())
                .orElseThrow(() -> new RuntimeException("Bank account not found"));
        bankAccount.setFrozen(true);
        bankAccountRepository.save(bankAccount);
    }

    @EventHandler
    public void on(AccountUnfrozenEvent event) {
        BankAccount bankAccount = bankAccountRepository.findById(event.getAccountId())
                .orElseThrow(() -> new RuntimeException("Bank account not found"));
        bankAccount.setFrozen(false);
        bankAccountRepository.save(bankAccount);
    }
}

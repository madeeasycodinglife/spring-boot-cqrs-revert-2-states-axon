package com.madeeasy.command.api.controller;

import com.madeeasy.command.api.commands.CreateAccountCommand;
import com.madeeasy.command.api.commands.FreezeAccountCommand;
import com.madeeasy.command.api.commands.RevertToStateCommand;
import com.madeeasy.command.api.commands.UnfreezeAccountCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/bank-accounts")
@SuppressWarnings("all")
public class BankAccountCommandController {

    private final CommandGateway commandGateway;

    public BankAccountCommandController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping
    public CompletableFuture<String> createAccount() {
        String accountId = UUID.randomUUID().toString();
        CreateAccountCommand command = new CreateAccountCommand(accountId);
        return commandGateway.send(command);
    }

    @PostMapping("/{accountId}/freeze")
    public CompletableFuture<String> freezeAccount(@PathVariable String accountId) {
        FreezeAccountCommand command = new FreezeAccountCommand(accountId);
        return commandGateway.send(command);
    }

    @PostMapping("/{accountId}/revert")
    public CompletableFuture<String> revertToState(@PathVariable String accountId,
                                                   @RequestParam("state") String stateToRevert) {
        RevertToStateCommand command = new RevertToStateCommand(accountId, stateToRevert);
        return commandGateway.send(command);
    }

    @PostMapping("/{accountId}/unfreeze")
    public CompletableFuture<String> unfreezeAccount(@PathVariable String accountId) {
        UnfreezeAccountCommand command = new UnfreezeAccountCommand(accountId);
        return commandGateway.send(command);
    }


}


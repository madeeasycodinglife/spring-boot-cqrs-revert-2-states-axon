package com.madeeasy.command.api.aggregate;

import com.madeeasy.command.api.commands.CreateAccountCommand;
import com.madeeasy.command.api.commands.FreezeAccountCommand;
import com.madeeasy.command.api.commands.RevertToStateCommand;
import com.madeeasy.command.api.commands.UnfreezeAccountCommand;
import com.madeeasy.command.api.events.AccountCreatedEvent;
import com.madeeasy.command.api.events.AccountFrozenEvent;
import com.madeeasy.command.api.events.AccountUnfrozenEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class BankAccountAggregate {
    @AggregateIdentifier
    private String accountId;
    private boolean frozen;

    public BankAccountAggregate() {
        // Default no-arg constructor required by Axon
    }

    @CommandHandler
    public BankAccountAggregate(CreateAccountCommand command) {
        AggregateLifecycle.apply(new AccountCreatedEvent(command.getAccountId()));
    }

    @EventSourcingHandler
    public void on(AccountCreatedEvent event) {
        this.accountId = event.getAccountId();
        this.frozen = false;
    }

    @CommandHandler
    public void handle(FreezeAccountCommand command) {
        if (!frozen) {
            AggregateLifecycle.apply(new AccountFrozenEvent(accountId));
        }
    }

    @EventSourcingHandler
    public void on(AccountFrozenEvent event) {
        this.frozen = true;
    }

    @CommandHandler
    public void handle(UnfreezeAccountCommand command) {
        if (frozen) {
            AggregateLifecycle.apply(new AccountUnfrozenEvent(accountId));
        }
    }

    @EventSourcingHandler
    public void on(AccountUnfrozenEvent event) {
        this.frozen = false;
    }

    @CommandHandler
    public void handle(RevertToStateCommand command) {
        String stateToRevert = command.getStateToRevert();
        if (stateToRevert.equals("frozen")) {
            if (!frozen) {
                AggregateLifecycle.apply(new AccountFrozenEvent(accountId));
            }
        } else if (stateToRevert.equals("unfrozen")) {
            if (frozen) {
                AggregateLifecycle.apply(new AccountUnfrozenEvent(accountId));
            }
        }
    }
}

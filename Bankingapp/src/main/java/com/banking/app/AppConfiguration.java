package com.banking.app;

import org.axonframework.eventsourcing.AggregateFactory;
import org.axonframework.eventsourcing.AggregateSnapshotter;
import org.axonframework.eventsourcing.EventCountSnapshotTriggerDefinition;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventsourcing.GenericAggregateFactory;
import org.axonframework.eventsourcing.Snapshotter;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.banking.app.aggregate.Account;

@Configuration
public class AppConfiguration {

	@Autowired
	private EventStore eventStore;

	@Bean
	public AggregateFactory<Account> aggregateFactory() {
		return new GenericAggregateFactory<Account>(Account.class);
	}

	@Bean
	public EventCountSnapshotTriggerDefinition countSnapshotTriggerDefinition() {
		return new EventCountSnapshotTriggerDefinition(snapShotter(), 3);
	}

	@Bean
	public Snapshotter snapShotter() {
		return new AggregateSnapshotter(eventStore, aggregateFactory());
	}

	@Bean
	public EventSourcingRepository<Account> accountRepository() {
		return new EventSourcingRepository<>(aggregateFactory(), eventStore, countSnapshotTriggerDefinition());
	}

	
}

package com.xiaoke1256.thirdpay.infrastructure.event;

import com.xiaoke1256.thirdpay.domain.event.DomainEvent;
import org.springframework.context.ApplicationEvent;

/**
 * 领域事件适配器
 * 将领域事件适配为Spring的应用事件
 */
public class DomainEventAdapter extends ApplicationEvent {

    private final DomainEvent domainEvent;

    public DomainEventAdapter(DomainEvent domainEvent) {
        super(domainEvent);
        this.domainEvent = domainEvent;
    }

    public DomainEvent getDomainEvent() {
        return domainEvent;
    }
}
package com.xiaoke1256.thirdpay.infrastructure.event;

import com.xiaoke1256.thirdpay.domain.event.DomainEvent;
import com.xiaoke1256.thirdpay.domain.event.DomainEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Spring实现的领域事件发布器
 */
@Component
public class SpringDomainEventPublisher implements DomainEventPublisher {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publish(DomainEvent event) {
        // 将领域事件转换为Spring的应用事件
        applicationEventPublisher.publishEvent(new DomainEventAdapter(event));
    }
}
package com.xiaoke1256.thirdpay.domain.event;

/**
 * 领域事件发布器接口
 */
public interface DomainEventPublisher {
    
    /**
     * 发布领域事件
     * @param event 领域事件
     */
    void publish(DomainEvent event);
}
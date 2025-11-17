package com.xiaoke1256.thirdpay.domain.event;

import java.time.LocalDateTime;

/**
 * 领域事件接口
 */
public interface DomainEvent {
    
    /**
     * 获取事件ID
     * @return 事件ID
     */
    String getEventId();
    
    /**
     * 获取事件发生时间
     * @return 事件发生时间
     */
    LocalDateTime getTimestamp();
}
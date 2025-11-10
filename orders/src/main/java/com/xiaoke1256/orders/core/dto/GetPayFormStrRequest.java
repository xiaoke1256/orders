package com.xiaoke1256.orders.core.dto;

import lombok.Data;

@Data
public class GetPayFormStrRequest {
    private String payType;
    private String merchantOrderNo;
    private String remark;
}

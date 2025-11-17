package com.xiaoke1256.thirdpay.domain.model.aggregate;

import com.xiaoke1256.thirdpay.domain.model.valueobject.AccountNo;
import com.xiaoke1256.thirdpay.domain.model.valueobject.Money;

import java.time.LocalDateTime;

/**
 * 分户账聚合根
 */
public class HouseholdAcc {
    private Long accId;
    private final AccountNo accNo;
    private final String accName;
    private Money balance;
    private final LocalDateTime insertTime;
    private LocalDateTime updateTime;

    private HouseholdAcc(Builder builder) {
        this.accId = builder.accId;
        this.accNo = builder.accNo;
        this.accName = builder.accName;
        this.balance = builder.balance;
        this.insertTime = builder.insertTime;
        this.updateTime = builder.updateTime;
    }

    public static class Builder {
        private Long accId;
        private AccountNo accNo;
        private String accName;
        private Money balance;
        private LocalDateTime insertTime = LocalDateTime.now();
        private LocalDateTime updateTime = LocalDateTime.now();

        public Builder setAccId(Long accId) {
            this.accId = accId;
            return this;
        }

        public Builder setAccNo(AccountNo accNo) {
            this.accNo = accNo;
            return this;
        }

        public Builder setAccName(String accName) {
            this.accName = accName;
            return this;
        }

        public Builder setBalance(Money balance) {
            this.balance = balance;
            return this;
        }

        public Builder setInsertTime(LocalDateTime insertTime) {
            this.insertTime = insertTime;
            return this;
        }

        public Builder setUpdateTime(LocalDateTime updateTime) {
            this.updateTime = updateTime;
            return this;
        }

        public HouseholdAcc build() {
            validate();
            return new HouseholdAcc(this);
        }

        private void validate() {
            if (accNo == null) {
                throw new IllegalArgumentException("Account number cannot be null");
            }
            if (accName == null || accName.trim().isEmpty()) {
                throw new IllegalArgumentException("Account name cannot be null or empty");
            }
            if (balance == null) {
                this.balance = Money.of(0);
            }
        }
    }

    // 业务行为

    /**
     * 扣款操作
     * @param amount 扣款金额
     * @throws IllegalStateException 余额不足时抛出异常
     */
    public void debit(Money amount) {
        if (balance.isLessThan(amount)) {
            throw new IllegalStateException("Insufficient balance");
        }
        this.balance = this.balance.subtract(amount);
        this.updateTime = LocalDateTime.now();
    }

    /**
     * 收款操作
     * @param amount 收款金额
     */
    public void credit(Money amount) {
        this.balance = this.balance.add(amount);
        this.updateTime = LocalDateTime.now();
    }

    /**
     * 检查余额是否足够
     * @param amount 要检查的金额
     * @return 余额是否足够
     */
    public boolean hasSufficientBalance(Money amount) {
        return balance.isGreaterThanOrEqual(amount);
    }

    // Getters

    public Long getAccId() {
        return accId;
    }

    public AccountNo getAccNo() {
        return accNo;
    }

    public String getAccName() {
        return accName;
    }

    public Money getBalance() {
        return balance;
    }

    public LocalDateTime getInsertTime() {
        return insertTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setAccId(Long accId) {
        this.accId = accId;
    }
}
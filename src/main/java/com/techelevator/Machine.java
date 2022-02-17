package com.techelevator;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Machine {

    // Instance Variables
    private double insertMoneyInt;
    private BigDecimal balance = new BigDecimal("0.00");
    private boolean isPurchaseProduct = false;
    private BigDecimal price = new BigDecimal("0.00");

    // Constructors
    public void increaseBalance() {
        BigDecimal insertMoneyBD = BigDecimal.valueOf(insertMoneyInt);
         balance = balance.add(insertMoneyBD);
    }

    public void decreaseBalance() {
                balance = balance.subtract(price).setScale(2, RoundingMode.HALF_UP);

    }

    // Getters and Setters

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public double getInsertMoneyInt() {

        return insertMoneyInt;
    }

    public void setInsertMoney(double insertMoney) {

        this.insertMoneyInt = insertMoney;
    }

    public void setInsertMoneyInt(int insertMoneyInt) {
        this.insertMoneyInt = insertMoneyInt;
    }

    public boolean isPurchaseProduct() {
        return isPurchaseProduct;
    }

    public void setPurchaseProduct(boolean purchaseProduct) {
        isPurchaseProduct = purchaseProduct;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}


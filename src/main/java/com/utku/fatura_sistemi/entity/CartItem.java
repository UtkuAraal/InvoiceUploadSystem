package com.utku.fatura_sistemi.entity;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.beans.property.SimpleStringProperty;

public class CartItem {
    private SimpleStringProperty name;
    private SimpleIntegerProperty unitPrice;
    private SimpleIntegerProperty quantity;
    private SimpleIntegerProperty amount;

    public CartItem() {
    }

    public CartItem(String name, int unitPrice, int quantity, int amount) {
        this.name = new SimpleStringProperty(name);
        this.unitPrice = new SimpleIntegerProperty(unitPrice);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.amount = new SimpleIntegerProperty(amount);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public int getUnitPrice() {
        return unitPrice.get();
    }

    public SimpleIntegerProperty unitPriceProperty() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice.set(unitPrice);
    }

    public int getQuantity() {
        return quantity.get();
    }

    public SimpleIntegerProperty quantityProperty() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public int getAmount() {
        return amount.get();
    }

    public SimpleIntegerProperty amountProperty() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount.set(amount);
    }
}

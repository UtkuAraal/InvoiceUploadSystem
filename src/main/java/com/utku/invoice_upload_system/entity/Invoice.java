package com.utku.invoice_upload_system.entity;

public class Invoice {
    private int id;
    private String seri;
    private String number;
    private int totalAmount;
    private int discount;
    private int amountToPay;
    private int customerId;

    public Invoice() {
    }

    public Invoice(int id, String seri, String number, int totalAmount, int discount, int amountToPay, int customerId) {
        this.id = id;
        this.seri = seri;
        this.number = number;
        this.totalAmount = totalAmount;
        this.discount = discount;
        this.amountToPay = amountToPay;
        this.customerId = customerId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSeri() {
        return seri;
    }

    public void setSeri(String seri) {
        this.seri = seri;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getAmountToPay() {
        return amountToPay;
    }

    public void setAmountToPay(int amountToPay) {
        this.amountToPay = amountToPay;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
}

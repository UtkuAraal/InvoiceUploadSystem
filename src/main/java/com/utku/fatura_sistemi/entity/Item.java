package com.utku.fatura_sistemi.entity;

public class Item {
    private int id;
    private String name;
    private int unitPrice;

    public Item(){

    }

    public Item(int id, String name, int unitPrice){
        this.id = id;
        this.name = name;
        this.unitPrice = unitPrice;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }
}

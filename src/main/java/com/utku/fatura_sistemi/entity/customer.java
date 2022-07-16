package com.utku.fatura_sistemi.entity;

public class customer {
    private int id;
    private String nameSurname;
    private String ssNumber;

    public customer(){

    }

    public customer(int id, String nameSurname, String ssNumber){
        this.nameSurname = nameSurname;
        this.ssNumber = ssNumber;
    }

    public int getId(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getNameSurname(){
        return this.nameSurname;
    }

    public void setNameSurname(String nameSurname){
        this.nameSurname = nameSurname;
    }

    public String getSsNumber(){
        return this.ssNumber;
    }

    public void setSsNumber(String ssNumber){
        this.ssNumber = ssNumber;
    }
}

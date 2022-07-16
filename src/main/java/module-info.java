module com.utku.fatura_sistemi {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires json.simple;


    opens com.utku.fatura_sistemi to javafx.fxml;
    exports com.utku.fatura_sistemi;
    exports com.utku.fatura_sistemi.controller;
    opens com.utku.fatura_sistemi.controller to javafx.fxml;
    opens com.utku.fatura_sistemi.entity to javafx.fxml;
    exports com.utku.fatura_sistemi.entity;
}
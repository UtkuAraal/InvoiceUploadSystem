module com.utku.invoice_upload_system {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires json.simple;


    opens com.utku.invoice_upload_system to javafx.fxml;
    exports com.utku.invoice_upload_system;
    exports com.utku.invoice_upload_system.controller;
    opens com.utku.invoice_upload_system.controller to javafx.fxml;
    opens com.utku.invoice_upload_system.entity to javafx.fxml;
    exports com.utku.invoice_upload_system.entity;
}
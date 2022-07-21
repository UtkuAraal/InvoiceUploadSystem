package com.utku.invoice_upload_system.controller;

import com.utku.invoice_upload_system.HelloApplication;
import com.utku.invoice_upload_system.Statics;
import com.utku.invoice_upload_system.entity.Invoice;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class MainMenuController {

    public void createInvoice(ActionEvent event) throws IOException {
        Statics.customer = null;
        Statics.invoice = new Invoice();
        Statics.invoiceItemsList = new ArrayList<>();
        ((Node)event.getSource()).getScene().getWindow().hide();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("CreateInvoice.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 587, 770);
        Stage stage = new Stage();
        stage.setTitle("Fatura Oluştur");
        stage.setScene(scene);
        stage.show();
    }

    public void xmlInvoice(ActionEvent event) throws IOException {
        Statics.invoice = null;
        Statics.customer = null;
        Statics.outputItems = null;
        ((Node)event.getSource()).getScene().getWindow().hide();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("InvoiceXMLOutput.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 330);
        Stage stage = new Stage();
        stage.setTitle("Fatura XML Formu");
        stage.setScene(scene);
        stage.show();
    }

    public void jsonInvoice(ActionEvent event) throws IOException {
        Statics.invoice = null;
        Statics.customer = null;
        Statics.outputItems = null;
        ((Node)event.getSource()).getScene().getWindow().hide();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("InvoiceJSONOutput.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 330);
        Stage stage = new Stage();
        stage.setTitle("Fatura JSON Formu");
        stage.setScene(scene);
        stage.show();
    }

    public void deleteInvoice(ActionEvent event) throws IOException {
        Statics.invoice = null;
        Statics.customer = null;
        ((Node)event.getSource()).getScene().getWindow().hide();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("DeleteInvoice.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 381);
        Stage stage = new Stage();
        stage.setTitle("Fatura Silme Formu");
        stage.setScene(scene);
        stage.show();
    }

    public void exit(){
        Statics.database.closeDatabase();
        Platform.exit();
    }



}

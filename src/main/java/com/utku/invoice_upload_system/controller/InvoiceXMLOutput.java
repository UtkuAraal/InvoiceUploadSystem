package com.utku.invoice_upload_system.controller;

import com.utku.invoice_upload_system.HelloApplication;
import com.utku.invoice_upload_system.Statics;
import com.utku.invoice_upload_system.utils.XmlOutputService;
import com.utku.invoice_upload_system.view.XmlOutputForms;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;


public class InvoiceXMLOutput {

    @FXML
    private ListView invoiceInfo;

    @FXML
    private Label successlbl;

    public void chooseInvoice(){
        Dialog dialog = new Dialog();
        dialog.setTitle("Fatura Seçim Ekranı");
        dialog.getDialogPane().setContent(new XmlOutputForms().chooseInvoiceForm());
        dialog.showAndWait();
        if (Statics.invoice != null){
            addToScreen();
        }
    }

    public void addToScreen(){
        invoiceInfo.getItems().clear();
        invoiceInfo.getItems().add("Fatura Seri: " + Statics.invoice.getSeri());
        invoiceInfo.getItems().add("Fatura Numarası: " + Statics.invoice.getNumber());
        invoiceInfo.getItems().add("");
        invoiceInfo.getItems().add("Müşteri Adı: " + Statics.customer.getNameSurname());
        invoiceInfo.getItems().add("Müşteri TCKN: " + Statics.customer.getSsNumber());
        invoiceInfo.getItems().add("");
        invoiceInfo.getItems().add("Fatura Tutarı: " + Statics.invoice.getAmountToPay());
    }

    public void saveForXML(ActionEvent event){
        if(!(Statics.invoice == null)){
            XmlOutputService xmlService = new XmlOutputService();
            xmlService.XMLOutputFile(Statics.invoice, Statics.outputItems, Statics.customer);
            successlbl.setVisible(true);
            PauseTransition visiblePause = new PauseTransition(
                    Duration.seconds(3)
            );
            visiblePause.setOnFinished(
                    e -> successlbl.setVisible(false)
            );
            visiblePause.play();
            Statics.invoice = null;
            Statics.outputItems = null;
            Statics.customer = null;
            invoiceInfo.getItems().clear();
        }
    }

    public void close(ActionEvent event){
        goToMainMenu(event);
    }

    private void goToMainMenu(ActionEvent event){
        try{
            ((Node)event.getSource()).getScene().getWindow().hide();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("MainMenu.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 450, 375);
            Stage stage = new Stage();
            stage.setTitle("Ana Menü");
            stage.setScene(scene);
            stage.show();
        }catch (Exception e){
            System.out.println(e);
        }
    }
}

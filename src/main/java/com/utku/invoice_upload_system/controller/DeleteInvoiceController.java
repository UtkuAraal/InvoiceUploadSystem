package com.utku.invoice_upload_system.controller;

import com.utku.invoice_upload_system.HelloApplication;
import com.utku.invoice_upload_system.Statics;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.SQLException;


public class DeleteInvoiceController {

    @FXML
    private TextField invoiceSerialTxtField;

    @FXML
    private TextField invoiceNumberTxtField;

    @FXML
    private ListView invoiceInfo;

    @FXML
    private Label successlbl;

    @FXML
    private Label warninglbl;

    public void findInvoice(){
        if(invoiceSerialTxtField.getText().equals("") || invoiceNumberTxtField.getText().equals("")){
            warninglbl.setVisible(true);
            PauseTransition visiblePause = new PauseTransition(
                    Duration.seconds(3)
            );
            visiblePause.setOnFinished(
                    e -> warninglbl.setVisible(false)
            );
            visiblePause.play();

        }else{
            try{
                Statics.invoice = Statics.database.findInvoiceBySeriAndNumber(invoiceSerialTxtField.getText(), invoiceNumberTxtField.getText());
                if(Statics.invoice.getSeri().equals("")){
                    throw new Exception("Fatura bulunamadı!");
                }
                Statics.customer = Statics.database.getCustomerFromInvoice(Statics.invoice.getCustomerId());
                addToScreen();
            }catch (Exception e){
                Statics.invoice = null;
                Statics.customer = null;
                invoiceInfo.getItems().clear();
                invoiceInfo.getItems().add("Bu seriye ve numaraya ait fatura bulunmamaktadır !");
            }
        }

    }


    public void deleteInvoice() throws SQLException {

        if(Statics.invoice == null || Statics.customer == null){
            warninglbl.setVisible(true);
            PauseTransition visiblePause = new PauseTransition(
                    Duration.seconds(3)
            );
            visiblePause.setOnFinished(
                    e -> warninglbl.setVisible(false)
            );
            visiblePause.play();

        }else{
            Statics.database.deleteInvoice(Statics.invoice.getId());

            successlbl.setVisible(true);
            PauseTransition visiblePause = new PauseTransition(
                    Duration.seconds(3)
            );
            visiblePause.setOnFinished(
                    e -> successlbl.setVisible(false)
            );
            visiblePause.play();
            invoiceInfo.getItems().clear();
            Statics.invoice = null;
            Statics.customer = null;
        }
    }

    public void close(ActionEvent event){
        goToMainMenu(event);
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

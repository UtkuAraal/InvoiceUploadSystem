package com.utku.fatura_sistemi.controller;

import com.utku.fatura_sistemi.HelloApplication;
import com.utku.fatura_sistemi.dataAccess.IDatabaseDal;
import com.utku.fatura_sistemi.entity.Invoice;
import com.utku.fatura_sistemi.entity.customer;
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

    private IDatabaseDal database = HelloApplication.database;

    private Invoice choosenInvoice;
    private customer customerInformation;

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

        }else{
            try{
                choosenInvoice = database.findInvoiceBySeriAndNumber(invoiceSerialTxtField.getText(), invoiceNumberTxtField.getText());
                if(choosenInvoice.getSeri().equals("")){
                    throw new Exception("Fatura bulunamadı!");
                }
                customerInformation = database.getCustomerFromInvoice(choosenInvoice.getId());
                addToScreen();
            }catch (Exception e){
                choosenInvoice = null;
                customerInformation = null;
                invoiceInfo.getItems().clear();
                invoiceInfo.getItems().add("Bu seriye ve numaraya ait fatura bulunmamaktadır !");
            }
        }

    }


    public void deleteInvoice() throws SQLException {

        if(choosenInvoice == null || customerInformation == null){
            warninglbl.setVisible(true);
            PauseTransition visiblePause = new PauseTransition(
                    Duration.seconds(3)
            );
            visiblePause.setOnFinished(
                    e -> warninglbl.setVisible(false)
            );
            visiblePause.play();

        }else{
            database.deleteInvoice(choosenInvoice.getId());

            successlbl.setVisible(true);
            PauseTransition visiblePause = new PauseTransition(
                    Duration.seconds(3)
            );
            visiblePause.setOnFinished(
                    e -> successlbl.setVisible(false)
            );
            visiblePause.play();
            invoiceInfo.getItems().clear();
            choosenInvoice = null;
            customerInformation = null;
        }
    }

    public void close(ActionEvent event){
        goToMainMenu(event);
    }

    public void addToScreen(){
        invoiceInfo.getItems().clear();
        invoiceInfo.getItems().add("Fatura Seri: " + choosenInvoice.getSeri());
        invoiceInfo.getItems().add("Fatura Numarası: " + choosenInvoice.getNumber());
        invoiceInfo.getItems().add("");
        invoiceInfo.getItems().add("Müşteri Adı: " + customerInformation.getNameSurname());
        invoiceInfo.getItems().add("Müşteri TCKN: " + customerInformation.getSsNumber());
        invoiceInfo.getItems().add("");
        invoiceInfo.getItems().add("Fatura Tutarı: " + choosenInvoice.getAmountToPay());
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

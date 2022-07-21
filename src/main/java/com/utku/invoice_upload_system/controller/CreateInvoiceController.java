package com.utku.invoice_upload_system.controller;

import com.utku.invoice_upload_system.HelloApplication;
import com.utku.invoice_upload_system.Statics;
import com.utku.invoice_upload_system.entity.*;
import com.utku.invoice_upload_system.view.CustomerForms;
import com.utku.invoice_upload_system.view.ItemForms;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class CreateInvoiceController implements Initializable{

    @FXML
    private TextField invoiceSerial;

    @FXML
    private TextField invoiceNumber;

    @FXML
    private ListView customerInfo;

    @FXML
    private TableView<CartItem> cart = new TableView<>();

    @FXML
    private TableColumn<CartItem, String> itemName = new TableColumn<>();

    @FXML
    private TableColumn<CartItem, Double> itemUnitPrice = new TableColumn<>();

    @FXML
    private TableColumn<CartItem, Integer> itemQuantity = new TableColumn<>();

    @FXML
    private TableColumn<CartItem, Double> itemAmount = new TableColumn<>();

    @FXML
    private TextField discountAmount;

    @FXML
    private ListView invoiceSummary;

    @FXML
    private Label warninglbl;

    public void chooseCustomer() throws SQLException {
        Dialog dialog = new Dialog();
        CustomerForms customerForms = new CustomerForms();
        dialog.setTitle("Müşteri Seçim Ekranı");
        dialog.getDialogPane().setContent(customerForms.chooseCustomerForm());
        dialog.showAndWait();

        if(Statics.customer != null){
            customerInfo.getItems().clear();
            customerInfo.getItems().add("Adı: " + Statics.customer.getNameSurname());
            customerInfo.getItems().add("TCKN: " + Statics.customer.getSsNumber());
        }
    }

    public void calculateInvoice(){
        if(!(discountAmount.getText().equals(""))){
            double discount = Double.parseDouble(discountAmount.getText());
            double total = 0;
            for(InvoiceItems item : Statics.invoiceItemsList){
                total += item.getAmount();
            }

            String totalFormat = Statics.decimalFormat.format(total).replace(",",".");
            total = Double.parseDouble(totalFormat);
            if (discount > total){
                return;
            }
            double amountToPay = total - discount;

            Statics.invoice.setTotalAmount(total);
            Statics.invoice.setDiscount(discount);
            Statics.invoice.setAmountToPay(amountToPay);

            invoiceSummary.getItems().clear();
            invoiceSummary.getItems().add("Toplam Tutar: " + total);
            invoiceSummary.getItems().add("Ödenecek Tutar: " + amountToPay);
        }
    }

    public void addProduct() throws SQLException {
        Dialog dialog = new Dialog();
        dialog.setTitle("Ürün Seçim Formu");
        dialog.getDialogPane().setContent(new ItemForms().chooseItemForm());
        dialog.showAndWait();
        if(Statics.cartItem !=  null){
            System.out.println(Statics.cartItem.getAmount());
            cart.getItems().add(Statics.cartItem);
            Statics.cartItem = null;
        }
    }

    public void cancel(ActionEvent event) throws IOException {
        goToMainMenu(event);
    }

    private void goToMainMenu(ActionEvent event) throws IOException{
        ((Node)event.getSource()).getScene().getWindow().hide();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("MainMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 450, 375);
        Stage stage = new Stage();
        stage.setTitle("Ana Menü");
        stage.setScene(scene);
        stage.show();
    }

    public void save(ActionEvent event) throws SQLException, IOException {
        if(invoiceSerial.getText().equals("") || invoiceNumber.getText().equals("") || Statics.customer == null || Statics.invoiceItemsList.size() == 0 || Statics.invoice.getAmountToPay() == 0){
            warninglbl.setVisible(true);
        }else{
            Statics.invoice.setSeri(invoiceSerial.getText());
            Statics.invoice.setNumber(invoiceNumber.getText());
            Statics.invoice.setCustomerId(Statics.customer.getId());
            Statics.database.addInvoice(Statics.invoice, Statics.invoiceItemsList);
            goToMainMenu(event);
        }
    }


    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        itemName.setCellValueFactory(new PropertyValueFactory<CartItem, String>("name"));
        itemUnitPrice.setCellValueFactory(new PropertyValueFactory<CartItem, Double>("unitPrice"));
        itemQuantity.setCellValueFactory(new PropertyValueFactory<CartItem, Integer>("quantity"));
        itemAmount.setCellValueFactory(new PropertyValueFactory<CartItem, Double>("amount"));
    }
}

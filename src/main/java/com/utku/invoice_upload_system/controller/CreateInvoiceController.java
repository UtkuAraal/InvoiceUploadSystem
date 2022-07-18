package com.utku.invoice_upload_system.controller;

import com.utku.invoice_upload_system.HelloApplication;
import com.utku.invoice_upload_system.dataAccess.IDatabaseDal;
import com.utku.invoice_upload_system.entity.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CreateInvoiceController implements Initializable{


    private IDatabaseDal database = HelloApplication.database;

    private Invoice invoice = new Invoice();
    private Customer choosenCustomer = null;
    private List<InvoiceItems> invoiceItemsList = new ArrayList<>();

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
    private TableColumn<CartItem, Integer> itemUnitPrice = new TableColumn<>();

    @FXML
    private TableColumn<CartItem, Integer> itemQuantity = new TableColumn<>();

    @FXML
    private TableColumn<CartItem, Integer> itemAmount = new TableColumn<>();

    @FXML
    private TextField discountAmount;

    @FXML
    private ListView invoiceSummary;

    @FXML
    private Label warninglbl;





    public void chooseCustomer() throws SQLException {
        Dialog dialog = new Dialog();
        dialog.setTitle("Müşteri Seçim Ekranı");
        dialog.getDialogPane().setContent(chooseCustomerForm());
        dialog.show();

    }

    private Node chooseCustomerForm() throws SQLException {
        GridPane gridPane = new GridPane();
        TableView tableView = new TableView();

        TableColumn<Customer, String> nameCol = new TableColumn<>("Adı");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("nameSurname"));

        TableColumn<Customer, String> ssNumberCol = new TableColumn<>("TCKN");
        ssNumberCol.setCellValueFactory(new PropertyValueFactory<>("ssNumber"));

        tableView.getColumns().add(nameCol);
        tableView.getColumns().add(ssNumberCol);

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        List<Customer> customerList = database.getCustomers();

        for (Customer custom : customerList){
            tableView.getItems().add(custom);
        }

        Button chooseCustomerbtn = new Button("Müşteriyi seç");
        chooseCustomerbtn.setWrapText(true);
        chooseCustomerbtn.setDisable(true);

        Button newCustomerbtn = new Button("Yeni Müşteri ");
        newCustomerbtn.setWrapText(true);


        Button cancelbtn = new Button("Vazgeç");
        cancelbtn.setWrapText(true);

        TableView.TableViewSelectionModel<Customer> selectionModel = tableView.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        ObservableList<Customer> selectedItems = selectionModel.getSelectedItems();

        selectedItems.addListener(new ListChangeListener<Customer>() {
            @Override
            public void onChanged(Change<? extends Customer> c) {
                chooseCustomerbtn.setDisable(false);
            }
        });

        chooseCustomerbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                choosenCustomer = (Customer) selectedItems.get(0);
                ((Node)event.getSource()).getScene().getWindow().hide();
                customerInfo.getItems().clear();
                customerInfo.getItems().add("Adı: " + choosenCustomer.getNameSurname());
                customerInfo.getItems().add("TCKN: " + choosenCustomer.getSsNumber());
            }
        });

        newCustomerbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Dialog dialog = new Dialog();
                dialog.setTitle("Müşteri Ekle");
                dialog.getDialogPane().setContent(addNewCustomerForm());
                dialog.show();
                ((Node)event.getSource()).getScene().getWindow().hide();
            }
        });

        cancelbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ((Node)event.getSource()).getScene().getWindow().hide();
            }
        });

        gridPane.add(tableView, 0, 0);
        gridPane.add(cancelbtn, 0, 1);
        gridPane.add(chooseCustomerbtn, 1, 1);
        gridPane.add(newCustomerbtn, 1, 2);
        return gridPane;
    }

    private Node addNewCustomerForm(){
        GridPane gridPane = new GridPane();
        gridPane.add(new Label("Adı Soyadı: "), 0, 0);
        gridPane.add(new Label("T.C. Kimlik Numarası: "), 0, 1);

        TextField nameTxtField = new TextField();
        TextField ssNumberTxtField = new TextField();

        gridPane.add(nameTxtField, 1, 0);
        gridPane.add(ssNumberTxtField, 1, 1);

        Button addCustomerToDB = new Button("Kaydet");
        addCustomerToDB.setWrapText(true);

        Button cancelbtn = new Button("Vazgeç");
        cancelbtn.setWrapText(true);

        addCustomerToDB.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!(nameTxtField.getText().equals("") || ssNumberTxtField.getText().equals(""))){
                    try {
                        Customer newCustomer = database.addNewCustomer(nameTxtField.getText(), ssNumberTxtField.getText());
                        choosenCustomer = newCustomer;
                        ((Node)event.getSource()).getScene().getWindow().hide();
                        customerInfo.getItems().clear();
                        customerInfo.getItems().add("Adı: " + choosenCustomer.getNameSurname());
                        customerInfo.getItems().add("TCKN: " + choosenCustomer.getSsNumber());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        cancelbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ((Node)event.getSource()).getScene().getWindow().hide();
            }
        });

        gridPane.add(addCustomerToDB, 1, 2);
        gridPane.add(cancelbtn, 0, 2);

        return gridPane;


    }

    public void calculateInvoice(){
        if(!(discountAmount.getText().equals(""))){
            int discount = Integer.parseInt(discountAmount.getText());
            int total = 0;
            for(InvoiceItems item : invoiceItemsList){
                total += item.getAmount();
            }
            if (discount > total){
                return;
            }
            int amountToPay = total - discount;

            invoice.setTotalAmount(total);
            invoice.setDiscount(discount);
            invoice.setAmountToPay(amountToPay);

            invoiceSummary.getItems().clear();
            invoiceSummary.getItems().add("Toplam Tutar: " + total);
            invoiceSummary.getItems().add("Ödenecek Tutar: " + amountToPay);
        }
    }

    public void addProduct() throws SQLException {
        Dialog dialog = new Dialog();
        dialog.setTitle("Ürün Seçim Formu");
        dialog.getDialogPane().setContent(chooseItemForm());
        dialog.show();

    }

    public Node chooseItemForm() throws SQLException {
        GridPane gridPane = new GridPane();
        TableView tableView = new TableView();

        TableColumn<Item, String> nameCol = new TableColumn<>("Adı");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Item, Integer> unitPriceCol = new TableColumn<>("Unit Price");
        unitPriceCol.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));

        tableView.getColumns().add(nameCol);
        tableView.getColumns().add(unitPriceCol);

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        List<Item> itemList = database.getItems();

        for (Item item : itemList){
            tableView.getItems().add(item);
        }

        Label quantitylbl = new Label("Miktar: ");
        TextField quantityTxtField = new TextField();

        // Field is forced to be numeric only.
        quantityTxtField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    quantityTxtField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        Label requirelbl = new Label("Miktar bilgisi gereklidir.");
        requirelbl.setTextFill(Color.RED);
        requirelbl.setVisible(false);


        Button chooseItembtn = new Button("Ürün Seç");
        chooseItembtn.setWrapText(true);
        chooseItembtn.setDisable(true);

        Button newItembtn = new Button("Yeni Ürün");
        newItembtn.setWrapText(true);


        Button cancelbtn = new Button("Vazgeç");
        cancelbtn.setWrapText(true);

        TableView.TableViewSelectionModel<Item> selectionModel = tableView.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        ObservableList<Item> selectedItems = selectionModel.getSelectedItems();

        selectedItems.addListener(new ListChangeListener<Item>() {
            @Override
            public void onChanged(Change<? extends Item> i) {
                chooseItembtn.setDisable(false);
            }
        });

        chooseItembtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if(quantityTxtField.getText().equals("")){
                    requirelbl.setVisible(true);
                }else{
                    Item item = (Item) selectedItems.get(0);
                    InvoiceItems invoiceItems = new InvoiceItems();
                    invoiceItems.setItemId(item.getId());
                    invoiceItems.setQuantity(Integer.parseInt(quantityTxtField.getText()));
                    invoiceItems.setAmount(Integer.parseInt(quantityTxtField.getText()) * item.getUnitPrice());
                    invoiceItemsList.add(invoiceItems);
                    ((Node)event.getSource()).getScene().getWindow().hide();

                    CartItem cartItem = new CartItem(item.getName(), item.getUnitPrice(), Integer.parseInt(quantityTxtField.getText()), Integer.parseInt(quantityTxtField.getText()) * item.getUnitPrice());
                    cart.getItems().add(cartItem);
                }


            }
        });

        newItembtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Dialog dialog = new Dialog();
                dialog.setTitle("Ürün Ekle");
                dialog.getDialogPane().setContent(addNewItemForm());
                dialog.show();
                ((Node)event.getSource()).getScene().getWindow().hide();
            }
        });

        cancelbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ((Node)event.getSource()).getScene().getWindow().hide();
            }
        });

        gridPane.add(tableView, 0, 0);
        gridPane.add(quantitylbl, 0, 1);
        gridPane.add(quantityTxtField, 1, 1);
        gridPane.add(cancelbtn, 0, 2);
        gridPane.add(requirelbl, 0,3);
        gridPane.add(chooseItembtn, 1, 2);
        gridPane.add(newItembtn, 1, 3);
        return gridPane;

    }

    private Node addNewItemForm(){
        GridPane gridPane = new GridPane();
        gridPane.add(new Label("Ürün adı: "), 0, 0);
        gridPane.add(new Label("Birim Fiyatı: "), 0, 1);

        TextField nameTxtField = new TextField();
        TextField unitPriceTxtField = new TextField();

        unitPriceTxtField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    unitPriceTxtField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        gridPane.add(nameTxtField, 1, 0);
        gridPane.add(unitPriceTxtField, 1, 1);

        Button addItemToDB = new Button("Kaydet");
        addItemToDB.setWrapText(true);

        Button cancelbtn = new Button("Vazgeç");
        cancelbtn.setWrapText(true);

        addItemToDB.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    database.addNewItem(nameTxtField.getText(), unitPriceTxtField.getText());
                    ((Node)event.getSource()).getScene().getWindow().hide();
                    Dialog dialog = new Dialog();
                    dialog.setTitle("Ürün Seçim Formu");
                    dialog.getDialogPane().setContent(chooseItemForm());
                    dialog.show();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        cancelbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ((Node)event.getSource()).getScene().getWindow().hide();
            }
        });

        gridPane.add(addItemToDB, 1, 2);
        gridPane.add(cancelbtn, 0, 2);

        return gridPane;


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
        if(invoiceSerial.getText().equals("") || invoiceNumber.getText().equals("") || choosenCustomer == null || invoiceItemsList.size() == 0 || invoice.getAmountToPay() == 0){
            warninglbl.setVisible(true);
        }else{
            invoice.setSeri(invoiceSerial.getText());
            invoice.setNumber(invoiceNumber.getText());
            invoice.setCustomerId(choosenCustomer.getId());
            database.addInvoice(invoice, invoiceItemsList);
            goToMainMenu(event);
        }
    }


    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        itemName.setCellValueFactory(new PropertyValueFactory<CartItem, String>("name"));
        itemUnitPrice.setCellValueFactory(new PropertyValueFactory<CartItem, Integer>("unitPrice"));
        itemQuantity.setCellValueFactory(new PropertyValueFactory<CartItem, Integer>("quantity"));
        itemAmount.setCellValueFactory(new PropertyValueFactory<CartItem, Integer>("amount"));


        discountAmount.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    discountAmount.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }


}

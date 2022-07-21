package com.utku.invoice_upload_system.view;

import com.utku.invoice_upload_system.Statics;
import com.utku.invoice_upload_system.entity.CartItem;
import com.utku.invoice_upload_system.entity.InvoiceItems;
import com.utku.invoice_upload_system.entity.Item;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.sql.SQLException;
import java.util.List;

public class ItemForms {
    public Node chooseItemForm() throws SQLException {
        GridPane gridPane = new GridPane();
        TableView tableView = new TableView();

        TableColumn<Item, String> nameCol = new TableColumn<>("Adı");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Item, Double> unitPriceCol = new TableColumn<>("Unit Price");
        unitPriceCol.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));

        tableView.getColumns().add(nameCol);
        tableView.getColumns().add(unitPriceCol);

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        for (Item item : Statics.database.getItems()){
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
                    String amountFormat = Statics.decimalFormat.format((Integer.parseInt(quantityTxtField.getText()) * item.getUnitPrice())).replace(",",".");
                    double amount = Double.valueOf(amountFormat);
                    invoiceItems.setAmount(amount);
                    Statics.invoiceItemsList.add(invoiceItems);
                    ((Node)event.getSource()).getScene().getWindow().hide();
                    Statics.cartItem = new CartItem(item.getName(), item.getUnitPrice(), Integer.parseInt(quantityTxtField.getText()), amount);
                }


            }
        });

        newItembtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Dialog dialog = new Dialog();
                dialog.setTitle("Ürün Ekle");
                dialog.getDialogPane().setContent(addNewItemForm());
                dialog.showAndWait();
                tableView.getItems().clear();
                for (Item item : Statics.database.getItems()){
                    tableView.getItems().add(item);
                }


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

        gridPane.add(nameTxtField, 1, 0);
        gridPane.add(unitPriceTxtField, 1, 1);

        Button addItemToDB = new Button("Kaydet");
        addItemToDB.setWrapText(true);

        Button cancelbtn = new Button("Vazgeç");
        cancelbtn.setWrapText(true);

        addItemToDB.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                    Statics.database.addNewItem(nameTxtField.getText(), unitPriceTxtField.getText());
                    ((Node)event.getSource()).getScene().getWindow().hide();

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
}

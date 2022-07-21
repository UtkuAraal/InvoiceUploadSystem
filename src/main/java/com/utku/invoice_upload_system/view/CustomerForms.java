package com.utku.invoice_upload_system.view;

import com.utku.invoice_upload_system.Statics;
import com.utku.invoice_upload_system.entity.Customer;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import java.sql.SQLException;
import java.util.List;

public class CustomerForms {
    public Node chooseCustomerForm() throws SQLException {
        GridPane gridPane = new GridPane();
        TableView tableView = new TableView();

        TableColumn<Customer, String> nameCol = new TableColumn<>("Adı");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("nameSurname"));

        TableColumn<Customer, String> ssNumberCol = new TableColumn<>("TCKN");
        ssNumberCol.setCellValueFactory(new PropertyValueFactory<>("ssNumber"));

        tableView.getColumns().add(nameCol);
        tableView.getColumns().add(ssNumberCol);

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        List<Customer> customerList = Statics.database.getCustomers();

        for (Customer custom : customerList) {
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
                Statics.customer = (Customer) selectedItems.get(0);
                ((Node) event.getSource()).getScene().getWindow().hide();

            }
        });

        newCustomerbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Dialog dialog = new Dialog();
                dialog.setTitle("Müşteri Ekle");
                dialog.getDialogPane().setContent(addNewCustomerForm());
                dialog.showAndWait();
                ((Node) event.getSource()).getScene().getWindow().hide();
            }
        });

        cancelbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ((Node) event.getSource()).getScene().getWindow().hide();
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
                        Customer newCustomer = Statics.database.addNewCustomer(nameTxtField.getText(), ssNumberTxtField.getText());
                        Statics.customer = newCustomer;
                        ((Node)event.getSource()).getScene().getWindow().hide();
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
}

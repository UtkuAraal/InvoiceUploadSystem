package com.utku.invoice_upload_system.controller;

import com.utku.invoice_upload_system.HelloApplication;
import com.utku.invoice_upload_system.Statics;
import com.utku.invoice_upload_system.dataAccess.IDatabaseDal;
import com.utku.invoice_upload_system.entity.Invoice;
import com.utku.invoice_upload_system.entity.OutputItem;
import com.utku.invoice_upload_system.entity.Customer;
import com.utku.invoice_upload_system.utils.JsonOutputService;
import javafx.animation.PauseTransition;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.List;

public class InvoiceJSONOutput {


    private Invoice choosenInvoice;
    private List<OutputItem> items;
    private Customer customerInformation;

    @FXML
    private ListView invoiceInfo;

    @FXML
    private Label successlbl;

    public void chooseInvoice(){
        Dialog dialog = new Dialog();
        dialog.setTitle("Fatura Seçim Ekranı");
        dialog.getDialogPane().setContent(chooseInvoiceForm());
        dialog.show();
    }

    public Node chooseInvoiceForm(){
        GridPane gridPane = new GridPane();
        TableView tableView = new TableView();

        TableColumn<Invoice, String> seriCol = new TableColumn<>("Fatura Seri");
        seriCol.setCellValueFactory(new PropertyValueFactory<>("seri"));

        TableColumn<Invoice, String> numberCol = new TableColumn<>("Fatura Numarası");
        numberCol.setCellValueFactory(new PropertyValueFactory<>("number"));

        tableView.getColumns().add(seriCol);
        tableView.getColumns().add(numberCol);

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        List<Invoice> invoiceList = Statics.database.getInvoices();

        for (Invoice invoice : invoiceList){
            tableView.getItems().add(invoice);
        }

        Button chooseInvoicebtn = new Button("Fatura seç");
        chooseInvoicebtn.setWrapText(true);
        chooseInvoicebtn.setDisable(true);

        Button cancelbtn = new Button("Vazgeç");
        cancelbtn.setWrapText(true);

        TableView.TableViewSelectionModel<Invoice> selectionModel = tableView.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        ObservableList<Invoice> selectedItems = selectionModel.getSelectedItems();

        selectedItems.addListener(new ListChangeListener<Invoice>() {
            @Override
            public void onChanged(Change<? extends Invoice> i) {
                chooseInvoicebtn.setDisable(false);
            }
        });

        chooseInvoicebtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                choosenInvoice = (Invoice) selectedItems.get(0);
                ((Node)event.getSource()).getScene().getWindow().hide();
                items = Statics.database.getInvoiceItems(choosenInvoice.getId());
                customerInformation = Statics.database.getCustomerFromInvoice(choosenInvoice.getCustomerId());
                addToScreen();
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
        gridPane.add(chooseInvoicebtn, 1, 1);
        return gridPane;
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

    public void saveForJSON(ActionEvent event){
        if(!(choosenInvoice == null)){
            try{
                JsonOutputService jsonService = new JsonOutputService();
                jsonService.JSONOutputFile(choosenInvoice, items, customerInformation);

                successlbl.setVisible(true);
                PauseTransition visiblePause = new PauseTransition(
                        Duration.seconds(3)
                );
                visiblePause.setOnFinished(
                        e -> successlbl.setVisible(false)
                );
                visiblePause.play();
                choosenInvoice = null;
                items = null;
                customerInformation = null;
                invoiceInfo.getItems().clear();
            }catch (Exception e){
                System.out.println(e);
            }

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

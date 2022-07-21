package com.utku.invoice_upload_system.view;

import com.utku.invoice_upload_system.Statics;
import com.utku.invoice_upload_system.entity.Invoice;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import java.util.List;

public class XmlOutputForms {
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
                Statics.invoice = (Invoice) selectedItems.get(0);
                ((Node)event.getSource()).getScene().getWindow().hide();
                Statics.outputItems = Statics.database.getInvoiceItems(Statics.invoice.getId());
                Statics.customer = Statics.database.getCustomerFromInvoice(Statics.invoice.getCustomerId());

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
}

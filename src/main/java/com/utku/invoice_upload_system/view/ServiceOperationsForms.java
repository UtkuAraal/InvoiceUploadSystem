package com.utku.invoice_upload_system.view;

import com.utku.invoice_upload_system.Statics;
import com.utku.invoice_upload_system.entity.Customer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ServiceOperationsForms {
    public Node queryUInvoiceForm(){
        GridPane gridPane = new GridPane();
        Statics.Type = "Seri Numarası";

        ToggleGroup typeGroup = new ToggleGroup();
        RadioButton serialbtn = new RadioButton("Seri Numarası  ");
        serialbtn.setToggleGroup(typeGroup);
        serialbtn.setSelected(true);
        RadioButton namebtn = new RadioButton("İsim ");
        namebtn.setToggleGroup(typeGroup);

        Label typetxt = new Label("Arama Türü: ");

        gridPane.add(typetxt,0 ,0);
        gridPane.add(serialbtn, 1, 0);
        gridPane.add(namebtn, 2, 0);

        Label searchlbl = new Label("Aranacak Seri Numarası : ");

        typeGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>()
        {
            public void changed(ObservableValue<? extends Toggle> ob,
                                Toggle o, Toggle n)
            {

                RadioButton rb = (RadioButton)typeGroup.getSelectedToggle();

                if (rb != null) {
                    String type = rb.getText();

                    searchlbl.setText("Aranacak " + type + " : ");
                    Statics.Type = type;
                }
            }
        });

        gridPane.add(searchlbl, 0, 1);

        TextField valueTxtField = new TextField();

        gridPane.add(valueTxtField, 0, 2);


        Button query = new Button("Sorgula");
        query.setWrapText(true);

        Button cancelbtn = new Button("Vazgeç");
        cancelbtn.setWrapText(true);

        query.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!valueTxtField.getText().equals("")){
                    Statics.Value = valueTxtField.getText();
                    ((Node)event.getSource()).getScene().getWindow().hide();
                }

            }
        });

        cancelbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ((Node)event.getSource()).getScene().getWindow().hide();
            }
        });

        gridPane.add(query, 2, 3);
        gridPane.add(cancelbtn, 0, 3);

        return gridPane;


    }



    public Node ChooseInvoice(){
        GridPane gridPane = new GridPane();
        Statics.Type = "xml";

        ToggleGroup typeGroup = new ToggleGroup();
        RadioButton xmlbtn = new RadioButton("XML   ");
        xmlbtn.setToggleGroup(typeGroup);
        xmlbtn.setSelected(true);
        RadioButton jsonbtn = new RadioButton("Json ");
        jsonbtn.setToggleGroup(typeGroup);

        Label typetxt = new Label("Arama Türü: ");

        gridPane.add(typetxt,0 ,0);
        gridPane.add(xmlbtn, 1, 0);
        gridPane.add(jsonbtn, 2, 0);

        Label searchlbl = new Label("XML formatındaki faturalar : ");
        ListView invoices = new ListView();

        invoices.getItems().clear();
        List<String> records = getInvoiceFiles();
        for(String invoice : records){
            invoices.getItems().add(invoice);
        }

        if(records.size() == 0){
            invoices.getItems().add("Kayıtlı fatura bulunamadı!");
        }

        typeGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>()
        {
            public void changed(ObservableValue<? extends Toggle> ob,
                                Toggle o, Toggle n)
            {

                RadioButton rb = (RadioButton)typeGroup.getSelectedToggle();

                if (rb != null) {
                    String type = rb.getText().trim();

                    searchlbl.setText(type.trim() + " formatındaki faturalar :  ");
                    Statics.Type = type.toLowerCase(Locale.ROOT);

                    invoices.getItems().clear();
                    List<String> records = getInvoiceFiles();
                    for(String invoice : records){
                        invoices.getItems().add(invoice);
                    }

                    if(records.size() == 0){
                        invoices.getItems().add("Kayıtlı fatura bulunamadı!");
                    }
                }
            }
        });

        gridPane.add(searchlbl, 0, 1);
        gridPane.add(invoices, 0, 2);


        Button sendInvoice = new Button("Gönder");
        sendInvoice.setWrapText(true);
        sendInvoice.setDisable(true);

        Button cancelbtn = new Button("Vazgeç");
        cancelbtn.setWrapText(true);

        sendInvoice.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try{
                    File file = new File((String)invoices.getSelectionModel().getSelectedItem());
                    Statics.Value = new String(Files.readAllBytes(Paths.get(file.getPath())));;
                    ((Node)event.getSource()).getScene().getWindow().hide();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        invoices.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                sendInvoice.setDisable(false);
            }
        });

        cancelbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ((Node)event.getSource()).getScene().getWindow().hide();
            }
        });

        gridPane.add(sendInvoice, 2, 3);
        gridPane.add(cancelbtn, 0, 3);

        return gridPane;

    }

    private List<String> getInvoiceFiles(){
        try{
            List<String> fileList = new ArrayList<>();
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(""), "*." + Statics.Type.toLowerCase(Locale.ROOT))) {
                for (Path path : stream) {
                    if (!Files.isDirectory(path) && !path.getFileName().toString().equals("pom.xml")) {
                        fileList.add(path.getFileName()
                                .toString());
                    }
                }
            }
            return fileList;

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}

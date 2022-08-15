package com.utku.invoice_upload_system.controller;

import com.utku.invoice_upload_system.HelloApplication;
import com.utku.invoice_upload_system.Statics;
import com.utku.invoice_upload_system.utils.HttpRequests;
import com.utku.invoice_upload_system.utils.TcpConnections;
import com.utku.invoice_upload_system.view.ServiceOperationsForms;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class ServiceOperationsController{

    @FXML
    private TextField httpUrl;

    @FXML
    private TextField tcpIp;

    @FXML
    private TextField tcpPort;

    @FXML
    private TextArea responseView;

    @FXML
    private Label warninglbl;

    private String url;
    private String ip;
    private int port;
    private byte type;

    public void queryInvoice(){
        if(getServerDetails()){

            Dialog dialog = new Dialog();
            dialog.setTitle("Sorgulanacak fatura formu");
            dialog.getDialogPane().setContent(new ServiceOperationsForms().queryUInvoiceForm());
            dialog.showAndWait();

            if(Statics.Type != null && Statics.Value != null){
                String response = "";
                if(type == 1){
                    response += new TcpConnections().QueryInvoice(tcpIp.getText(), Integer.parseInt(tcpPort.getText()), Statics.Type, Statics.Value);
                }else{

                    HttpRequests httpRequests = new HttpRequests();
                    String queryBy;
                    if(Statics.Type.trim().equals("Seri Numarası")){
                        queryBy = "serial";
                    }else{
                        queryBy = "name";
                    }
                    response += httpRequests.getReq(url, queryBy, Statics.Value);
                }
                responseView.setText(response);
            }


        }
    }

    public void uploadInvoice(){
        if(getServerDetails()){
            Dialog dialog = new Dialog();
            dialog.setTitle("Fatura yükleme formu");
            dialog.getDialogPane().setContent(new ServiceOperationsForms().ChooseInvoice());
            dialog.showAndWait();

            if(Statics.Type != null && Statics.Value != null){
                String response = "";

                if(type == 1){
                    response += new TcpConnections().uploadInvoice(tcpIp.getText(), Integer.parseInt(tcpPort.getText()), Statics.Type, Statics.Value);
                }else{
                    HttpRequests httpRequests = new HttpRequests();
                    response += httpRequests.postReq(url, Statics.Type, Statics.Value);
                }
                responseView.setText(response);
            }
        }
    }

    public boolean getServerDetails(){
        if(httpUrl.getText().equals("") && !tcpIp.getText().equals("") && !tcpPort.getText().equals("")){
            ip = tcpIp.getText();
            port = Integer.parseInt(tcpPort.getText());
            type = 1;
            return true;
        }else if(!httpUrl.getText().equals("") && (tcpIp.getText().equals("") && tcpPort.getText().equals(""))){
            url = httpUrl.getText();
            type = 2;
            return true;
        }else{
            warninglbl.setVisible(true);
            return false;
        }
    }

    public void mainMenu(ActionEvent event) throws IOException {
        ((Node)event.getSource()).getScene().getWindow().hide();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("MainMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 450, 375);
        Stage stage = new Stage();
        stage.setTitle("Ana Menü");
        stage.setScene(scene);
        stage.show();
    }
}

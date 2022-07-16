package com.utku.fatura_sistemi;

import com.utku.fatura_sistemi.dataAccess.IDatabaseDal;
import com.utku.fatura_sistemi.dataAccess.SqliteDatabase;
import com.utku.fatura_sistemi.entity.customer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.List;

public class HelloApplication extends Application {

    public static IDatabaseDal database;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("MainMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 450, 375);
        stage.setTitle("Ana Menü");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args){

        database = new SqliteDatabase();

        launch();
    }
}
package com.utku.invoice_upload_system;

import com.utku.invoice_upload_system.dataAccess.IDatabaseDal;
import com.utku.invoice_upload_system.dataAccess.SqliteDatabase;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {



    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("MainMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 450, 375);
        stage.setTitle("Ana Menü");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args){
        Statics statics = new Statics();
        launch();
    }
}
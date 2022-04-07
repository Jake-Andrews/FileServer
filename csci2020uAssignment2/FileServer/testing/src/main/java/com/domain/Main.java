package com.domain;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main extends Application {

    public static ArrayList<String> args1;

    @Override
    public void start(Stage primaryStage) throws IOException{
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("."));
        File mainDirectory = directoryChooser.showDialog(primaryStage);
        //mainDirectory contains the path to the folder you choose, absolute path
        String defaultDirectory = mainDirectory.getPath();
        //System.out.println(defaultDirectory);

        //Arraylist to hold the Path of the Directory Chosen
        args1 = new ArrayList<String>();
        args1.add(defaultDirectory);

        //setting scene, etc...
        Parent root = FXMLLoader.load(getClass().getResource("/FXML/sample.fxml"));
        primaryStage.setTitle("File Sharing Client");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
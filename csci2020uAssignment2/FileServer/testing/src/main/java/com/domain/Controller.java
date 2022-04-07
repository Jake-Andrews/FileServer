package com.domain;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.File;
import java.io.IOException;


public class Controller {
    /**
     * This class builds the client user interface with FXML.
     * Displays 4 buttons, and two lists. A client file list,
     * and a server file list. Download, upload, quit and 
     * delete buttons. 
     */
    @FXML private ListView<String> leftView;                                    //Listview for client files
    @FXML private ListView<String> rightView;                                   //Listview for server files

    @FXML private Button upload;
    @FXML private Button download;
    @FXML private Button quit;

    private FileServerClient client;                                            //Client object

    private ObservableList<String> fileList;                                    //list of client files
    private ObservableList<String> serverFileList;                              //list of server files

    @FXML
    public void initialize() {
        client = new FileServerClient();                                        //instantiate client

        setLeftView();                                                          //Fill fileList with strings

        leftView.setItems(fileList);                                            //Fill up the listView's
        serverFileList = (client.getServerFileList());
        rightView.setItems(serverFileList);
    }

    private void refresh(String fileName, String serverOrClient) {
        /**
         * This method updates the list after a command has been processed.
         * 
         * @param fileName String name of file being dealt with
         * @param serverOrClient String the list to be updated, server list or client list
         */
        if (serverOrClient.equalsIgnoreCase("Server")) {                        //update server list
            serverFileList.add(fileName);
        } else {fileList.add(fileName);}                                        //update client list
    }

    private void setLeftView() {
        /**
         * This method instantiates the fileList.
         */
        fileList = FXCollections.observableArrayList();

        String filePath  = Main.args1.get(0);                                   //read filepath from cmd line
        File file = new File(filePath);
        File [] files = file.listFiles();                                       //Store all client files in array

        for (int i = 0; i < files.length; i++) {                                //add filenames to fileList
            fileList.add(files[i].getName());
        }
    }

    @FXML
    private void quit(ActionEvent event) {
        /**
         * Quits program when quit button is clicked.
         * Closes socket connection and UI
         */
        client.closeSocket();
        client.closeServerSocket();
        Platform.exit();
    }


    @FXML
    private void download(ActionEvent event) throws IOException {
        /**
         * This method allows the user to download a file from the server when clicked.
         * Grabs selected file from serverlist, and sends the filename to client function
         * to contact the server.
         */
        client.openSocket();                                                    //open connection
        
        String chosenFileName = "";
                                                                                //grab selected file in list
        if (rightView.getSelectionModel().getSelectedIndex() != -1) {           //If it's -1, then nothing is selected in the server's list of files
            int index = rightView.getSelectionModel().getSelectedIndex();
            chosenFileName = serverFileList.get(index);

        } else {
            System.out.println("Error, you either selected an item on your shared folder");
            System.out.println("Or you have not chosen a file");
        }
        client.recieveFile(chosenFileName);                                     //call client function to talk to server
        //client.closeSocket();                                                 //close connection
        refresh(chosenFileName,"Client");                                       //update listview
    }   

    @FXML
    private void delete(ActionEvent event) throws IOException {
        /**
         * This method allows the user to delete a file in the client side.
         * Grabs selected file from fileList, and removes the filename from 
         * list and removes the file from the clients local folder.
         */
        
        String chosenFileName = "";
        boolean leftOrRight = true;
        int index = 0;

        if (leftView.getSelectionModel().getSelectedIndex() != -1) {            //If it's -1, then nothing is selected in the list of files
            index = leftView.getSelectionModel().getSelectedIndex();            //grabs the currently selected listView cell.
            chosenFileName = fileList.get(index);

        } else {
            index = rightView.getSelectionModel().getSelectedIndex();
            chosenFileName = fileList.get(index);
            leftOrRight = false;
        }

        if (leftOrRight) {                                                      //if selected file is in client list
            fileList.remove(index);                                             //remove from list
            String filePath = Main.args1.get(0);
            filePath = filePath + "/" + chosenFileName;

            File file = new File(filePath);                                     //delete file from folder
            file.delete();
        }
        else {
            System.out.println("Doesn't work on Server files!");
        }
    }

    //
    @FXML
    private void upload(ActionEvent event) throws IOException {
        /**
         * This method allows the user to upload a file to the server when clicked.
         * Grabs selected file from filelist, and sends the filename to client function
         * to contact the server.
         */
    
        client.openSocket();                                                    //Open connection

        String chosenFileName = "";
                                                                                //grabs the currently selected listView cell.
        if (leftView.getSelectionModel().getSelectedIndex() != -1) {            //If it's -1, then nothing is selected in the clients's list of files
            int index = leftView.getSelectionModel().getSelectedIndex();
            chosenFileName = fileList.get(index);

        } else {
            System.out.println("Error, you either selected an item on your shared folder");
            System.out.println("Or you have not chosen a file");
        }

        client.sendFileName(chosenFileName);                                    //call client function to talk to server
        //client.closeSocket();                                                 //close connection
        refresh(chosenFileName,"Server");                                       //update listview
    }
}
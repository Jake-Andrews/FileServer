package com.domain;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import java.io.*;
import java.io.IOException;
import java.net.*;
import java.util.*;
import java.awt.*;

public class FileServerClient extends Frame {
    /**
     * This class represents the client object, that connects
     * to the server socket and sends messages to the server.
     */

    private Socket socket = null;
    private BufferedReader in = null;
    private PrintWriter networkOut = null;
    private BufferedReader networkIn = null;

    private ObservableList<String> serverFileList;                                          //list of files in the server

    public static String SERVER_ADDRESS = "localhost";
    public static int SERVER_PORT = 16789;

    public ObservableList<String> getServerFileList() {
        return serverFileList;
    }

    public FileServerClient() {                                                             //constructor class, opens socket connections 
        openSocket();                                                                       //then retrieves files in server
        getFiles();
        //closeSocket();
    }

    protected void openSocket() {
        /**
         * This method initializes the socket connection between the client and the server.
         * Instantiates the local socket, as well as the in and out streams used to
         * communicate messages with the server.
         */

        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);                               //instantiate local socket
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + SERVER_ADDRESS);
        } catch (IOException e) {
            System.err.println("IOEXception while connecting to server: " + SERVER_ADDRESS);
        }
        if (socket == null) {
            System.err.println("socket is null");                                           //null error - most likely to happen if server is offline
        }
        try {
            networkOut = new PrintWriter(socket.getOutputStream(), true);                   //writes to serverthread

            networkIn = new BufferedReader(new InputStreamReader(socket.getInputStream())); //reads from serverthread
        } catch (IOException e) {
            System.err.println("IOEXception while opening a read/write connection");
        }
        in = new BufferedReader(new InputStreamReader(System.in));                          //reads from system
    }

    protected void getFiles() {
        /**
         * Retrieves a list of the filenames in the server.
         * Stores all the filenames in an observable arraylist
         */

        //The message should contain a string of files names with newline characters
        //ex: Something.txt\nnewfile.txt

        String fileList = null;
        networkOut.println("Filler" + " DIR");                                              //write message to server
        try {
            fileList = networkIn.readLine();                                                //read server response in as string
        } catch (IOException e) {
            System.err.println("Error reading initial greeting from socket.");
        }

                                                                                            //Now time to parse the String. filename.txt,name.txt,etc..
        assert fileList != null;
        ArrayList<String> temp = new ArrayList<>(Arrays.asList(fileList.split(",")));       //breaks csv string into an arraylist
        serverFileList = FXCollections.observableArrayList();

        for (int i = 0; i < temp.size(); i++) {                                             //add each filename to the list
            serverFileList.add(temp.get(i));
        }
    }

    protected void recieveFile(String fileName) throws IOException {
        /**
         * Download a file from the server.
         * Parses the file and writes contents and filename
         * to client socket.
         * 
         * @param filename String name of the file to be downloaded
         */

        networkOut.println(fileName + " DOWNLOAD");                                         //write message to server socket

        String pathey = System.getProperty("user.dir");                                     //build client file path
        pathey = pathey + "/src/main/resources/TextFiles/" + fileName;
 
        File file = new File(pathey);                                                       //create a new file locally
        file.createNewFile();

        String message = networkIn.readLine();                                              //read server reply
        FileOutputStream fos = new FileOutputStream(pathey);                                //writes to file
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        byte[] buffer = new byte[1024];

        InputStream into = socket.getInputStream();                                         //reads from server socket
        int count;
        while ((count = into.read(buffer)) >=  0){                                          //while bytes are being read in
            fos.write(buffer,0,count);                                                      //write bytes to file
        }                                                                   

        fos.close();
        socket.close();
        System.out.println(fileName + " was downloaded from the server sucessfully!");
    }

    protected void sendFileName(String fileName) throws IOException {
        /**
         * Uploads a file to the server.
         * Parses the file and writes contents and filename
         * to server socket.
         * 
         * @param filename String name of the file to be uploaded
         */

        networkOut.println(fileName + " UPLOAD");                                           //write message for serverthread

        String pathey = System.getProperty("user.dir");                                     //build client file path
        pathey = pathey + "/src/main/resources/TextFiles/" + fileName;
        File file = new File(pathey);

        OutputStream os = socket.getOutputStream();                                         //writes to server socket
        BufferedInputStream into = new BufferedInputStream(new FileInputStream(file));      //reads from file
        byte[] buffer = new byte[1024];

        int count;
        while ((count = into.read(buffer)) > 0) {                                           //while reading in bytes
            os.write(buffer, 0, count);                                                     //write to server socket
            os.flush();
        }

        try(BufferedReader br = new BufferedReader(new FileReader(file))) {                 //read through file until nothing else to read
            for(String line; (line = br.readLine()) != null; ) {
                networkOut.println(line);                                                   //write to server socket
            }
        }
    }

    protected void closeServerSocket() {
        /**
         * This method closes the server socket connection
         * by sending the cue message to the socket
         */
        networkOut.println("QUIT");
    }

    protected void closeSocket() {
        /**
         * This method closes the client socket
         */
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


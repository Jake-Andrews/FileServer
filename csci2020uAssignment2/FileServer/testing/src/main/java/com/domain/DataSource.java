package com.domain;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;

public class DataSource {

    public static File currentFileName;
    public static ObservableList<FileInformation> listOfFiles = FXCollections.observableArrayList();

    public static void setCurrentFileName(File file) {
        currentFileName = file;
    }

    //should grab the information from the file, and populate the table.
    /*
    public static void load() {
        readFile(currentFileName);
    }

     */

    public static void addRecord(FileInformation newFile) {
        listOfFiles.add(newFile);
    }

    /*
    public static void save() throws IOException{
        System.out.println("Saving to:" + currentFileName.toString());

        try {
            if (!currentFileName.exists()) {
                currentFileName.createNewFile();
            }

            if (currentFileName.canWrite()) {
                PrintWriter fileOutput = new PrintWriter(currentFileName);

                Iterator<StudentRecord> iterator = studentRecords.iterator();
                while (iterator.hasNext()) {
                    StudentRecord temp = (StudentRecord) iterator.next();
                    fileOutput.print(temp.getStudentID() + ",");
                    fileOutput.print(temp.getAssignmentMark() + ",");
                    fileOutput.print(temp.getMidtermMark() + ",");
                    if (iterator.hasNext()) {
                        fileOutput.println(temp.getFinalExamMark());
                    } else {fileOutput.print(temp.getFinalExamMark());}
                }
                fileOutput.close();
            }
        } catch(Exception e) {System.err.println(e);}
        //}else{
        //		System.out.println("Error: the output file already exists: " + output.getAbsolutePath());
        //	}
    }

     */

    public static ObservableList<FileInformation> getListOfFiles() {
        return listOfFiles;
    }

    public static void wipe() {
        listOfFiles.clear();
        currentFileName = null;
    }
    /*
    public static void readFile(File file) {
        //file manipulation
        try{
            FileReader fileInput = new FileReader(file);
            BufferedReader input = new BufferedReader(fileInput);

            int count = 0;
            String line;
            while((line = input.readLine()) != null){
                String[] data = line.split(",");
                String studentID = data[0];
                float assignmentMark = Float.valueOf(data[1]);
                float midtermMark = Float.valueOf(data[2]);
                float examMark = Float.valueOf(data[3]);

                studentRecords.add(new StudentRecord(studentID, assignmentMark, midtermMark, examMark));
            }

            input.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

     */
}
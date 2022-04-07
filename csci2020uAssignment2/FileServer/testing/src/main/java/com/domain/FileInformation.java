package com.domain;

public class FileInformation {
    public String fileName;

    public FileInformation (String filename) {
        this.fileName = "idk";
    }

    public FileInformation() {
        this.fileName = "N/A";
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
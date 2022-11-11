package org.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class Header {

    List<String> headerList = new ArrayList<>();
    public void extractHeader(String header) {
        long fileVersion;
        String headerTag;
        LocalDateTime fileCreatedOn;
        String fileComment = "";

        System.out.println("---------------HEADER------------------------");

        //Store header tag
        headerTag = header.substring(0,5);
        System.out.println("Tag is : "+headerTag);

        //Store file version
        try {
            fileVersion = Long.parseLong(header.substring(5,9));
            if (fileVersion==4 || fileVersion==5) {
                System.out.println("File version is: " + fileVersion);
            } else {
                System.out.println("A fatal error occurred in Header!! Unsupported file version : "+fileVersion+". Supports only version 4 or 5!");
                throw new RuntimeException();
            }
        } catch (NumberFormatException e) {
            System.out.println("A fatal error occurred in Header!! File version - Only numbers allowed!");
            throw new RuntimeException(e);
        }

        //Store file creation date and time
        String tmpDateTime = header.substring(9,26);
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
            fileCreatedOn = LocalDateTime.parse(tmpDateTime, formatter);
            if (fileCreatedOn.isAfter(LocalDateTime.now())) {
                System.out.println("A fatal error occurred in Header!! Future file creation date is not allowed!");
                throw new RuntimeException();
            } else {
                System.out.println("File created on: "+fileCreatedOn);
            }
        } catch(DateTimeParseException e) {
            System.out.println("A fatal error occurred in Header!! Date and Time - Only numbers allowed!");
            throw new RuntimeException(e);
        }

        //Store file comment
        if (fileVersion==5) {
            fileComment = header.substring(header.indexOf("}") + 1).trim();
            System.out.println("File comment is: " + fileComment);
        }

        headerList.add(headerTag);
        headerList.add(String.valueOf(fileVersion));
        headerList.add(String.valueOf(fileCreatedOn));
        headerList.add(fileComment);
    }

    //Passing the header arraylist to write it on the CSV
    public void writeHeaderCSV() {
        CSVWriter csvWriter = new CSVWriter();
        csvWriter.createHeaderCSVFile(headerList);
    }
}

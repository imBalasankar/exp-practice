package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {
    public static long fileVersion;

    public static void main(String[] args) {

        try {
            File myObj = new File("SAMPLE_v3.dat");
            Scanner myReader = new Scanner(myObj);
            String header = myReader.nextLine();

            if(header.substring(0,5).contains("HEADR")){
                extractHeader(header);
            } else {
                System.out.println("Header is Mandatory!!");
                return;
            }

            while(myReader.hasNextLine()){
                String currentLine = myReader.nextLine();
                String tag = currentLine.substring(0,5);
                if(tag.contains("TRADE")){
                    System.out.println("This is a Trade structure");
                } else if (tag.contains("EXTRD")) {
                    System.out.println("This is a Extended Trade structure");
                } else if (tag.contains("FOOTR")) {
                    extractFooter(currentLine);
                    return;
                } else if (!myReader.hasNextLine()){
                    System.out.println("Footer is Mandatory!!");
                    return;
                }else {
                    System.out.println("This structure is not recognized: "+tag);
                }
            }

            myReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void extractHeader(String header){
        String headerTag;
        LocalDateTime fileCreatedOn;
        String fileComment;

        headerTag = header.substring(0,5);
        System.out.println("Tag is : "+headerTag);

        fileVersion = Long.parseLong(header.substring(5,9));
        if(fileVersion==4 || fileVersion==5) {
            System.out.println("File version is: " + fileVersion);
        }else{
            System.out.println("Unsupported file version!! " + fileVersion);
        }

        String tmpDateTime = header.substring(9,26);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        fileCreatedOn = LocalDateTime.parse(tmpDateTime, formatter);
        System.out.println("File created on: "+fileCreatedOn);

        if(fileVersion==5) {
            fileComment = header.substring(header.indexOf("}") + 1);
            System.out.println("File comment is: " + fileComment);
        }
    }

    public static void extractFooter(String footer){
        String footerTag;
        long noOfStructures;
        long noOfCharsInStructures;

        footerTag = footer.substring(0,5);
        System.out.println("Tag is : "+footerTag);

        noOfStructures = Long.parseLong(footer.substring(5,15));
        System.out.println("Number of TRADE and EXTRD structures is: "+noOfStructures);

        if(fileVersion==5) {
            noOfCharsInStructures = Long.parseLong(footer.substring(15, 25));
            System.out.println("Number of characters in TRADE and EXTRD structures is: " + noOfCharsInStructures);
        }
    }

}
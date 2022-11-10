package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {
    public static long fileVersion;

    public static void main(String[] args) {
        Trade trade = new Trade();

        try {
            File myObj = new File("SAMPLE_v3.dat");
            Scanner myReader = new Scanner(myObj);
            String header = myReader.nextLine();

            if (header.substring(0,5).contains("HEADR")) {
                extractHeader(header);
            } else {
                System.out.println("Header is Mandatory!!");
                throw new RuntimeException();
            }

            while (myReader.hasNextLine()) {
                String currentLine = myReader.nextLine();
                String tag = currentLine.substring(0,5);
                if (tag.contains("TRADE")) {
                    trade.extractTrade(currentLine);
                } else if (tag.contains("EXTRD")) {
                    System.out.println("This is a Extended Trade structure");
                } else if (tag.contains("FOOTR")) {
                    extractFooter(currentLine);
                    return;
                } else if (!myReader.hasNextLine()) {
                    System.out.println("Footer is Mandatory!!");
                    throw new RuntimeException();
                } else {
                    System.out.println("This structure is not recognized: "+tag);
                }
            }

            myReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred : File not found!!");
            e.printStackTrace();
        }
    }

    public static void extractHeader(String header) {
        String headerTag;
        LocalDateTime fileCreatedOn;
        String fileComment;

        headerTag = header.substring(0,5);
        System.out.println("Tag is : "+headerTag);

        try {
            fileVersion = Long.parseLong(header.substring(5,9));
        } catch (Exception e) {
            System.out.println("An error occurred : File version - Only numbers allowed!!");
            throw new NumberFormatException();
        }

        if (fileVersion==4 || fileVersion==5) {
            System.out.println("File version is: " + fileVersion);
        } else {
            System.out.println("Unsupported file version: "+fileVersion+"!! Supports only version 4 or 5!");
            throw new RuntimeException();
        }

        String tmpDateTime = header.substring(9,26);

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
            fileCreatedOn = LocalDateTime.parse(tmpDateTime, formatter);
        } catch(Exception e) {
            System.out.println("An error occurred : Date and Time - Only numbers allowed!!");
            e.printStackTrace();
            throw new RuntimeException();
        }

        if (fileCreatedOn.isAfter(LocalDateTime.now())) {
            System.out.println("An error occurred : Future file creation date is not allowed!!");
            //throw new RuntimeException();
        } else {
            System.out.println("File created on: "+fileCreatedOn);
        }

        if (fileVersion==5) {
            fileComment = header.substring(header.indexOf("}") + 1).trim().replaceAll("^/+","").replaceAll("\\+$","");
            System.out.println("File comment is: " + fileComment);
        }
    }

    public static void extractFooter(String footer) {
        String footerTag;
        long noOfStructures;
        long noOfCharsInStructures;

        footerTag = footer.substring(0,5);
        System.out.println("Tag is : "+footerTag);

        try {
            noOfStructures = Long.parseLong(footer.substring(5, 15));
            System.out.println("Number of TRADE and EXTRD structures is: "+noOfStructures);
        } catch (Exception e) {
            System.out.println("An error occurred : No of structures - Only numbers allowed!!");
            e.printStackTrace();
            throw new RuntimeException();
        }

        if (fileVersion==5) {
            try {
                noOfCharsInStructures = Long.parseLong(footer.substring(15, 25));
                System.out.println("Number of characters in TRADE and EXTRD structures is: " + noOfCharsInStructures);
            } catch (Exception e) {
                System.out.println("An error occurred : No of characters in structures - Only numbers allowed!!");
                e.printStackTrace();
                throw new RuntimeException();
            }
        }
    }

}
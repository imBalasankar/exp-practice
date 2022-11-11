package org.example;

import java.io.File;
import java.util.Scanner;

public class Main {
    public static long fileVersion;

    public static void main(String[] args) {
        Header header = new Header();
        ExtendedTrade extendedTrade = new ExtendedTrade();
        Footer footer = new Footer();
        long lineNumber = 0;

        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Input filename: ");
            String fileName = scanner.nextLine();
            File myObj = new File(fileName);
            Scanner myReader = new Scanner(myObj);

            //Read the first line and passing it to the extract header function
            String headerLine = myReader.nextLine();
            lineNumber++;
            if (headerLine.substring(0,5).contains("HEADR")) {
                header.extractHeader(headerLine);
                header.writeHeaderCSV();
                fileVersion = Long.parseLong(headerLine.substring(5,9));
            } else {
                System.out.println("A fatal error occurred!! Header is Mandatory!");
                throw new RuntimeException();
            }

            while (myReader.hasNextLine()) {
                String currentLine = myReader.nextLine();
                lineNumber++;
                String tag = currentLine.substring(0,5);
                if (tag.contains("TRADE") || tag.contains("EXTRD")) {
                    extendedTrade.passTrade(tag, currentLine, lineNumber);
                } else if (tag.contains("FOOTR")) {
                    footer.extractFooter(currentLine, fileVersion);
                    extendedTrade.writeCSV();
                    footer.writeFooterCSV();
                    return;
                }  else {
                    System.out.println("A fatal error occurred!! This structure is not recognized: "+tag);
                    throw new RuntimeException();
                }

                //Stop the application if the footer is missing
                if (!myReader.hasNextLine()) {
                    System.out.println("A fatal error occurred!! Footer is Mandatory!");
                    throw new RuntimeException();
                }
            }
            myReader.close();
        } catch (Exception e) {
            System.out.println("A fatal error occurred!! Check either file name is correct or file is not blank!");
            throw new RuntimeException(e);
        }
    }

}
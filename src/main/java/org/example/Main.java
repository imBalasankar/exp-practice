package org.example;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static long fileVersion;

    public static void main(String[] args) {
        Header header = new Header();
        ExtendedTrade extendedTrade = new ExtendedTrade();
        Footer footer = new Footer();
        long lineNumber = 0;

        try {
            File myObj = new File("sample.txt");
            Scanner myReader = new Scanner(myObj);

            //Read the first line and passing it to the extract header function
            String headerLine = myReader.nextLine();
            lineNumber++;
            if (headerLine.substring(0,5).contains("HEADR")) {
                header.extractHeader(headerLine);
                header.writeHeaderCSV();
                fileVersion = Long.parseLong(headerLine.substring(5,9));
            } else {
                System.out.println("Header is Mandatory!!");
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
                    System.out.println("This structure is not recognized: "+tag);
                }

                //Stop the application if the footer is missing
                if (!myReader.hasNextLine()) {
                    System.out.println("Footer is Mandatory!!");
                    throw new RuntimeException();
                }
            }
            myReader.close();
        } catch (IOException e) {
            System.out.println("An error occurred : File not found!!");
            throw new RuntimeException(e);
        }
    }

}
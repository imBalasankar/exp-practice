package org.example;

import java.util.ArrayList;
import java.util.List;

public class Footer {
    List<String> footerList = new ArrayList<>();
    public void extractFooter(String footer, long fileVersion) {
        String footerTag;
        long noOfStructures;
        long noOfCharsInStructures=0;

        System.out.println("------------------FOOTER----------------------");

        //Store footer tag
        footerTag = footer.substring(0,5);
        System.out.println("Tag is : "+footerTag);

        //Store no of trade and extended trade structures
        try {
            noOfStructures = Long.parseLong(footer.substring(5, 15));
            System.out.println("Number of TRADE and EXTRD structures is: "+noOfStructures);
        } catch (Exception e) {
            System.out.println("A fatal error occurred in Footer!! No of structures - Only numbers allowed!");
            throw new RuntimeException(e);
        }

        //Store no of characters in trade and extended trade structures
        if (fileVersion==5) {
            try {
                noOfCharsInStructures = Long.parseLong(footer.substring(15, 25));
                System.out.println("Number of characters in TRADE and EXTRD structures is: " + noOfCharsInStructures);
            } catch (Exception e) {
                System.out.println("A fatal error occurred in Footer!! No of characters in structures - Only numbers allowed!");
                throw new RuntimeException(e);
            }
        }

        footerList.add(footerTag);
        footerList.add(String.valueOf(noOfStructures));
        if (noOfCharsInStructures != 0) {
            footerList.add(String.valueOf(noOfCharsInStructures));
        }

    }

    //Passing the footer arraylist to write it on the CSV
    public void writeFooterCSV() {
        CSVWriter csvWriter = new CSVWriter();
        csvWriter.createFooterCSVFile(footerList);
    }
}

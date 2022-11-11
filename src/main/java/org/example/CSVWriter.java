package org.example;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVWriter {
    //Writing the Trade CSV
    public void createCSVFile(ArrayList<ExtendedTrade> etrd) {
        String version;

        try {
            FileWriter out = new FileWriter("trade.csv");
            CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.builder().setHeader("Tag", "Version", "Trade date and time", "Direction", "Item id", "Price", "Quantity", "Buyer", "Seller", "Trade comment", "Nested tag").build());

          for ( ExtendedTrade etr : etrd) {
              if (etr.getVersion()==0) {
                  version = "";
              } else {
                  version = String.valueOf(etr.getVersion());
              }
              printer.printRecord(
                      etr.getTradeTag(), version, etr.getTradeDateTime(), etr.getDirection(), etr.getItemId(), etr.getPrice(), etr.getQuantity(), etr.getBuyer(), etr.getSeller(), etr.getTradeComment(), etr.getNestedTag()
              );
          }
            printer.flush();
            out.close();
        } catch (IOException e){
            System.out.println("A fatal error occurred while creating the CSV file!!");
            throw new RuntimeException(e);
        }
    }

    //Writing the Header CSV
    public void createHeaderCSVFile(List<String> headerList) {

        try {
            FileWriter out = new FileWriter("header.csv");
            CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.builder().setHeader("Tag", "File Version", "File created date and time", "File comment").build());
            printer.printRecord(headerList);
            printer.flush();
            out.close();
        } catch (IOException e){
            System.out.println("A fatal error occurred while creating the CSV file!!");
            throw new RuntimeException(e);
        }
    }

    //Writing the Footer CSV
    public void createFooterCSVFile(List<String> footerList) {

        try {
            FileWriter out = new FileWriter("footer.csv");
            CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.builder().setHeader("Tag", "Number of TRADE and EXTRD structures", "Number of characters in TRADE and EXTRD structures").build());
            printer.printRecord(footerList);
            printer.flush();
            out.close();
        } catch (IOException e){
            System.out.println("A fatal error occurred while creating the CSV file!!");
            throw new RuntimeException(e);
        }
    }
}

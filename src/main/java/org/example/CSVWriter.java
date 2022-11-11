package org.example;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVWriter {
    public void createCSVFile(ArrayList<ExtendedTrade> etrd) {
        String version;

        try {
            FileWriter out = new FileWriter("trade.csv");
            CSVPrinter printer = new CSVPrinter(out, CSVFormat.Builder.create().setHeader("Tag", "Version", "Trade date and time", "Direction", "Item id", "Price", "Quantity", "Buyer", "Seller", "Trade comment", "Nested tag").build());

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
            System.out.println("Error occurred while creating CSV file!!");
            throw new RuntimeException(e);
        }
    }

    public void createHeaderCSVFile(List<String> headerList) {

        try {
            FileWriter out = new FileWriter("header.csv");
            CSVPrinter printer = new CSVPrinter(out, CSVFormat.Builder.create().setHeader("Tag", "File Version", "File created date and time", "File comment").build());
            printer.printRecord(headerList);
            printer.flush();
            out.close();
        } catch (IOException e){
            System.out.println("Error occurred while creating CSV file!!");
            throw new RuntimeException(e);
        }
    }
}

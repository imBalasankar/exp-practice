package org.example;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;

public class CSVWriter {
    public void createCSVFile(ExtendedTrade etr) throws IOException {
//        String version;
//        String nestedTag;
//        if (etr.getVersion()==0) {
//            version = "";
//        } else {
//            version = String.valueOf(etr.getVersion());
//        }
//
//        if (etr.getNestedTag()==null) {
//            nestedTag = "";
//        } else {
//            nestedTag = etr.getNestedTag();
//        }

        FileWriter out = new FileWriter("trade.csv");
        try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.Builder.create().setHeader("Tag", "Version", "Trade date and time", "Direction", "Item id", "Price", "Quantity", "Buyer", "Seller", "Trade comment", "Nested tag").build())) {
          printer.printRecord(
                etr.getTradeTag(), etr.getVersion(), etr.getTradeDateTime(), etr.getDirection(), etr.getItemId(), etr.getPrice(), etr.getQuantity(), etr.getBuyer(), etr.getSeller(), etr.getTradeComment(), etr.getNestedTag()
          );
        }
    }
}

package org.example;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;

public class CSVWriter {
    public void createCSVFile(Trade tr) throws IOException {
        FileWriter out = new FileWriter("trade.csv");
        try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.Builder.create().setHeader("Tag", "Version", "Trade date and time", "Direction", "Item id", "Price", "Quantity", "Buyer", "Seller", "Trade comment", "Nested tag").build());) {
          printer.printRecord();
        }
    }
}

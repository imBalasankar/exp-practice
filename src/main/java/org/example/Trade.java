package org.example;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Trade {
    protected long id;
    protected BigDecimal volume;
    protected String tradeTag;
    protected LocalDateTime tradeDateTime;
    protected String direction;
    protected String itemId;
    protected BigDecimal price;
    protected long quantity;
    protected String buyer;
    protected String seller;
    protected String tradeComment;

    public Trade() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public String getTradeTag() {
        return tradeTag;
    }

    public void setTradeTag(String tradeTag) {
        this.tradeTag = tradeTag;
    }

    public LocalDateTime getTradeDateTime() {
        return tradeDateTime;
    }

    public void setTradeDateTime(LocalDateTime tradeDateTime) {
        this.tradeDateTime = tradeDateTime;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getTradeComment() {
        return tradeComment;
    }

    public void setTradeComment(String tradeComment) {
        this.tradeComment = tradeComment;
    }
}

class ExtendedTrade extends Trade{
    private long version;
    private String nestedTag ="";

    public ExtendedTrade() {
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public String getNestedTag() {
        return nestedTag;
    }

    long tmpId = 0;
    ArrayList<ExtendedTrade> tradeList = new ArrayList<>();

    public void setNestedTag(String nestedTag) {
        this.nestedTag = nestedTag;
    }

    public void passTrade(String tag, String trade, long lineNo) {
        if(tag.contentEquals("TRADE")){
            extractTrade(trade, lineNo);
        }else{
            extractExtendedTrade(trade, lineNo);
        }
    }

    public void extractTrade(String trade, long lineNo) {
        ExtendedTrade tr = new ExtendedTrade();

        System.out.println("------------------TRADE----------------------");

        //Set trade tag
        tr.setTradeTag(trade.substring(0,5));
        System.out.println("Tag is : "+tr.getTradeTag());

        //Set trade date and time
        String tmpDateTime = trade.substring(5,22);
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
            tradeDateTime = LocalDateTime.parse(tmpDateTime, formatter);
            if (tradeDateTime.isAfter(LocalDateTime.now())) {
                System.out.println("An error occurred in line number "+lineNo+" : Future file creation date is not allowed!! Skipping this line...");
                return;
            } else {
                tr.setTradeDateTime(tradeDateTime);
                System.out.println("Trade date and time is : "+tr.getTradeDateTime());
            }
        } catch(DateTimeParseException e) {
            System.out.println("An error occurred in line number "+lineNo+" : Date and Time - Only numbers allowed!! Skipping this line...");
            return;
        }

        //Set trade direction
        direction = trade.substring(22,23);
        if (direction.contentEquals("B") || direction.contentEquals("S")) {
            tr.setDirection(direction);
            System.out.println("Direction is : "+tr.getDirection());
        } else {
            System.out.println("An error occurred in line number "+lineNo+" : Direction should be B or S!! Skipping this line...");
            return;
        }

        //Set trade item id
        itemId = trade.substring(23, 35);
        Pattern itemIdLeadPattern = Pattern.compile("[A-Z]+");
        Matcher itemIdLeadMatcher = itemIdLeadPattern.matcher(itemId.substring(0,3));

        Pattern itemIdTrailPattern = Pattern.compile("[A-Z0-9]+");
        Matcher itemIdTrailMatcher = itemIdTrailPattern.matcher(itemId.substring(3));
        if (itemIdLeadMatcher.matches()) {
            if (itemIdTrailMatcher.matches()) {
                tr.setItemId(itemId);
                System.out.println("Item id is : "+tr.getItemId());
            } else {
                System.out.println("An error occurred in line number "+lineNo+" : Last 9 characters of the Item id should be upper case letters or numbers!! Skipping this line...");
                return;
            }
        } else {
            System.out.println("An error occurred in line number "+lineNo+" : First 3 characters of the Item id should be upper case letters!! Skipping this line...");
            return;
        }

        //Set trade price
        String tmpPrice = "";
        tmpPrice = tmpPrice.concat(trade.substring(35,46) +"."+trade.substring(46,50));
        try {
            if (tmpPrice.substring(0, 1).contentEquals("+")) {
                price = new BigDecimal(tmpPrice.substring(1));
            } else  {
                price = new BigDecimal(tmpPrice);
            }
            tr.setPrice(price);
            System.out.println("Price is: " + tr.getPrice());
        } catch (Exception e) {
            System.out.println("An error occurred in line number "+lineNo+" : Price should only have numbers!! Skipping this line...");
            return;
        }

        //Set trade quantity
        try {
            quantity = Long.parseLong(trade.substring(50,61));
            if(quantity<=0){
                System.out.println("An error occurred in line number "+lineNo+" : Quantity never be zero or negative!! Skipping this line...");
                return;
            } else {
                tr.setQuantity(quantity);
                System.out.println("Quantity is: " + tr.getQuantity());
            }
        } catch (Exception e) {
            System.out.println("An error occurred in line number "+lineNo+" : Quantity should only have numbers!! Skipping this line...");
            return;
        }

        //Set trade volume
        volume = price.multiply(BigDecimal.valueOf(quantity));
        tr.setVolume(volume);
        System.out.println("Trade volume is: " + tr.getVolume());

        //Set trade buyer
        buyer = trade.substring(61,65);
        Pattern buyerPattern = Pattern.compile("[A-Za-z0-9_]+");
        Matcher buyerMatcher = buyerPattern.matcher(buyer);
        if (buyerMatcher.matches()) {
            tr.setBuyer(buyer);
            System.out.println("Buyer is : "+tr.getBuyer());
        } else {
            System.out.println("An error occurred in line number "+lineNo+" : Buyer id should contain letters, numbers, and underscore only!! Skipping this line...");
            return;
        }

        //Set trade seller
        seller = trade.substring(65,69);
        Pattern sellerPattern = Pattern.compile("[A-Za-z0-9_]+");
        Matcher sellerMatcher = sellerPattern.matcher(seller);
        if (sellerMatcher.matches()) {
            tr.setSeller(seller);
            System.out.println("Seller is : "+tr.getSeller());
        } else {
            System.out.println("An error occurred in line number "+lineNo+" : Seller id should contain letters, numbers, and underscore only!! Skipping this line...");
            return;
        }

        //Set trade comment
        tradeComment = trade.substring(69).trim();
        tr.setTradeComment(tradeComment);
        System.out.println("Trade comment is: "+tr.getTradeComment());

        //Set trade id if everything is successfully set
        id = ++tmpId;
        tr.setId(id);
        System.out.println("Trade id is: "+tr.getId());

        //Add trade to the list
        tradeList.add(tr);

    }

    public void extractExtendedTrade(String trade, long lineNo){
        ExtendedTrade extr = new ExtendedTrade();

        System.out.println("-----------EXTENDED TRADE------------------");

        //Set extended trade tag
        extr.setTradeTag(trade.substring(0,5));
        System.out.println("Tag is : "+ extr.getTradeTag());

        //Set extended trade version
        try {
            version = Long.parseLong(trade.substring(5, 9));
            if (version==1) {
                extr.setVersion(version);
                System.out.println("Version is: " + extr.getVersion());
            } else {
                System.out.println("An error occurred in line number "+lineNo+" : Unsupported version - Supports only version 1!! Skipping this line...");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("An error occurred in line number "+lineNo+" :  Version - Only numbers allowed!! Skipping this line...");
            return;
        }

        //Set extended trade date and time
        String tmpDateTime = trade.substring(9,26);
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
            tradeDateTime = LocalDateTime.parse(tmpDateTime, formatter);
            if (tradeDateTime.isAfter(LocalDateTime.now())) {
                System.out.println("An error occurred in line number "+lineNo+" : Future file creation date is not allowed!! Skipping this line...");
                return;
            } else {
                extr.setTradeDateTime(tradeDateTime);
                System.out.println("Trade date and time is : "+ extr.getTradeDateTime());
            }
        } catch(DateTimeParseException e) {
            System.out.println("An error occurred in line number "+lineNo+" : Date and Time - Only numbers allowed!! Skipping this line...");
            return;
        }

        //Set extended trade direction
        direction = trade.substring(26,30);
        if (direction.contentEquals("BUY_") || direction.contentEquals("SELL")) {
            extr.setDirection(direction);
            System.out.println("Direction is : "+ extr.getDirection());
        } else {
            System.out.println("An error occurred in line number "+lineNo+" : Direction should be BUY_ or SELL!! Skipping this line...");
            return;
        }

        //Set extended trade item id
        itemId = trade.substring(30, 42);
        Pattern itemIdLeadPattern = Pattern.compile("[A-Z]+");
        Matcher itemIdLeadMatcher = itemIdLeadPattern.matcher(itemId.substring(0,3));

        Pattern itemIdTrailPattern = Pattern.compile("[A-Z0-9]+");
        Matcher itemIdTrailMatcher = itemIdTrailPattern.matcher(itemId.substring(3));
        if (itemIdLeadMatcher.matches()) {
            if (itemIdTrailMatcher.matches()) {
                extr.setItemId(itemId);
                System.out.println("Item id is : "+ extr.getItemId());
            } else {
                System.out.println("An error occurred in line number "+lineNo+" : Last 9 characters of the Item id should be upper case letters or numbers!! Skipping this line...");
                return;
            }
        } else {
            System.out.println("An error occurred in line number "+lineNo+" : First 3 characters of the Item id should be upper case letters!! Skipping this line...");
            return;
        }

        //Set extended trade price
        String tmpPrice = "";
        tmpPrice = tmpPrice.concat(trade.substring(42,53) +"."+trade.substring(53,57));
        try {
            if (tmpPrice.substring(0, 1).contentEquals("+")) {
                price = new BigDecimal(tmpPrice.substring(1));
            } else  {
                price = new BigDecimal(tmpPrice);
            }
            extr.setPrice(price);
            System.out.println("Price is: " + price);
        } catch (Exception e) {
            System.out.println("An error occurred in line number "+lineNo+" : Price should only have numbers!! Skipping this line...");
            return;
        }

        //Set extended trade quantity
        try {
            quantity = Long.parseLong(trade.substring(57,68));
            if(quantity<=0){
                System.out.println("An error occurred in line number "+lineNo+" : Quantity never be zero or negative!! Skipping this line...");
                return;
            } else {
                extr.setQuantity(quantity);
                System.out.println("Quantity is: " + quantity);
            }
        } catch (Exception e) {
            System.out.println("An error occurred in line number "+lineNo+" : Quantity should only have numbers!! Skipping this line...");
            return;
        }

        //Set extended trade volume
        volume = price.multiply(BigDecimal.valueOf(quantity));
        extr.setVolume(volume);
        System.out.println("Trade volume is: " + extr.getVolume());

        //Set extended trade buyer
        buyer = trade.substring(68,72);
        Pattern buyerPattern = Pattern.compile("[A-Za-z0-9_]+");
        Matcher buyerMatcher = buyerPattern.matcher(buyer);
        if (buyerMatcher.matches()) {
            extr.setBuyer(buyer);
            System.out.println("Buyer is : "+ extr.getBuyer());
        } else {
            System.out.println("An error occurred in line number "+lineNo+" : Buyer id should contain letters, numbers, and underscore only!! Skipping this line...");
            return;
        }

        //Set extended trade seller
        seller = trade.substring(72,76);
        Pattern sellerPattern = Pattern.compile("[A-Za-z0-9_]+");
        Matcher sellerMatcher = sellerPattern.matcher(seller);
        if (sellerMatcher.matches()) {
            extr.setSeller(seller);
            System.out.println("Seller is : "+ extr.getSeller());
        } else {
            System.out.println("An error occurred in line number "+lineNo+" : Seller id should contain letters, numbers, and underscore only!! Skipping this line...");
            return;
        }

        //Set extended trade nested tag
        nestedTag = trade.substring(76).trim();
        extr.setNestedTag(nestedTag);
        System.out.println("Nested tag is: "+ extr.getNestedTag());

        //Set extended trade id if everything is successfully set
        id = ++tmpId;
        extr.setId(id);
        System.out.println("Trade id is: "+extr.getId());

        //Add extended trade to the list
        tradeList.add(extr);
    }

    public void writeCSV() {
        CSVWriter csvWriter = new CSVWriter();
        csvWriter.createCSVFile(tradeList);
    }
}

package org.example;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Trade {
    private String tradeTag;
    private LocalDateTime tradeDateTime;
    private String direction;
    private String itemId;
    private BigDecimal price;
    private long quantity;
    private String buyer;
    private String seller;
    private String tradeComment;

    public Trade() {
    }

    public Trade(String tradeTag, LocalDateTime tradeDateTime, String direction, String itemId, BigDecimal price, long quantity, String buyer, String seller, String tradeComment) {
        this.tradeTag = tradeTag;
        this.tradeDateTime = tradeDateTime;
        this.direction = direction;
        this.itemId = itemId;
        this.price = price;
        this.quantity = quantity;
        this.buyer = buyer;
        this.seller = seller;
        this.tradeComment = tradeComment;
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

    public void extractTrade(String trade){
        Trade tr = new Trade();
        //System.out.println(trade);

        System.out.println("------------------TRADE----------------------");

        tr.setTradeTag(trade.substring(0,5));
        System.out.println("Tag is : "+tr.getTradeTag());

        String tmpDateTime = trade.substring(5,22);

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
            tradeDateTime = LocalDateTime.parse(tmpDateTime, formatter);
        } catch(Exception e) {
            System.out.println("An error occurred : Date and Time - Only numbers allowed!! Moving to the next line...");
            e.printStackTrace();
            return;
        }

        if (tradeDateTime.isAfter(LocalDateTime.now())) {
            System.out.println("An error occurred : Future file creation date is not allowed!! Moving to the next line...");
            return;
        } else {
            tr.setTradeDateTime(tradeDateTime);
            System.out.println("Trade date and time is : "+tr.getTradeDateTime());
        }

        direction = trade.substring(22,23);
        if (direction.contentEquals("B") || direction.contentEquals("S")) {
            tr.setDirection(direction);
            System.out.println("Direction is : "+tr.getDirection());
        } else {
            System.out.println("Direction should be B or S!! Moving to the next line...");
            return;
        }

        itemId = trade.substring(23, 35);
        Pattern pattern = Pattern.compile("[A-Z]+");
        Matcher matcher = pattern.matcher(itemId.substring(0,3));

        Pattern pattern2 = Pattern.compile("[A-Z0-9]+");
        Matcher matcher2 = pattern2.matcher(itemId.substring(3));
        if (matcher.matches()) {
            if (matcher2.matches()) {
                tr.setItemId(itemId);
                System.out.println("Item id is : "+tr.getItemId());
            } else {
                System.out.println("Last 9 characters of the Item id is should be upper case letters or numbers!! Moving to the next line...");
                return;
            }
        } else {
            System.out.println("First 3 characters of the Item id is should be upper case letters!! Moving to the next line...");
            return;
        }

        String tmpPrice = "";
        tmpPrice = tmpPrice.concat(trade.substring(35,46) +"."+trade.substring(46,50));
        try {
            if (tmpPrice.substring(0, 1).contentEquals("+")) {
                price = new BigDecimal(tmpPrice.substring(1));
            } else  {
                price = new BigDecimal(tmpPrice);
            }
            tr.setPrice(price);
            System.out.println("Price is: " + price);
        } catch (Exception e) {
            System.out.println("Price should only have numbers!! Moving to the next line...");
            return;
        }


    }
}

class ExtendedTrade extends Trade{
    private long version;
    private String nestedTag;

    public ExtendedTrade(String tradeTag, LocalDateTime tradeDateTime, String direction, String itemId, BigDecimal price, long quantity, String buyer, String seller, String tradeComment, long version, String nestedTag) {
        super(tradeTag, tradeDateTime, direction, itemId, price, quantity, buyer, seller, tradeComment);
        this.version = version;
        this.nestedTag = nestedTag;
    }

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

    public void setNestedTag(String nestedTag) {
        this.nestedTag = nestedTag;
    }
}

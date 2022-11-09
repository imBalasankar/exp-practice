package org.example;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

        return;
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

package org.example;

public class Footer {
    public void extractFooter(String footer, long fileVersion) {
        String footerTag;
        long noOfStructures;
        long noOfCharsInStructures;

        System.out.println("------------------FOOTER----------------------");

        //Store footer tag
        footerTag = footer.substring(0,5);
        System.out.println("Tag is : "+footerTag);

        //Store no of trade and extended trade structures
        try {
            noOfStructures = Long.parseLong(footer.substring(5, 15));
            System.out.println("Number of TRADE and EXTRD structures is: "+noOfStructures);
        } catch (Exception e) {
            System.out.println("An error occurred : No of structures - Only numbers allowed!!");
            throw new RuntimeException(e);
        }

        //Store no of characters in trade and extended trade structures
        if (fileVersion==5) {
            try {
                noOfCharsInStructures = Long.parseLong(footer.substring(15, 25));
                System.out.println("Number of characters in TRADE and EXTRD structures is: " + noOfCharsInStructures);
            } catch (Exception e) {
                System.out.println("An error occurred : No of characters in structures - Only numbers allowed!!");
                throw new RuntimeException(e);
            }
        }

    }
}

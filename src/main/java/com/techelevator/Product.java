package com.techelevator;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Product {

    // Instance variables
    private String name;
    private BigDecimal price;
    private String typeName;
    private String slotID;
    private int inventory;
    private String sound;

    //Constructors
    public Product (String slotID, String name, BigDecimal price, String typeName, int inventory, String sound) {
        this.slotID = slotID;
        this.name = name;
        this.price = price;
        this.typeName = typeName;
        this.inventory = inventory;
        this.sound = sound;
    }

    public Product(String slotID, String name, BigDecimal price, String typeName) {
        this.slotID = slotID;
        this.name = name;
        this.price = price;
        this.typeName = typeName;

    }

    public Product() {

    }

    // Getters and setters

    public String getSound() {
        switch (this.typeName) {
            case "Chip":
                sound = "Crunch Crunch, Yum!";
                break;
            case "Candy":
                sound = "Munch Munch, Yum!";
                break;
            case "Gum":
                sound = "Chew Chew, Yum!";
                break;
            case "Drink":
                sound = "Glug Glug, Yum!";
                break;
            default:
                sound = "Ew, what am I eating?";
                break;
        }
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price.setScale(2, RoundingMode.HALF_UP);
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getSlotID() {
        return slotID;
    }

    public void setSlotID(String slotID) {
        this.slotID = slotID;
    }
}

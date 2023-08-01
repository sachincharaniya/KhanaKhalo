package com.pooja.khanakhalo.ModelClass;

public class Cart {
    private String Name,Image,Kitchen;
    private int Quantity;
    private long Price;

    public Cart(){

    }

    public Cart(String name, String image, long price, int quantity, String kitchen){
        Name = name;
        Image = image;
        Price = price;
        Quantity = quantity;
        Kitchen = kitchen;
    }

    public String getName() {
        return Name;
    }
    public String getImage() {
        return Image;
    }
    public long getPrice() {
        return Price;
    }
    public int getQuantity() {
        return Quantity;
    }
    public String getKitchen() {
        return Kitchen;
    }
}

package com.pooja.khanakhalo.ModelClass;

public class Kitchen {

    private String Name;
    private String Image;
    private String Type;
    private String Distance;
    private String Description;
    private int Charge;
    private String Status;
    private int DefaultPrice;

    public Kitchen(){

    }
    public Kitchen(String name, String image, String type, String distance, String description, int defaultPrice, int charge, String status){
        Name = name;
        Image = image;
        Type = type;
        Distance = distance;
        Description = description;
        DefaultPrice = defaultPrice;
        Charge = charge;
        Status = status;
    }

    public String getName(){
        return Name;
    }
    public String getImage(){
        return Image;
    }
    public String getType(){
        return Type;
    }
    public String getDistance(){
        return Distance;
    }
    public String getDescription(){
        return Description;
    }
    public int getDefaultPrice(){
        return DefaultPrice;
    }
    public int getCharge(){
        return Charge;
    }
    public String getStatus(){
        return Status;
    }
}

package com.example.km.spendingrec;

/**
 * Created by YiLong on 9/12/17.
 */

public class Entry {
    //public String product;
    public String Merchant;
    public String Address;
    public String Date;
    public String Time;
    public double Price;
    public double Quantity;

    public Entry(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Entry(String merchant, String address, String date, String time, double price, double quantity) {
        //this.product = product;
        this.Merchant = merchant;
        this.Address = address;
        this.Date = date;
        this.Time = time;
        this.Price = price;
        this.Quantity = quantity;
    }
}

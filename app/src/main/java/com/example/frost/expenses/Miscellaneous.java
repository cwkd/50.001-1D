package com.example.frost.expenses;

/**
 * Created by John on 8/12/2017.
 */

public class Miscellaneous implements Visitable{

    private String productName = "";
    private double productPrice = 0.00;
    // Your code goes here
    public void accept(Visitor v){
        v.visit(this);
    }

    public Miscellaneous(String productName, double productPrice){
        this.productName = productName;
        this.productPrice = productPrice;
    }

    public String getProduct(){
        return productName;
    }

    public Double getPrice(){
        return productPrice;
    }

}

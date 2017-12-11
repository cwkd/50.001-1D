package com.example.frost.expenses;

/**
 * Created by Daniel on 11/12/2017.
 */

public class Item {

    public String category;
    public String product;
    public String price;

    Item(String category, String product, String price) {
        this.category = category;
        this.product = product;
        this.price = price;
    }

}

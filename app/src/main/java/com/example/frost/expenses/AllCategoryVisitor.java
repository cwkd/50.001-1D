package com.example.frost.expenses;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by John on 8/12/2017.
 */

public class AllCategoryVisitor {

    private boolean bFood;
    private boolean bTravel;
    private boolean bEmergency;
    private boolean bMiscellaneous;

    public AllCategoryVisitor(boolean bFood, boolean bTravel, boolean bEmergency, boolean bMiscellaneous){
        this.bFood = bFood;
        this.bTravel = bTravel;
        this.bEmergency = bEmergency;
        this.bMiscellaneous = bMiscellaneous;
    }

    private int category;

    public int getRecommendedCategory(){
        if(bFood){
            this.category = 1;
        }
        if(bTravel){
            this.category = 2;
        }
        if(bEmergency){
            this.category = 3;
        }
        if(bMiscellaneous){
            this.category = 4;
        }

        return category;
    }

    private ArrayList<Visitable> items = new ArrayList<Visitable>();

    public static void main (String[] args) throws JSONException {
        System.out.println("Hello World! Hello World! Hello World! Hello World! Hello World! Hello World!");

        ArrayList<Visitable> items = new ArrayList<Visitable>();
        CategoryVisitor postage = new CategoryVisitor();
        CategoryReadJSON readJSON = new CategoryReadJSON(new JSONObject());
        AllCategoryVisitor ACV = new AllCategoryVisitor(readJSON.bCategory("food"), readJSON.bCategory("travel"), readJSON.bCategory("emergency"), readJSON.bCategory("miscellaneous"));

        HashMap<String, Double> Data = readJSON.extractData();
        int category = ACV.getRecommendedCategory();

        for(Map.Entry<String, Double> data : Data.entrySet()) {
            String productName = data.getKey();
            Double productPrice = data.getValue();
            switch (category) {
                case 1:
                    items.add(new Food(productName, productPrice));
                case 2:
                    items.add(new Travel(productName, productPrice));
                case 3:
                    items.add(new Emergency(productName, productPrice));
                case 4:
                    items.add(new Miscellaneous(productName, productPrice));
            }
        }

        for (Visitable o : items) {
            o.accept(postage);
        }
        System.out.println(postage.getTotalBudget());
        System.out.println(postage.getProductList());
    }

}

package com.example.frost.expenses;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by John on 8/12/2017.
 */

public class CategoryVisitor implements Visitor {

    private double totalSpending;
    private List<String> productList = new ArrayList<String>();
    // Your code goes here
    public CategoryVisitor(){
    }


    @Override
    public void visit(Food f) {
        totalSpending+= f.getPrice();
        productList.add(f.getProduct());
    }

    @Override
    public void visit(Travel t) {
        totalSpending += t.getPrice();
        productList.add(t.getProduct());
    }

    @Override
    public void visit(Miscellaneous m) {
        totalSpending += m.getPrice();
        productList.add(m.getProduct());
    }

    @Override
    public void visit(Emergency e) {
        totalSpending += e.getPrice();
        productList.add(e.getProduct());
    }

    public double getTotalBudget(){
        return totalSpending;
    }

    public List<String> getProductList(){
        return productList;
    }
}

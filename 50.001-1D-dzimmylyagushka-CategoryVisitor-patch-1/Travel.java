package expenses;

/**
 * Created by John on 8/12/2017.
 */

public class Travel implements Visitable{

    private String productName = "";
    private double productPrice = 0.00;
    // Your code goes here
    public void accept(Visitor v){
        v.visit(this);
    }

    public Travel(String productName, double productPrice){
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

package com.example.frost.expenses;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;


/**
 * Created by cindy on 13/12/2017.
 */

public class pushToDatabase {

    String firebaseURL = "https://moneywise-a5fef.firebaseio.com/";

    public void addToDatabase(JSONObject jsonObject)
    {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl(firebaseURL);

        String product="null";
        String address="null";
        double price=0;
        int quantity=0;
        Entry newEntry;

        try{
            address = jsonObject.getString("Address");

            while(jsonObject.keys().hasNext()){
                if (jsonObject.keys().next()!="Address"){
                    product = jsonObject.keys().next();
                    //used substring to remove the $ sign
                    price = Double.parseDouble(jsonObject.getString(jsonObject.keys().next()).substring(1));
                    quantity = 1;

                    newEntry = new Entry("null",address, "null","null",price,quantity);
                    mDatabase.child(product).setValue(newEntry);
                }
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
            Log.i("heegege","hello");
        }
    }
}

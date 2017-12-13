package com.example.frost.expenses;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.util.Iterator;
import java.util.Objects;


/**
 * Created by cindy on 13/12/2017.
 */

public class pushToDatabase {

    String firebaseURL = "https://moneywise-a5fef.firebaseio.com/";

    public void addToDatabase(JSONObject myjson)
    {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl(firebaseURL);

        String product="null";
        String address="null";
        double price=0;
        int quantity=0;
        Entry newEntry;

        try{
            address = myjson.getString("Address");
            //Log.i("yilong",address);
            Iterator<String> iter = myjson.keys();
            while(iter.hasNext()){
                String key = iter.next();
                //if (!Objects.equals(jsonObject.keys().next(), "Address")){
                if (!key.equals("Address")){

                    product = key;
                    //used substring to remove the $ sign
                    price = Double.parseDouble(myjson.getString(product).substring(1));
                    quantity = 1;

                    newEntry = new Entry(product,address, "null","null",price,quantity);
                    mDatabase.child(product).setValue(newEntry);
                    //Log.i("yilong",product);
                    //Log.i("yilong",String.valueOf(price) );
                }
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
            Log.i("heegege","hello");
        }
    }
}

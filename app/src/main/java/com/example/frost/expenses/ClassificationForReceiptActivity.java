package com.example.frost.expenses;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Daniel on 11/12/2017.
 */

public class ClassificationForReceiptActivity extends AppCompatActivity implements ReclassifyDialogFragment.ReclassifyDialogListener {

    private JSONObject results = null;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ReclassifyDialogFragment dialog;
    ArrayList<Item> mDataset;
    private int adapaterPos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classification_for_receipt);
        Intent intent = getIntent();
        try {
            results =  new JSONObject(intent.getStringExtra("results"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (results != null) {
            mDataset = new ArrayList<>();
            ArrayList<Visitable> items = new ArrayList<Visitable>();
            CategoryVisitor postage = new CategoryVisitor();
            try {
                CategoryReadJSON readJSON = new CategoryReadJSON(results);
                AllCategoryVisitor ACV = new AllCategoryVisitor(readJSON.bCategory("food"), readJSON.bCategory("travel"), readJSON.bCategory("emergency"), readJSON.bCategory("miscellaneous"));

                HashMap<String, Double> Data = readJSON.extractData();
                int category = ACV.getRecommendedCategory();

                for (Map.Entry<String, Double> data : Data.entrySet()) {
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
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ArrayList<String> cat = (ArrayList<String>) postage.getProductList();
            Iterator<String> categories = cat.iterator();
            while (categories.hasNext()) {
                Log.d("Cat", categories.next());
            }
            Iterator<String> iter = results.keys();
            Log.d("Cat", "Done");
            while (iter.hasNext()) {
                String product = iter.next();
                if (!product.equals("Address")) {
                    try {
                        Item item = new Item("ItemCat", product, String.valueOf(results.get(product).toString()));
                        mDataset.add(item);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        mRecyclerView = findViewById(R.id.classification_recycler);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ClassificationRecyclerAdapter(this, mDataset, this);
        mRecyclerView.setAdapter(mAdapter);


    }

    @Override
    public void onReclassify(String string) {
        Item item = mDataset.get(adapaterPos);
        item.category = string;
        mAdapter.notifyDataSetChanged();
    }

    public void reclassifyCallback(int pos) {
        adapaterPos = pos;
    }

    public void showDialog() {
        dialog = new ReclassifyDialogFragment();
        FragmentManager fm = getFragmentManager();
        dialog.show(fm, "ReclassifyDialog");
    }
}
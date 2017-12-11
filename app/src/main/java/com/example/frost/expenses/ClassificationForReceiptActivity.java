package com.example.frost.expenses;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

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
            Iterator<String> iter = results.keys();
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

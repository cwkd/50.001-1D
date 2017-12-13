package com.example.frost.expenses;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Daniel on 11/12/2017.
 */

public class ClassificationRecyclerAdapter extends RecyclerView.Adapter<ClassificationRecyclerAdapter.ViewHolder> {

    public static final String ITEMS = "items";
    public static final String FROM_CLASSIFICATION = "fromClassification";
    private ArrayList<Item> mDataset;
    Context parentContext;
    ClassificationForReceiptActivity parentAct;



    public ClassificationRecyclerAdapter(Context c, ArrayList<Item> dataset, ClassificationForReceiptActivity act) {
        parentContext = c;
        mDataset = dataset;
        parentAct = act;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        TextView categoryView;
        TextView productView;
        TextView priceView;
        Button acceptButton;

        public ViewHolder(View view) {
            super(view);
            categoryView = view.findViewById(R.id.category_view);
            productView = view.findViewById(R.id.product_view);
            priceView = view.findViewById(R.id.price_view);
            acceptButton = view.findViewById(R.id.accept_button);
            view.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            parentAct.reclassifyCallback(getAdapterPosition());
            parentAct.showDialog();
            return true;
        }
    }

    @Override
    public ClassificationRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (viewType == R.layout.classification_recycler_row) {
            v = LayoutInflater.from(parentContext).inflate(R.layout.classification_recycler_row, parent, false);
        } else {
            v = LayoutInflater.from(parentContext).inflate(R.layout.classification_recycler_footer, parent, false);
        }
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (position == mDataset.size()) {
            holder.acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(parentAct, "Hello", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(parentAct, MainActivity.class);
                    double[] items = {0.0,0.0,0.0,0.0};

                    for (Item item: mDataset) {
                        if (item.category.equals("Food")) {
                            items[0] = items[0] + Double.parseDouble(item.price.replace("$", ""));
                        } else if (item.category.equals("Transport")) {
                            items[1] = items[1] + Double.parseDouble(item.price.replace("$", ""));
                        } else if (item.category.equals("Emergency")) {
                            items[2] = items[2] + Double.parseDouble(item.price.replace("$", ""));
                        } else if (item.category.equals("Misc")) {
                            items[3] = items[3] + Double.parseDouble(item.price.replace("$", ""));
                        }
                    }
                    intent.putExtra(ITEMS, items);
                    intent.putExtra(FROM_CLASSIFICATION, true);
                    parentAct.startActivity(intent);
                }
            });
        } else {
            if (position % 2 == 0) {
                holder.itemView.setBackgroundResource(R.color.secondaryColor);
            }
            holder.categoryView.setText(mDataset.get(position).category);
            holder.productView.setText(mDataset.get(position).product);
            holder.priceView.setText(mDataset.get(position).price);
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == mDataset.size()) ? R.layout.classification_recycler_footer : R.layout.classification_recycler_row;
    }
}

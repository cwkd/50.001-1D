package com.example.frost.expenses;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Daniel on 11/12/2017.
 */

public class ClassificationRecyclerAdapter extends RecyclerView.Adapter<ClassificationRecyclerAdapter.ViewHolder> {

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

        public ViewHolder(View view) {
            super(view);
            categoryView = view.findViewById(R.id.category_view);
            productView = view.findViewById(R.id.product_view);
            priceView = view.findViewById(R.id.price_view);
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
        View v = LayoutInflater.from(parentContext).inflate(R.layout.classification_recycler_row, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position % 2 == 0) {
            holder.itemView.setBackgroundResource(R.color.secondaryColor);
        }

        holder.categoryView.setText(mDataset.get(position).category);
        holder.productView.setText(mDataset.get(position).product);
        holder.priceView.setText(mDataset.get(position).price);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}

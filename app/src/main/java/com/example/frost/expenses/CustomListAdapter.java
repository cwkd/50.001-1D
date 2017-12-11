package com.example.frost.expenses;

import android.app.Activity;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListAdapter extends ArrayAdapter {
    //to reference the Activity
    private final Activity context;

    //to store the list of numbers
    private final String[] numberArray;

    //to store the list of categories
    private final String[] categoryArray;

    private final int[] iconArray;

    public CustomListAdapter(Activity context, String[] categoryArrayParam, String[] numberArrayParam, int[] iconArrayParam) {
        super(context, R.layout.listview_row, categoryArrayParam);
        this.context = context;
        this.numberArray = numberArrayParam;
        this.categoryArray = categoryArrayParam;
        this.iconArray = iconArrayParam;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_row, null, true);

        //this code gets references to objects in the listview_row.xml file
        TextView numberTextField = (TextView) rowView.findViewById(R.id.secondLine);
        TextView categoryTextField = (TextView) rowView.findViewById(R.id.firstLine);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        //this code sets the values of the objects to values from the arrays
        numberTextField.setText(numberArray[position]);
        categoryTextField.setText(categoryArray[position]);
        imageView.setImageResource(iconArray[position]);

        return rowView;
    }
}
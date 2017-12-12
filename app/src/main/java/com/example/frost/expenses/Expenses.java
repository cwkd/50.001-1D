package com.example.frost.expenses;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.*;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.*;

import java.util.ArrayList;

import linanalysistools.*;

public class Expenses extends ListFragment {

    float[] dataArray = {};
    spendingAnalyser analyser = new spendingAnalyser(300, 1, 31, 4);
    String[] categoryArray = {"Food", "Transportation", "Emergency", "Miscellaneous"};
    spendingAnalyser lastMonthAnalyser;
    spendingAnalyser last2MonthAnalyser;
    String[] numberArray = {"", "", "", ""};
    int[] iconArray = {R.drawable.ic_food, R.drawable.ic_transportation, R.drawable.ic_emergency, R.drawable.ic_misc};
    PieChart pieChart;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expenses, container, false);
        final Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
        String[] values =
                {"This Month", "Last Month", "Last 2 Months"};
        ArrayAdapter<String> spinnerAdapter =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, values);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(spinnerAdapter);
        pieChart = (PieChart) view.findViewById(R.id.chart);
        pieChart.setRotationEnabled(true);
        pieChart.setHoleRadius(20f);
        pieChart.setCenterTextSize(20);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View selectedItemView, int position, long id) {
                String selection = spinner.getSelectedItem().toString().trim();
                Log.i("Selection", selection);
                CustomListAdapter adapter = new CustomListAdapter(getActivity(), categoryArray, numberArray, iconArray);
                switch (selection) {
                    case "This Month":
                        analyser = new spendingAnalyser(300, 1, 31, 4);
                        dataArray = new float[]{(float) analyser.getMonthlyMealPool(), (float) analyser.getMonthlyTransportPool(),
                                (float) analyser.getMonthlyEmergencyPool(), (float) analyser.getMonthlyMiscPool()};
                        numberArray = new String[]{Double.toString(analyser.getMonthlyMealPool()), Double.toString(analyser.getMonthlyTransportPool()),
                                Double.toString(analyser.getMonthlyEmergencyPool()), Double.toString(analyser.getMonthlyMiscPool())};
                        Log.i("Yay", numberArray[0]);
                        addDataSet(categoryArray, dataArray);
                        adapter = new CustomListAdapter(getActivity(), categoryArray, numberArray, iconArray);
                        setListAdapter(adapter);
                        break;
                    case "Last Month":
                        lastMonthAnalyser = new spendingAnalyser(200, 1, 31, 4);
                        dataArray = new float[]{(float) lastMonthAnalyser.getMonthlyMealPool(), (float) lastMonthAnalyser.getMonthlyTransportPool(),
                                (float) lastMonthAnalyser.getMonthlyEmergencyPool(), (float) lastMonthAnalyser.getMonthlyMiscPool()};
                        numberArray = new String[]{Double.toString(lastMonthAnalyser.getMonthlyMealPool()), Double.toString(lastMonthAnalyser.getMonthlyTransportPool()),
                                Double.toString(lastMonthAnalyser.getMonthlyEmergencyPool()), Double.toString(lastMonthAnalyser.getMonthlyMiscPool())};
                        Log.i("Yay", numberArray[0]);
                        addDataSet(categoryArray, dataArray);
                        adapter = new CustomListAdapter(getActivity(), categoryArray, numberArray, iconArray);
                        setListAdapter(adapter);
                        break;
                    case "Last 2 Months":
                        last2MonthAnalyser = new spendingAnalyser(400, 20, 31, 10);
                        dataArray = new float[]{(float) last2MonthAnalyser.getMonthlyMealPool(), (float) last2MonthAnalyser.getMonthlyTransportPool(),
                                (float) last2MonthAnalyser.getMonthlyEmergencyPool(), (float) last2MonthAnalyser.getMonthlyMiscPool()};
                        numberArray = new String[]{Double.toString(last2MonthAnalyser.getMonthlyMealPool()), Double.toString(last2MonthAnalyser.getMonthlyTransportPool()),
                                Double.toString(last2MonthAnalyser.getMonthlyEmergencyPool()), Double.toString(last2MonthAnalyser.getMonthlyMiscPool())};
                        Log.i("Yay", numberArray[0]);
                        addDataSet(categoryArray, dataArray);
                        adapter = new CustomListAdapter(getActivity(), categoryArray, numberArray, iconArray);
                        setListAdapter(adapter);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                dataArray = new float[]{0, 0, 0, 0};
                numberArray = new String[]{"", "", "", ""};
                CustomListAdapter adapter = new CustomListAdapter(getActivity(), categoryArray, numberArray, iconArray);
                setListAdapter(adapter);
            }

        });
        tracker spendingTracker = new tracker();
        return view;
    }

    public void addDataSet(String[] category, float[] numbers) {
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();

        for (int i = 0; i < numbers.length; i++) {
            yEntrys.add(new PieEntry(numbers[i], i));
        }

        for (int i = 0; i < category.length; i++) {
            xEntrys.add(category[i]);
        }

        PieDataSet pieDataSet = new PieDataSet(yEntrys, "Budget You Have Left Now");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(16);

        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.DEFAULT);

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.rgb(255, 248, 225));
        colors.add(Color.rgb(204, 197, 175));
        colors.add(Color.rgb(255, 236, 179));
        colors.add(Color.rgb(203, 186, 131));

        pieDataSet.setColors(colors);

        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    public static Expenses newInstance() {
        return new Expenses();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
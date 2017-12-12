package com.example.libanalysistools;

import java.util.ArrayList;

public class Tracker {
	
	private static final int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	private static final int[] daysInMonthLeapYear = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

	private ArrayList<SpendingAnalyser> threeMonthsHistory;
	private SpendingAnalyser currentMonth;

	public Tracker() {
		threeMonthsHistory = new ArrayList<>();
		currentMonth = null;
	}

	public void startNewMonth(double budget, int dayOfMonth, int month, int firstMeal) {
		if (currentMonth == null) {
			threeMonthsHistory.add(currentMonth);
			if (threeMonthsHistory.size() > 3) {
				threeMonthsHistory.remove(0);
			}
		}
		if (budget == 0) {
			for (SpendingAnalyser m: threeMonthsHistory) {
				budget += m.getTotalExp();
			}
			budget /= 3.0;
			budget *= 0.95;
		}
		currentMonth = new SpendingAnalyser(budget, dayOfMonth, daysInMonth[month - 1], firstMeal);
	}

	public void startNewMonthLeapYear(double budget, int dayOfMonth, int month, int firstMeal) {
		if (currentMonth == null) {
			threeMonthsHistory.add(currentMonth);
			if (threeMonthsHistory.size() > 3) {
				threeMonthsHistory.remove(0);
			}
		}
		if (budget == 0) {
			for (SpendingAnalyser m: threeMonthsHistory) {
				budget += m.getTotalExp();
			}
			budget /= 3.0;
			budget *= 0.95;
		}
		currentMonth = new SpendingAnalyser(budget, dayOfMonth, daysInMonthLeapYear[month - 1], firstMeal);
	}

	public SpendingAnalyser getCurrentMonth() {
		return currentMonth;
	}

	public SpendingAnalyser getPastMonth(int numMonthsBack) {
		return threeMonthsHistory.get(numMonthsBack-1);
	}
}


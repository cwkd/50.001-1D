package linanalysistools;

public class spendingAnalyser {

    private double startingBudget;
    private int daysInMonth;
    private int dayOfMonth;
    private int currentMeal;

    private double[][] mealRecords;
    private double[][] transportRecords;

    private double monthlyMealAlloc;
    private double monthlyTransportAlloc;
    private double monthlyEmergencyAlloc;
    private double monthlyMiscAlloc;

    private double monthlyMealPool;
    private double monthlyTransportPool;
    private double monthlyEmergencyPool;
    private double monthlyMiscPool;

    private double monthlyMealOverdraft;
    private double monthlyTransportOverdraft;
    private double monthlyEmergencyOverdraft;
    private double monthlyMiscOverdraft;

    private double dailyMealAlloc;
    private double dailyTransportAlloc;
    private double dailyMealPool;
    private double dailyTransportPool;

    private double dailyMealOverdraft;
    private double dailyTransportOverdraft;

    private double monthlyBreakfastExp;
    private double monthlyLunchExp;
    private double monthlyDinnerExp;
    private double monthlyExtraMealExp;
    private double monthlyTransportExp;

    private static final int TRANSPORT = 1000;
    private static final int BREAKFAST = 0000;
    private static final int LUNCH = 0001;
    private static final int DINNER = 0010;
    private static final int EXTRA_MEAL = 0011;
    private static final int MEAL = 0100;

    public spendingAnalyser(double budget, int dayOfMonth, int daysInMonth, int firstMeal) {
        startingBudget = budget;
        monthlyEmergencyAlloc = startingBudget*0.2;
        monthlyMiscAlloc = startingBudget*0.1;
        monthlyMealAlloc = (startingBudget - (monthlyEmergencyAlloc + monthlyMiscAlloc)) * 0.65;
        monthlyTransportAlloc = startingBudget - (monthlyMealAlloc + monthlyEmergencyAlloc + monthlyMiscAlloc);
        monthlyMealPool = monthlyMealAlloc; monthlyMiscPool = monthlyMiscAlloc; monthlyEmergencyPool = monthlyEmergencyAlloc; monthlyTransportPool = monthlyTransportAlloc;
        this.dayOfMonth = dayOfMonth;
        this.daysInMonth = daysInMonth;
        mealRecords = new double[daysInMonth][5];
        transportRecords = new double[daysInMonth][2];
        currentMeal = firstMeal;
        monthlyBreakfastExp = 0; monthlyLunchExp = 0; monthlyDinnerExp = 0; monthlyExtraMealExp = 0;
        monthlyMealOverdraft = 0; monthlyTransportOverdraft = 0;
    }

    public double getMonthlyMealAlloc() {
        return monthlyMealAlloc;
    }

    public double getMonthlyTransportAlloc() {
        return monthlyTransportAlloc;
    }

    public double getMonthlyMiscAlloc() {
        return monthlyMiscAlloc;
    }

    public double getMonthlyEmergencyAlloc() {
        return monthlyEmergencyAlloc;
    }

    public double getMonthlyMealPool() {
        return monthlyMealPool;
    }

    public double getMonthlyTransportPool() {
        return monthlyTransportPool;
    }

    public double getMonthlyMiscPool() {
        return monthlyMiscPool;
    }

    public double getMonthlyEmergencyPool() {
        return monthlyEmergencyPool;
    }

    public double getMonthlyBreakfastExp() {
        return monthlyBreakfastExp;
    }

    public double getMonthlyLunchExp() {
        return monthlyLunchExp;
    }

    public double getMonthlyDinnerExp() {
        return monthlyDinnerExp;
    }

    public double getMonthlyExtraMealExp() {
        return monthlyExtraMealExp;
    }

    public double getMonthlyTransportExp() {
        return monthlyTransportExp;
    }

    public double getMonthlyEmergencyExp() {
        if (monthlyEmergencyOverdraft == 0) {
            return monthlyEmergencyAlloc - monthlyEmergencyPool;
        } else {
            return monthlyEmergencyAlloc + monthlyEmergencyOverdraft;
        }
    }

    public double getMonthlyMiscExp() {
        if (monthlyMiscOverdraft == 0) {
            return monthlyMiscAlloc - monthlyMiscPool;
        } else {
            return monthlyMiscAlloc + monthlyMiscOverdraft;
        }
    }

    public double getMonthlyMealOverdraft() {
        return monthlyMealOverdraft;
    }

    public double getMonthlyTransportOverdraft() {
        return monthlyTransportOverdraft;
    }

    public double getMonthlyEmergencyOverdraft() {
        return monthlyEmergencyOverdraft;
    }

    public double getMonthlyMiscOverdraft() {
        return monthlyMiscOverdraft;
    }

    public double getTotalExp() {
        double retVal = monthlyBreakfastExp + monthlyLunchExp
                + monthlyDinnerExp + monthlyExtraMealExp + monthlyTransportExp
                + getMonthlyEmergencyExp() + getMonthlyMiscExp();
        return retVal;
    }

    public double getTotalOverdraft() {
        double retVal = monthlyMealOverdraft + monthlyTransportOverdraft
                + monthlyMiscOverdraft + monthlyEmergencyOverdraft;
        return retVal;
    }

    /**
     * Method adds to current monthly meal allocation after deducting balance of monthly meal overdraft
     * @param extra amount to add
     */
    public void addMonthlyMealAlloc(double extra) {
        if (monthlyMealOverdraft > 0) {
            if (monthlyMealOverdraft >= extra) {
                monthlyMealOverdraft -= extra;
                extra = 0;
            } else {
                extra -= monthlyMealOverdraft;
                monthlyMealOverdraft  = 0;
            }
        }
        monthlyMealAlloc += extra;
        monthlyMealPool += extra;
    }

    public void addMonthlyTransportAlloc(double extra) {
        if (monthlyTransportOverdraft > 0) {
            if (monthlyTransportOverdraft >= extra) {
                monthlyTransportOverdraft -= extra;
                extra = 0;
            } else {
                extra -= monthlyTransportOverdraft;
                monthlyTransportOverdraft = 0;
            }
        }
        monthlyTransportAlloc += extra;
        monthlyTransportPool += extra;
    }

    public void addMonthlyMiscAlloc(double extra) {
        if (monthlyMiscOverdraft > 0) {
            if (monthlyMiscOverdraft >= extra) {
                monthlyMiscOverdraft -= extra;
                extra = 0;
            } else {
                extra -= monthlyMiscOverdraft;
                monthlyMiscOverdraft = 0;
            }
        }
        monthlyMiscAlloc += extra;
        monthlyMiscPool += extra;
    }

    public void addMonthlyEmergencyAlloc(double extra) {
        if (monthlyEmergencyOverdraft > 0) {
            if (monthlyEmergencyOverdraft >= extra) {
                monthlyEmergencyOverdraft -= extra;
                extra = 0;
            } else {
                extra -= monthlyEmergencyOverdraft;
                monthlyEmergencyOverdraft = 0;
            }
        }
        monthlyEmergencyAlloc += extra;
        monthlyEmergencyPool += extra;
    }

    /**
     * Method to allocate funds to daily expenditure pools
     */
    public void assignDailyAlloc() {
        dailyMealAlloc = monthlyMealPool / (daysInMonth - dayOfMonth);
        monthlyMealPool -= dailyMealAlloc;
        dailyTransportAlloc = monthlyTransportPool / (daysInMonth - dayOfMonth);
        monthlyTransportPool -= dailyTransportAlloc;
        dailyTransportPool = dailyTransportAlloc; dailyMealPool = dailyMealAlloc;
        dailyMealOverdraft = 0; dailyTransportOverdraft = 0;
        currentMeal = BREAKFAST;
    }

    /**
     * Method to reteive expenditure amount for a specified type for a day
     * @param entryType indicates which type of specific meal or whether it is TRANSPORT
     * @param day indicates the day of the month
     * @return
     */
    public double getExpenditure(int entryType, int day) {
        double retVal; day--;
        switch (entryType) {
            case TRANSPORT:
                retVal = transportRecords[day][0];
                break;
            case BREAKFAST:
                retVal = mealRecords[day][0];
                break;
            case LUNCH:
                retVal = mealRecords[day][1];
                break;
            case DINNER:
                retVal = mealRecords[day][2];
                break;
            case EXTRA_MEAL:
                retVal = mealRecords[day][3];
                break;
            default:
                retVal = 0;
        }
        return retVal;
    }

    /**
     * Method to retrieve overdraft amount for a specified type for a day
     * @param entryType indicates whether type is TRANSPORT or MEAL
     * @param day indicates the day of the month
     * @return
     */
    public double getOverdraft(int entryType, int day) {
        double retVal; day--;
        switch (entryType) {
            case TRANSPORT:
                retVal = transportRecords[day][1];
                break;
            case MEAL:
                retVal = mealRecords[day][4];
                break;
            default:
                retVal = 0;
        }
        return retVal;
    }

    public void updateEntry(int entryType, int day, double newValue) {
        switch (entryType) {
            case TRANSPORT:
                if (transportRecords[day][0] == 0) {
                    monthlyTransportExp += newValue;
                } else {
                    transportRecords[day][0] = newValue - transportRecords[day][0];
                }
                transportRecords[day][0] = newValue;
                break;
            case BREAKFAST:
                if (mealRecords[day][0] == 0) {
                    monthlyBreakfastExp += newValue;
                } else {
                    monthlyBreakfastExp += newValue - mealRecords[day][0];
                }
                mealRecords[day][0] = newValue;
                break;
            case LUNCH:
                if (mealRecords[day][1] == 0) {
                    monthlyLunchExp += newValue;
                } else {
                    monthlyLunchExp += newValue - mealRecords[day][1];
                }
                mealRecords[day][1] = newValue;
            case DINNER:
                if (mealRecords[day][2] == 0) {
                    monthlyDinnerExp += newValue;
                } else {
                    monthlyDinnerExp += newValue - mealRecords[day][2];
                }
                mealRecords[day][2] = newValue;
            case EXTRA_MEAL:
                if (mealRecords[day][3] == 0) {
                    monthlyExtraMealExp += newValue;
                } else {
                    monthlyExtraMealExp += newValue - mealRecords[day][3];
                }
                mealRecords[day][3] = newValue;
        }
    }

    /**
     * Method to advance the current day counter
     */
    public void advanceDayCounter() {
        dayOfMonth++;
    }

    /**
     * Method to end the day, advances day counter, recalculates all expenditure pools
     */
    public void endDay() {
        monthlyMealPool += dailyMealPool;
        monthlyTransportPool += dailyTransportPool;
        monthlyMealPool -= dailyMealOverdraft;
        monthlyTransportPool -= dailyTransportOverdraft;
        mealRecords[dayOfMonth-1][4] = dailyMealOverdraft;
        transportRecords[dayOfMonth-1][1] = dailyTransportOverdraft;
        monthlyMealOverdraft += dailyMealOverdraft;
        transportRecords[dayOfMonth-1][0] = (dailyTransportAlloc - dailyTransportPool) + dailyTransportOverdraft;
        transportRecords[dayOfMonth-1][1] = dailyTransportOverdraft;
        advanceDayCounter();
    }

    /**
     * Method to redistribute monthly transport pool
     */
    public void topUpDailyTransportPool() {
        monthlyTransportPool += dailyTransportPool;
        if (monthlyTransportPool == 0) {
            if (monthlyMiscPool == 0) {
                monthlyMiscPool +=  monthlyEmergencyPool *0.1;
                monthlyEmergencyPool -= monthlyEmergencyPool *0.1;
            }
            monthlyTransportPool +=  monthlyMiscPool *0.1;
            monthlyMiscPool -= monthlyMiscPool *0.1;
        }
        dailyTransportPool = monthlyTransportPool / (daysInMonth - dayOfMonth-1);
        monthlyTransportPool -= dailyTransportPool;
    }

    /**
     * Method to redistribute monthly meal pool
     */
    public void topUpDailyMealPool() {
        monthlyMealPool += dailyMealPool;
        if (monthlyMealPool == 0) {
            if (monthlyMiscPool == 0) {
                monthlyMiscPool +=  monthlyEmergencyPool *0.1;
                monthlyEmergencyPool -= monthlyEmergencyPool *0.1;
            }
            monthlyMealPool +=  monthlyMiscPool *0.1;
            monthlyMiscPool -= monthlyMiscPool *0.1;
        }
        dailyMealPool = monthlyMealPool / (daysInMonth - dayOfMonth-1);
        monthlyMealPool -= dailyMealPool;
    }

    /**
     * Method takes in a double value and calculates the remaining monthly emergency pool left
     * @param expenditure
     */
    public void logEmergency(double expenditure) {
        if (monthlyEmergencyPool == 0) {
            monthlyEmergencyOverdraft += expenditure;
        } else if (monthlyEmergencyPool - expenditure > 0) {
            monthlyEmergencyPool -= expenditure;
        } else {
            monthlyEmergencyOverdraft += (expenditure - monthlyEmergencyPool);
            monthlyEmergencyPool = 0;
        }
    }

    /**
     * Method takes in a double value and calculates the remaining monthly misc pool left
     * @param expenditure
     */
    public void logMisc(double expenditure) {
        if (monthlyMiscPool == 0) {
            monthlyMiscOverdraft += expenditure;
        } else if (monthlyMiscPool - expenditure > 0) {
            monthlyMiscPool -= expenditure;
        } else {
            monthlyMiscOverdraft += (expenditure - monthlyMiscPool);
            monthlyMiscPool = 0;
        }
    }

    /**
     * Method takes in a double value and calculates the remaining daily transport pool left
     * @param expenditure
     */
    public void logTransport(double expenditure) {
        if (dailyTransportPool == 0) {
            dailyTransportOverdraft += expenditure;
        }
        else if (dailyTransportPool > expenditure) {
            dailyTransportPool -= expenditure;
        }
        else {
            dailyTransportOverdraft += (expenditure - dailyTransportPool);
            dailyTransportPool = 0;
        }
    }

    /**
     * Method takes in a double value and calculates the remaining daily meal pool left
     * @param expenditure
     */
    public void logExtraMeal(double expenditure) {
        if (dailyMealPool == 0) {
            dailyMealOverdraft += expenditure;
        }
        else if (dailyMealPool > expenditure) {
            dailyMealPool -= expenditure;
        }
        else {
            dailyMealOverdraft += (expenditure - dailyMealPool);
            dailyMealPool = 0;
        }
        mealRecords[dayOfMonth-1][3] += expenditure;
        monthlyExtraMealExp += expenditure;
    }

    /**
     * Method takes in a double value and calculates the remaining daily meal pool left
     * Advances the current meal tracker
     * Records any overdraft in the expenditure file
     * @param expenditure
     */
    public void logMainMeal(double expenditure) {
        if (dailyMealPool == 0) {
            dailyMealOverdraft += expenditure;
        }
        else if (dailyMealPool > expenditure) {
            dailyMealPool -= expenditure;
        }
        else {
            dailyMealOverdraft += (expenditure - dailyMealPool);
            dailyMealPool = 0;
        }

        switch (currentMeal) {
            case BREAKFAST:
                currentMeal = LUNCH;
                mealRecords[dayOfMonth-1][0] = expenditure;
                monthlyBreakfastExp += expenditure;
                break;

            case LUNCH:
                currentMeal = DINNER;
                mealRecords[dayOfMonth-1][1] = expenditure;
                monthlyLunchExp += expenditure;
                break;

            case DINNER:
                mealRecords[dayOfMonth-1][2] = expenditure;
                monthlyDinnerExp += expenditure;
                break;
        }
    }

    /**
     * Method advances current meal tracker
     */
    public void skipMeal() {
        switch (currentMeal) {
            case BREAKFAST:
                currentMeal = LUNCH;
                break;
            case LUNCH:
                currentMeal = DINNER;
                break;
            case DINNER:
                break;
        }
    }

    /**
     * Method uses daily meal allocation to calculate recommended expenditure for each meal
     * @return double
     */
    public double getMealRecommendation() {
        double recommendation;
        switch (currentMeal) {
            case BREAKFAST:
                recommendation = dailyMealAlloc*0.25;
                break;
            case LUNCH:
                recommendation = dailyMealAlloc*0.35;
                break;
            case DINNER:
                recommendation = dailyMealAlloc*0.40;
                break;
            default:
                recommendation = 0;
        }
        return recommendation;
    }
}

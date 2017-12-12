package com.example.frost.expenses;

import android.provider.BaseColumns;

/**
 * Created by YiLong on 28/11/17.
 */

public class ExpensesData {
    //specifies the table and column names
    public static final class ExpensesEntry implements BaseColumns {
        public static final String TABLE_NAME = "ExpensesRecord";
        public static final String COL_PRODUCT = "Product";
        public static final String COL_MERCHANT = "Merchant";

        //just added address
        public static final String COL_ADDRESS = "Address";

        public static final String COL_DATE = "Date";
        public static final String COL_TIME = "Time";
        public static final String COL_PRICE = "Price";
        public static final String COL_QUANTITY = "Quantity";
        public static final String COL_CATEGORY = "Category";

    }
}
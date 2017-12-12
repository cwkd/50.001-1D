package com.example.frost.expenses;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by YiLong on 28/11/17.
 */

//SQLite Helper Class for managing of all operations related to the database
public class ExpensesDbHelper extends SQLiteOpenHelper {

    private final Context context;
    private static final int DATABASE_VERSION = 1;
    //private SQLiteDatabase sqLiteDatabase;

    public ExpensesDbHelper(Context context){
        super(context,ExpensesData.ExpensesEntry.TABLE_NAME,null,DATABASE_VERSION);
        this.context = context;
    }
    public void onCreate(SQLiteDatabase database) {

        final String SQL_CREATE_TABLE = "CREATE TABLE "+ExpensesData.ExpensesEntry.TABLE_NAME + "(_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ExpensesData.ExpensesEntry.COL_PRODUCT + " TEXT NOT NULL, "
                + ExpensesData.ExpensesEntry.COL_MERCHANT + " TEXT NOT NULL, "
                + ExpensesData.ExpensesEntry.COL_ADDRESS + " TEXT NOT NULL, "
                + ExpensesData.ExpensesEntry.COL_DATE + " TEXT NOT NULL, "
                + ExpensesData.ExpensesEntry.COL_TIME + " TEXT NOT NULL, "
                + ExpensesData.ExpensesEntry.COL_PRICE + " REAL NOT NULL, "
                + ExpensesData.ExpensesEntry.COL_CATEGORY + "TEXT NOT NULL)";
        database.execSQL(SQL_CREATE_TABLE);
    }
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + ExpensesData.ExpensesEntry.TABLE_NAME;
        database.execSQL(SQL_DELETE_TABLE);
        onCreate(database);
    }

    public static String getEntry(SQLiteDatabase sqLiteDatabase, String date, String category) {
        String whereClause = ExpensesData.ExpensesEntry.COL_DATE + "=?";
        String[] whereArgs = {date};
        Cursor cursor = sqLiteDatabase.query(ExpensesData.ExpensesEntry.TABLE_NAME, null, whereClause, whereArgs,
                null, null, null);

        String output = "";
        int placeIndex = cursor.getColumnIndex(category);

        while (cursor.moveToNext()) {
            output += cursor.getString(placeIndex);
        }

        return output;
    }
}

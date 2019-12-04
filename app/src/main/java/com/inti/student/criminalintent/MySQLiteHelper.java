package com.inti.student.criminalintent;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {
    // item purchase table
    public static final String TABLE_ITEM_PURCHASE = "item_purchase";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CART_ITEM_ID = "itemId";
    public static final String COLUMN_CART_ITEM_QTY = "itemQty";
    public static final String COLUMN_CART_ITEM_STATUS = "itemStatus";
    public static final String COLUMN_CART_PAYMENT_DATE = "itemPaymentDate";
    public static final String COLUMN_CART_USER_ID = "itemUserId";

    // user table
    public static final String TABLE_USER = "user";
    public static final String COLUMN_USER_NAME = "userName";
    public static final String COLUMN_USER_EMAIL = "userEmail";
    public static final String COLUMN_USER_PASSWORD = "userPassword";
    public static final String COLUMN_USER_ADDRESS = "userAddress";

    private static final String DATABASE_NAME = "items.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE_ITEM_PURCHASE = "create table "
            + TABLE_ITEM_PURCHASE + "( " + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_CART_ITEM_ID
            + " text not null," + COLUMN_CART_ITEM_QTY
            + " integer not null," + COLUMN_CART_ITEM_STATUS
            + " text not null," + COLUMN_CART_PAYMENT_DATE
            + " text not null," + COLUMN_CART_USER_ID
            + " text not null);";

    // Database creation sql statement
    private static final String DATABASE_CREATE_USER = "create table "
            + TABLE_USER + "( " + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_USER_NAME
            + " text not null," + COLUMN_USER_EMAIL
            + " text not null," + COLUMN_USER_PASSWORD
            + " text not null," + COLUMN_USER_ADDRESS
            + " text not null);";

    public MySQLiteHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database){
        database.execSQL(DATABASE_CREATE_ITEM_PURCHASE);
        database.execSQL(DATABASE_CREATE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM_PURCHASE);
        onCreate(db);
    }
}
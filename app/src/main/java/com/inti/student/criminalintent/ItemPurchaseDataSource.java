package com.inti.student.criminalintent;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class ItemPurchaseDataSource {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = {MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_CART_ITEM_ID,
            MySQLiteHelper.COLUMN_CART_ITEM_QTY,
            MySQLiteHelper.COLUMN_CART_ITEM_STATUS,
            MySQLiteHelper.COLUMN_CART_USER_ID};
    private Long mUserId;
    private Context mContext;

    public ItemPurchaseDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
        // retrieve session data
        SharedPreferences prefs = context.getSharedPreferences("LoginSession", MODE_PRIVATE);
        mUserId = prefs.getLong("userId", 0); // retrieve login session saved in preference

        mContext = context;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public int createItemPurchase(String itemID, int itemQty) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_CART_ITEM_ID, itemID);
        values.put(MySQLiteHelper.COLUMN_CART_ITEM_QTY, itemQty);
        values.put(MySQLiteHelper.COLUMN_CART_ITEM_STATUS, "pending");
        values.put(MySQLiteHelper.COLUMN_CART_USER_ID, mUserId);

        Cursor cursor_item_exist = database.query(MySQLiteHelper.TABLE_ITEM_PURCHASE, allColumns,
                MySQLiteHelper.COLUMN_CART_ITEM_ID + "='" + itemID + "' AND " + MySQLiteHelper.COLUMN_CART_USER_ID + "='" + mUserId + "' AND " + MySQLiteHelper.COLUMN_CART_ITEM_STATUS + "='pending'", null, null, null, null);

        cursor_item_exist.moveToFirst();

        if (cursor_item_exist.isAfterLast()) { // if the query returns 0 row
            long rowInserted = database.insert(MySQLiteHelper.TABLE_ITEM_PURCHASE, null, values);
            cursor_item_exist.close();

            if(rowInserted != -1) { // if insert is successful
                return 1;
            } else {
                return 0;
            }
        } else {
            long cartItemId = cursor_item_exist.getInt(0);
            int current_qty = cursor_item_exist.getInt(2); // current quantity in database

            int update_qty = 0; // quantity to be updated to database

            if (current_qty + itemQty > 10){
                update_qty = 10; // because maximum purchase quantity is 10

                AlertDialog dialog = new AlertDialog.Builder(mContext)
                        .setTitle("Reached maximum purchase quantity")
                        .setMessage("You have exceed maximum purchase quantity of 10. \nYour current purchase quantity has been set to 10.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .create();
                dialog.show();
            } else {
                update_qty = current_qty + itemQty;
            }

            values.clear();
            values.put(MySQLiteHelper.COLUMN_CART_ITEM_QTY, update_qty);

            // update current record in database
            long rowUpdated = database.update(MySQLiteHelper.TABLE_ITEM_PURCHASE, values, MySQLiteHelper.COLUMN_ID + "='" + cartItemId + "' AND " + MySQLiteHelper.COLUMN_CART_USER_ID + "='" + mUserId + "' AND " + MySQLiteHelper.COLUMN_CART_ITEM_STATUS + "='pending'", null);
            cursor_item_exist.close();

            if(rowUpdated != -1) { // if update is successful
                return 1;
            } else {
                return 0;
            }
        }
    }

    public int checkoutOneItem(String itemID, int itemQty) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_CART_ITEM_ID, itemID);
        values.put(MySQLiteHelper.COLUMN_CART_ITEM_QTY, itemQty);
        values.put(MySQLiteHelper.COLUMN_CART_ITEM_STATUS, "paid");
        values.put(MySQLiteHelper.COLUMN_CART_USER_ID, mUserId);

        long rowInserted = database.insert(MySQLiteHelper.TABLE_ITEM_PURCHASE, null, values);

        if(rowInserted != -1) { // if insert is successful
            return 1;
        } else {
            return 0;
        }
    }

        public int updateItemPurchase(long cartItemId, int itemQty) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_ID, cartItemId);
        values.put(MySQLiteHelper.COLUMN_CART_ITEM_QTY, itemQty);

        long rowUpdated = database.update(MySQLiteHelper.TABLE_ITEM_PURCHASE, values, MySQLiteHelper.COLUMN_ID + "='" + cartItemId + "' AND " + MySQLiteHelper.COLUMN_CART_USER_ID + "='" + mUserId + "' AND " + MySQLiteHelper.COLUMN_CART_ITEM_STATUS + "='pending'", null);

        if(rowUpdated != -1) { // if update is successful
            return 1;
        } else {
            return 0;
        }
    }

    public int checkoutItemPurchase() {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_CART_ITEM_STATUS, "paid");

        long rowUpdated = database.update(MySQLiteHelper.TABLE_ITEM_PURCHASE, values, MySQLiteHelper.COLUMN_CART_USER_ID + "='" + mUserId + "' AND " + MySQLiteHelper.COLUMN_CART_ITEM_STATUS + "='pending'", null);

        if(rowUpdated != -1) { // if update is successful
            return 1;
        } else {
            return 0;
        }
    }

    public int deleteItemPurchase() {
        // only delete record belongs to the user
        long rowUpdated = database.delete(MySQLiteHelper.TABLE_ITEM_PURCHASE, MySQLiteHelper.COLUMN_CART_USER_ID
                + " ='" + mUserId + "' AND " + MySQLiteHelper.COLUMN_CART_ITEM_STATUS + "='pending'", null);

        if(rowUpdated != -1) { // if delete is successful
            return 1;
        } else {
            return 0;
        }
    }

    public ArrayList<ItemPurchase> getAllCartItem() {
        ArrayList<ItemPurchase> items = new ArrayList<ItemPurchase>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_ITEM_PURCHASE, allColumns,
                MySQLiteHelper.COLUMN_CART_USER_ID + "='" + mUserId + "' AND " + MySQLiteHelper.COLUMN_CART_ITEM_STATUS + "='pending'", null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ItemPurchase itemPurchase = cursorToItemPurchase(cursor);
            items.add(itemPurchase);
            cursor.moveToNext();
        }

        // make sure to close the cursor
        cursor.close();
        return items;
    }

    public ArrayList<ItemPurchase> getAllPurchasedItem() {
        ArrayList<ItemPurchase> items = new ArrayList<ItemPurchase>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_ITEM_PURCHASE, allColumns,
                MySQLiteHelper.COLUMN_CART_USER_ID + "='" + mUserId + "' AND " + MySQLiteHelper.COLUMN_CART_ITEM_STATUS + "='paid'", null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ItemPurchase itemPurchase = cursorToItemPurchase(cursor);
            items.add(itemPurchase);
            cursor.moveToNext();
        }

        // make sure to close the cursor
        cursor.close();
        return items;
    }

    private ItemPurchase cursorToItemPurchase(Cursor cursor) {
        ItemPurchase itemPurchase = new ItemPurchase();
        itemPurchase.setId(cursor.getLong(0));
        itemPurchase.setItemId(cursor.getString(1));
        itemPurchase.setQty(cursor.getInt(2));
        itemPurchase.setStatus(cursor.getString(3));
        return itemPurchase;
    }
}

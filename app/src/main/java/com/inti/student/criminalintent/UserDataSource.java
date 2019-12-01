package com.inti.student.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;

public class UserDataSource {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_USER_NAME,
            MySQLiteHelper.COLUMN_USER_EMAIL,
            MySQLiteHelper.COLUMN_USER_PASSWORD,
            MySQLiteHelper.COLUMN_USER_ADDRESS};

    public UserDataSource(Context context){
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException{
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public int createUser(String name, String email, String password, String address){
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_USER_NAME, name);
        values.put(MySQLiteHelper.COLUMN_USER_EMAIL, email);
        values.put(MySQLiteHelper.COLUMN_USER_PASSWORD, password);
        values.put(MySQLiteHelper.COLUMN_USER_ADDRESS, address);

        Cursor cursor_item_exist = database.query(MySQLiteHelper.TABLE_USER, allColumns,
                MySQLiteHelper.COLUMN_USER_EMAIL + "='" + email + "'", null, null, null, null);
        cursor_item_exist.moveToFirst();

        if (cursor_item_exist.isAfterLast()) { // if the query returns 0 row
            long rowInserted = database.insert(MySQLiteHelper.TABLE_USER, null, values);
            if(rowInserted != -1) { // if insert is successful
                return 1;
            } else {
                return 0;
            }
        } else {
            return 2;
        }
    }

    public void updateItemPurchase(long cartItemId, int itemQty) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_ID, cartItemId);
        values.put(MySQLiteHelper.COLUMN_CART_ITEM_QTY, itemQty);
        database.update(MySQLiteHelper.TABLE_ITEM_PURCHASE, values, MySQLiteHelper.COLUMN_ID + "='" + cartItemId + "'", null);
    }

    public void deleteItemPurchase(ItemPurchase itemPurchase){
        long id = itemPurchase.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_ITEM_PURCHASE, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public ArrayList<ItemPurchase> getAllItemPurchase(){
        ArrayList<ItemPurchase> comments = new ArrayList<ItemPurchase>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_ITEM_PURCHASE, allColumns,
                null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            ItemPurchase itemPurchase = cursorToItemPurchase(cursor);
            comments.add(itemPurchase);
            cursor.moveToNext();
        }

        // make sure to close the cursor
        cursor.close();
        return comments;
    }

    private ItemPurchase cursorToItemPurchase(Cursor cursor){
        ItemPurchase itemPurchase = new ItemPurchase();
        itemPurchase.setId(cursor.getLong(0));
        itemPurchase.setItemId(cursor.getString(1));
        itemPurchase.setQty(cursor.getInt(2));
        itemPurchase.setStatus(cursor.getString(3));
        return itemPurchase;
    }
}

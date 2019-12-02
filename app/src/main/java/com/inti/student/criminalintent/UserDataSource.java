package com.inti.student.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class UserDataSource {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_USER_NAME,
            MySQLiteHelper.COLUMN_USER_EMAIL,
            MySQLiteHelper.COLUMN_USER_PASSWORD,
            MySQLiteHelper.COLUMN_USER_ADDRESS};
    private Context mContext;

    public UserDataSource(Context context){
        dbHelper = new MySQLiteHelper(context);
        mContext = context;
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
            cursor_item_exist.close();
            if(rowInserted != -1) { // if insert is successful
                return 1;
            } else {
                return 0;
            }
        } else {
            return 2;
        }
    }

    public int getUserLoginDetails(String email, String password){
        //ArrayList<User> users = new ArrayList<User>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_USER, allColumns,
                MySQLiteHelper.COLUMN_USER_EMAIL + "='" + email + "'", null, null, null, null);

        cursor.moveToFirst();
        if (!cursor.isAfterLast()){ // if result is not empty
            User user = cursorToUser(cursor);
            cursor.close();

            if (password.equals(user.getPassword())) {

                // create session for user login
                SharedPreferences.Editor editor = mContext.getSharedPreferences("LoginSession", MODE_PRIVATE).edit();
                editor.putBoolean("login", true); // if user is login
                editor.putLong("userId", user.getId()); // stores user id
                editor.apply();

                return 1;
            } else {
                return 0;
            }
        } else {
            cursor.close();
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

    public ArrayList<User> getAllItemPurchase(){
        ArrayList<User> users = new ArrayList<User>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_ITEM_PURCHASE, allColumns,
                null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            User user = cursorToUser(cursor);
            users.add(user);
            cursor.moveToNext();
        }

        // make sure to close the cursor
        cursor.close();
        return users;
    }

    private User cursorToUser(Cursor cursor){
        User user = new User();
        user.setId(cursor.getLong(0));
        user.setName(cursor.getString(1));
        user.setEmail(cursor.getString(2));
        user.setPassword(cursor.getString(3));
        user.setAddress(cursor.getString(4));
        return user;
    }
}

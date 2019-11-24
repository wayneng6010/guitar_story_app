package com.inti.student.criminalintent;

import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.SQLException;
        import android.database.sqlite.SQLiteDatabase;

        import java.util.ArrayList;
        import java.util.List;

public class ItemPurchaseDataSource {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_CART_ITEM_ID,
            MySQLiteHelper.COLUMN_CART_ITEM_QTY,
            MySQLiteHelper.COLUMN_CART_ITEM_STATUS};

    public ItemPurchaseDataSource(Context context){
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException{
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public ItemPurchase createItemPurchase(String itemID, int itemQty, String itemStatus){
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_CART_ITEM_ID, itemID);
        values.put(MySQLiteHelper.COLUMN_CART_ITEM_QTY, itemQty);
        values.put(MySQLiteHelper.COLUMN_CART_ITEM_STATUS, itemStatus);
        long insertId = database.insert(MySQLiteHelper.TABLE_ITEM_PURCHASE, null, values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_ITEM_PURCHASE, allColumns,
                MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        ItemPurchase newComment = cursorToItemPurchase(cursor);
        cursor.close();
        return newComment;
    }

    public void deleteItemPurchase(ItemPurchase itemPurchase){
        long id = itemPurchase.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_ITEM_PURCHASE, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<ItemPurchase> getAllItemPurchase(){
        List<ItemPurchase> comments = new ArrayList<ItemPurchase>();

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

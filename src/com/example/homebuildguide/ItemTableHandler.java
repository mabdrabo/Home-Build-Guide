package com.example.homebuildguide;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by mahmoud on 8/20/13.
 */
public class ItemTableHandler {

    static DatabaseHandler dbHandler;

    // Items table name
    public static final String TABLE_ITEMS = "items";

    // Items Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_PARENT_ID = "parent_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PRICE = "price";
    private static final String KEY_PAID = "paid";
    private static final String KEY_DEADLINE = "deadline_millis";

    public static String CREATE_ITEMS_TABLE = "CREATE TABLE " + TABLE_ITEMS + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_PARENT_ID + " INTEGER,"
            + KEY_NAME + " TEXT,"
            + KEY_PRICE + " FLOAT,"
            + KEY_PAID + " FLOAT,"
            + KEY_DEADLINE + " INTEGER" + ")";

    public ItemTableHandler(DatabaseHandler databaseHandler) {
        dbHandler = databaseHandler;
    }


    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new Item
    public static void addItem(Item item) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PARENT_ID, item.get_parent_id()); // Item parent_id
        values.put(KEY_NAME, item.get_name()); // Item name
        values.put(KEY_PRICE, item.get_price()); // Item price
        values.put(KEY_PAID, item.get_paid()); // Item paid
        values.put(KEY_DEADLINE, item.get_deadline_calendar().getTimeInMillis()); // Item deadline in millis

        // Inserting Row
        db.insert(TABLE_ITEMS, null, values);
        db.close(); // Closing database connection
    }


    // Getting single Item
    public static Item getItem(int id) {
        if (getItemsCount() == 0)
            return null;

        SQLiteDatabase db = dbHandler.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ITEMS, null, KEY_ID + "=?", new String[] { String.valueOf(id) }, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        if (cursor.getCount() == 0)
            return null;

        Item item = new Item(Integer.parseInt(cursor.getString(1)), cursor.getString(2),
                Float.parseFloat(cursor.getString(3)), Float.parseFloat(cursor.getString(4)), Long.parseLong(cursor.getString(5)) );
        item.set_id(Integer.parseInt(cursor.getString(0)));
        cursor.close();

        return item;
    }


    // Getting All Items
    public static ArrayList<Item> getAllItems() {
        ArrayList<Item> itemList = new ArrayList<Item>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ITEMS;

        SQLiteDatabase db = dbHandler.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.getCount() == 0)
            return itemList;

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Item item = new Item(Integer.parseInt(cursor.getString(1)), cursor.getString(2),
                        Float.parseFloat(cursor.getString(3)), Float.parseFloat(cursor.getString(4)), Long.parseLong(cursor.getString(5)) );
                item.set_id(Integer.parseInt(cursor.getString(0)));

                // Adding Item to list
                itemList.add(item);
            } while (cursor.moveToNext());
        }

        // return Item list
        return itemList;
    }


    public static ArrayList<Item> getTodoItems() {
        ArrayList<Item> itemList = new ArrayList<Item>();
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        Cursor cursor = db.query(TABLE_ITEMS, null, KEY_PRICE + "!=" + KEY_PAID, null, null, null, KEY_DEADLINE + " ASC");

        if (cursor.getCount() == 0)
            return itemList;

        if (cursor.moveToFirst()) {
            do {
                Item item = new Item(Integer.parseInt(cursor.getString(1)), cursor.getString(2),
                        Float.parseFloat(cursor.getString(3)), Float.parseFloat(cursor.getString(4)), Long.parseLong(cursor.getString(5)) );
                item.set_id(Integer.parseInt(cursor.getString(0)));

                // Adding Item to list
                itemList.add(item);
            } while (cursor.moveToNext());
        }

        // return Item list;
        return itemList;
    }

    public static ArrayList<Item> getCategoryItems(int parent_id) {
        ArrayList<Item> allItems = getAllItems();
        ArrayList<Item> filteredItems = new ArrayList<Item>();
        for (Item item : allItems) {
            if (item.get_parent_id() == parent_id) {
                filteredItems.add(item);
            }
        }
        return filteredItems;
    }


    // Updating single Item
    public static int updateItem(Item item) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PARENT_ID, item.get_parent_id());
        values.put(KEY_NAME, item.get_name());
        values.put(KEY_PAID, item.get_paid());
        values.put(KEY_PRICE, item.get_price());
        values.put(KEY_DEADLINE, item.get_deadline_calendar().getTimeInMillis());

        // updating row
        System.out.println("updated_id: " + item.get_id());
        return db.update(TABLE_ITEMS, values, KEY_ID + "=?", new String[] { String.valueOf(item.get_id()) });
    }


    // Deleting single Item
    public static void deleteItem(Item item) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        db.delete(TABLE_ITEMS, KEY_ID + " = ?",
                new String[] { String.valueOf(item.get_id()) });
        db.close();
    }


    public static Calendar getNearestDeadline() {
        int year = 0, month = 0, day = 0;
        ArrayList<Item> allItems = getAllItems();

        if (allItems.size() > 0) {
            Calendar nearestCalendar = allItems.get(0).get_deadline_calendar();

            for (Item item : allItems) {
                nearestCalendar = (item.get_deadline_calendar().before(nearestCalendar))? item.get_deadline_calendar() : nearestCalendar;
            }
            year = nearestCalendar.get(Calendar.YEAR);
            month = nearestCalendar.get(Calendar.MONTH);
            day = nearestCalendar.get(Calendar.DAY_OF_MONTH);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar;
    }


    public static Calendar getFarthestDeadline() {
        int year = 0, month = 0, day = 0;
        ArrayList<Item> allItems = getAllItems();

        if (allItems.size() > 0) {
            Calendar farthestCalendar = allItems.get(0).get_deadline_calendar();

            for (Item item : allItems) {
                farthestCalendar = (farthestCalendar.before(item.get_deadline_calendar()))? item.get_deadline_calendar() : farthestCalendar;
            }
            year = farthestCalendar.get(Calendar.YEAR);
            month = farthestCalendar.get(Calendar.MONTH);
            day = farthestCalendar.get(Calendar.DAY_OF_MONTH);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar;
    }


    public static float getTotalPaid() {
        float totalPaid = 0;
        ArrayList<Item> allItems = getAllItems();
        for (Item item : allItems) {
            totalPaid += item.get_paid();
        }

        return totalPaid;
    }


    public static float getTotalPrice() {
        float totalPrice = 0;
        ArrayList<Item> allItems = getAllItems();
        for (Item item : allItems) {
            totalPrice += item.get_price();
        }

        return totalPrice;
    }


    public static int getItemsDoneCount() {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ITEMS, null, KEY_PRICE + "=" + KEY_PAID, null, null, null, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }


    // Getting Items Count
    public static int getItemsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_ITEMS;
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

}

package com.example.homebuildguide;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by mahmoud on 8/20/13.
 */
public class CategoryTableHandler {

    static DatabaseHandler dbHandler;

    // Categories table name
    public static final String TABLE_CATEGORIES = "categories";

    // Categories Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";

    public static String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_CATEGORIES + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME + " TEXT" + ")";

    public CategoryTableHandler(DatabaseHandler databaseHandler) {
        this.dbHandler = databaseHandler;
    }


    public static void addCategory(Category category) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, category.get_name()); // Category name

        // Inserting Row
        db.insert(TABLE_CATEGORIES, null, values);
        db.close(); // Closing database connection
    }


    public static Category getCategory(int id) {
        if (getCategoriesCount() == 0)
            return null;

        SQLiteDatabase db = dbHandler.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CATEGORIES, null, KEY_ID + "=?", new String[] { String.valueOf(id) }, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        if (cursor.getCount() == 0)
            return null;

        Category category = new Category(cursor.getString(1));
        category.set_id(Integer.parseInt(cursor.getString(0)));
        cursor.close();

        return category;
    }


    // Getting All Items
    public static ArrayList<Category> getAllCategories() {
        ArrayList<Category> categoryList = new ArrayList<Category>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CATEGORIES;

        SQLiteDatabase db = dbHandler.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.getCount() == 0)
            return categoryList;

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Category category = new Category(cursor.getString(1));
                category.set_id(Integer.parseInt(cursor.getString(0)));

                // Adding Item to list
                categoryList.add(category);
            } while (cursor.moveToNext());
        }
        cursor.close();

        // return Item list
        return categoryList;
    }


    // Updating single Category
    public static int updateCategory(Category category) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, category.get_name());

        // updating row
        System.out.println("updated_id: " + category.get_id());
        return db.update(TABLE_CATEGORIES, values, KEY_ID + "=?", new String[] { String.valueOf(category.get_id()) });
    }


    // Deleting single Item
    public static void deleteCategory(Category category) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        db.delete(TABLE_CATEGORIES, KEY_ID + " = ?",
                new String[] { String.valueOf(category.get_id()) });
        db.close();
    }


    // Getting Items Count
    public static int getCategoriesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CATEGORIES;
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

}

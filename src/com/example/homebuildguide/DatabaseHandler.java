package com.example.homebuildguide;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "HomeBuildGuide";

    ItemTableHandler itemTableHandler;
    CategoryTableHandler categoryTableHandler;


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        itemTableHandler  = new ItemTableHandler(this);
        categoryTableHandler = new CategoryTableHandler(this);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ItemTableHandler.CREATE_ITEMS_TABLE);
        db.execSQL(CategoryTableHandler.CREATE_CATEGORIES_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + ItemTableHandler.TABLE_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + CategoryTableHandler.TABLE_CATEGORIES);
        // Create tables again
        onCreate(db);
    }


    public void addItem(Item item) {
        itemTableHandler.addItem(item);
    }

    public Item getItem(int id) {
        return itemTableHandler.getItem(id);
    }

    public ArrayList<Item> getAllItems() {
        return itemTableHandler.getAllItems();
    }

    public ArrayList<Item> getTodoItems() {
        return itemTableHandler.getTodoItems();
    }

    public ArrayList<Item> filterItemsByParent(int parent_id) {
        return itemTableHandler.filterItemsByParent(parent_id);
    }

    public int updateItem(Item item) {
        return itemTableHandler.updateItem(item);
    }

    public void deleteItem(Item item) {
        itemTableHandler.deleteItem(item);
    }

    public Calendar getNearestDeadline() {
        return itemTableHandler.getNearestDeadline();
    }

    public Calendar getFarthestDeadline() {
        return itemTableHandler.getFarthestDeadline();
    }

    public float getTotalPaid() {
        return itemTableHandler.getTotalPaid();
    }

    public float getTotalPrice() {
        return itemTableHandler.getTotalPrice();
    }

    public int getItemsDoneCount() {
        return itemTableHandler.getItemsDoneCount();
    }

    public int getItemsCount() {
        return itemTableHandler.getItemsCount();
    }

    public void addCategory(Category category) {
        categoryTableHandler.addCategory(category);
    }

    public Category getCategory(int id) {
        return categoryTableHandler.getCategory(id);
    }

    public ArrayList<Category> getAllCategories() {
        return categoryTableHandler.getAllCategories();
    }

    public int updateCategory(Category category) {
        return categoryTableHandler.updateCategory(category);
    }

    public void deleteCategory(Category category) {
        categoryTableHandler.deleteCategory(category);
    }

    public int getCategoriesCount() {
        return categoryTableHandler.getCategoriesCount();
    }
}

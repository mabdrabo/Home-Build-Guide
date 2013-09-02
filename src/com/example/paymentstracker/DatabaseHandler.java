package com.example.paymentstracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "HomeBuildGuide";

    ItemTableHandler itemTableHandler;
    CategoryTableHandler categoryTableHandler;
    PaymentTableHandler paymentTableHandler;


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        itemTableHandler = new ItemTableHandler(this);
        categoryTableHandler = new CategoryTableHandler(this);
        paymentTableHandler = new PaymentTableHandler(this);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ItemTableHandler.CREATE_ITEMS_TABLE);
        db.execSQL(CategoryTableHandler.CREATE_CATEGORIES_TABLE);
        db.execSQL(PaymentTableHandler.CREATE_PAYMENTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + ItemTableHandler.TABLE_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + CategoryTableHandler.TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + PaymentTableHandler.CREATE_PAYMENTS_TABLE);
        // Create tables again
        onCreate(db);
    }


    // ITEMS
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

    public ArrayList<Item> getCategoryItems(int parent_id) {
        return itemTableHandler.getCategoryItems(parent_id);
    }

    public int updateItem(Item item) {
        return itemTableHandler.updateItem(item);
    }

    public void deleteItem(Item item) {
        itemTableHandler.deleteItem(item);
        for (Payment payment : getItemPayments(item.get_id()))
            deletePayment(payment);
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


    // CATEGORIES
    public void addCategory(Category category) {
        categoryTableHandler.addCategory(category);
    }

    public Category getCategory(int id) {
        return categoryTableHandler.getCategory(id);
    }

    public float getCategoryPrice(Category category) {
        return categoryTableHandler.getCategoryPrice(category);
    }

    public float getCategoryPaid(Category category) {
        return categoryTableHandler.getCategoryPaid(category);
    }

    public ArrayList<Category> getAllCategories() {
        return categoryTableHandler.getAllCategories();
    }

    public int updateCategory(Category category) {
        return categoryTableHandler.updateCategory(category);
    }

    public void deleteCategory(Category category) {
        categoryTableHandler.deleteCategory(category);
        for (Item item : getCategoryItems(category.get_id()))
            deleteItem(item);
    }

    public int getCategoriesCount() {
        return categoryTableHandler.getCategoriesCount();
    }


    // PAYMENTS
    public void addPayment(Payment payment) {
        paymentTableHandler.addPayment(payment);
    }

    public Payment getPayment(int id) {
        return paymentTableHandler.getPayment(id);
    }

    public ArrayList<Payment> getAllPayments() {
        return paymentTableHandler.getAllPayments();
    }

    public ArrayList<Payment> getItemPayments(int item_id) {
        return paymentTableHandler.getItemPayments(item_id);
    }

    public int updatePayment(Payment payment) {
        return paymentTableHandler.updatePayment(payment);
    }

    public void deletePayment(Payment payment) {
        paymentTableHandler.deletePayment(payment);
    }

    public int getPaymentsCount() {
        return paymentTableHandler.getPaymentsCount();
    }


}

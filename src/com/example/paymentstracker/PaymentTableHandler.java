package com.example.paymentstracker;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by mahmoud on 8/22/13.
 */
public class PaymentTableHandler {

    static DatabaseHandler dbHandler;

    // Categories table name
    public static final String TABLE_PAYMENTS = "payments";

    // Categories Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_ITEM_ID = "item_id";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_DATE = "date";

    public static String CREATE_PAYMENTS_TABLE = "CREATE TABLE " + TABLE_PAYMENTS + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_ITEM_ID + " INTEGER,"
            + KEY_AMOUNT + " FLOAT,"
            + KEY_DATE + " INTEGER" + ")";

    public PaymentTableHandler(DatabaseHandler databaseHandler) {
        this.dbHandler = databaseHandler;
    }


    public static void addPayment(Payment payment) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ITEM_ID, payment.get_item_id()); // Payment item_id
        values.put(KEY_AMOUNT, payment.get_amount()); // Payment amount
        values.put(KEY_DATE, payment.get_date().getTimeInMillis()); // Payment date

        // Inserting Row
        db.insert(TABLE_PAYMENTS, null, values);
        db.close(); // Closing database connection
    }


    public static Payment getPayment(int id) {
        if (getPaymentsCount() == 0)
            return null;

        SQLiteDatabase db = dbHandler.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PAYMENTS, null, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        if (cursor.getCount() == 0)
            return null;

        Payment payment = new Payment(Integer.parseInt(cursor.getString(1)), Float.parseFloat(cursor.getString(2)), Long.parseLong(cursor.getString(3)));
        payment.set_id(Integer.parseInt(cursor.getString(0)));
        cursor.close();

        return payment;
    }


    // Getting All Payments
    public static ArrayList<Payment> getAllPayments() {
        ArrayList<Payment> paymentList = new ArrayList<Payment>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PAYMENTS;

        SQLiteDatabase db = dbHandler.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.getCount() == 0)
            return paymentList;

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Payment payment = new Payment(Integer.parseInt(cursor.getString(1)), Float.parseFloat(cursor.getString(2)), Long.parseLong(cursor.getString(3)));
                payment.set_id(Integer.parseInt(cursor.getString(0)));

                // Adding Payment to list
                paymentList.add(payment);
            } while (cursor.moveToNext());
        }
        cursor.close();

        // return Payment list
        return paymentList;
    }


    // Getting all Payments of given Item
    public ArrayList<Payment> getItemPayments(int item_id) {
        ArrayList<Payment> paymentList = new ArrayList<Payment>();
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        Cursor cursor = db.query(TABLE_PAYMENTS, null, KEY_ITEM_ID + "=" + item_id, null, null, null, KEY_DATE + " DESC");

        if (cursor.getCount() == 0)
            return paymentList;

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Payment payment = new Payment(Integer.parseInt(cursor.getString(1)), Float.parseFloat(cursor.getString(2)), Long.parseLong(cursor.getString(3)));
                payment.set_id(Integer.parseInt(cursor.getString(0)));

                // Adding Payment to list
                paymentList.add(payment);
            } while (cursor.moveToNext());
        }
        cursor.close();

        // return Payment list
        return paymentList;

    }


    // Updating single Payment
    public static int updatePayment(Payment payment) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ITEM_ID, payment.get_item_id()); // Payment item_id
        values.put(KEY_AMOUNT, payment.get_amount()); // Payment amount
        values.put(KEY_DATE, payment.get_date().getTimeInMillis()); // Payment date

        // updating row
        System.out.println("updated_id: " + payment.get_id());
        return db.update(TABLE_PAYMENTS, values, KEY_ID + "=?", new String[]{String.valueOf(payment.get_id())});
    }


    // Deleting single Payment
    public static void deletePayment(Payment payment) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        db.delete(TABLE_PAYMENTS, KEY_ID + " = ?", new String[]{String.valueOf(payment.get_id())});
        db.close();
    }


    // Getting Payments Count
    public static int getPaymentsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_PAYMENTS;
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }


}

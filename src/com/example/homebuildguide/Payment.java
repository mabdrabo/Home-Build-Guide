package com.example.homebuildguide;

import java.util.Calendar;

/**
 * Created by mahmoud on 8/22/13.
 */
public class Payment {

    private  int _id;
    private int _item_id;
    private Calendar _date;
    private float _amount;

    public Payment(int item_id, float amount, long dateMillis) {
        this.set_item_id(item_id);
        this.set_amount(amount);
        _date = Calendar.getInstance();
        _date.setTimeInMillis(dateMillis);
    }


    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int get_item_id() {
        return _item_id;
    }

    public void set_item_id(int _item_id) {
        this._item_id = _item_id;
    }

    public Calendar get_date() {
        return _date;
    }

    public void set_date(Calendar _date) {
        this._date = _date;
    }

    public float get_amount() {
        return _amount;
    }

    public void set_amount(float _amount) {
        this._amount = _amount;
    }

}

package com.example.paymentstracker;

/**
 * Created by mahmoud on 8/20/13.
 */
public class Category {

    private int _id;
    private String _name;

    public Category(String name) {
        this.set_name(name);
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }
}

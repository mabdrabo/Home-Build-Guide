package com.example.homebuildguide;

import java.util.Calendar;

public class Item {

	private int _id;
	private int _parent_id;
	private String _name;
	private float _price;
	private float _paid;
	private Calendar _deadline;
	
	public Item(int parent_id, String name, float price, float paid, long deadlineMillis) {
		this.set_parent_id(parent_id);
		this.set_name(name);
		this.set_price(price);
		this.set_paid(paid);
		_deadline = Calendar.getInstance();
		_deadline.setTimeInMillis(deadlineMillis);
	}

	public Item() {
		// TODO Auto-generated constructor stub
		this.set_parent_id(0);
		this.set_name("Untitled");
		this.set_price(0);
		this.set_paid(0);
		_deadline = Calendar.getInstance();
	}

	public Item(int parent_id) {
		// TODO Auto-generated constructor stub
		this();
		this.set_parent_id(parent_id);
	}
	
	public Calendar get_deadline_calendar() {
		return this._deadline;
	}
	
//	public String get_deadline() {
//		
//		return this._deadline.get(Calendar.YEAR);
//	}
	
//	public void set_deadline(int year, int month, int day) {
//		Calendar calendar = Calendar.getInstance();
//		calendar.set(year, month, day);
//		this._deadline.setTimeInMillis(calendar.getTimeInMillis());
//	}

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

	public float get_price() {
		return _price;
	}

	public void set_price(float _price) {
		this._price = _price;
	}

	public float get_paid() {
		return _paid;
	}

	public void set_paid(float _paid) {
		this._paid = _paid;
	}
	
	public float get_remaining() {
		return this._price - this._paid;
	}

	public int get_parent_id() {
		return _parent_id;
	}

	public void set_parent_id(int _parent_id) {
		this._parent_id = _parent_id;
	}
	
	
}

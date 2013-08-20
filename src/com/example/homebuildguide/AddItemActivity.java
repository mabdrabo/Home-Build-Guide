package com.example.homebuildguide;

import java.util.Calendar;
import java.util.Formatter;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

public class AddItemActivity extends FragmentActivity {

	Button addDateTextViewButton;
	DialogFragment datePicker;
	long deadlineMillis;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_item);
//        setTitle("Add to ");
		
		addDateTextViewButton = (Button) findViewById(R.id.addDateTextViewButton);
		datePicker = new DatePickerFragment();
	}

	
	public void addDateOnClick(View view) {
		datePicker.show(getSupportFragmentManager(), "new_datePicker");
	}

	public void addItemButtonOnClick(View view) {
		String name = "" + ((EditText) findViewById(R.id.addNameEditText)).getText();
		float price = Float.parseFloat("" + ((EditText) findViewById(R.id.addPriceEditText)).getText());

		Item newItem = new Item(MainActivity.MAIN_POSITION, name, price, 0, deadlineMillis);
		MainActivity.database.addItem(newItem);
		
		Intent intent = new Intent().setClass(this, SecondaryActivity.class);
		startActivity(intent);
	}


	@SuppressLint("ValidFragment")
	public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
	
	    @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        // Use the current date as the default date in the picker
	        final Calendar c = Calendar.getInstance();
	        int year = c.get(Calendar.YEAR);
	        int month = c.get(Calendar.MONTH);
	        int day = c.get(Calendar.DAY_OF_MONTH);
	        // Create a new instance of DatePickerDialog and return it
	        return new DatePickerDialog(getActivity(), this, year, month, day);
	    }
	
	    public void onDateSet(DatePicker view, int year, int month, int day) {
	        // Do something with the date chosen by the user
	    	deadlineMillis = MainActivity.getMillis(year, month, day);
	    	Calendar calendar = Calendar.getInstance();
	    	calendar.setTimeInMillis(deadlineMillis);
	    	
	    	Formatter formatter = new Formatter();
	    	addDateTextViewButton.setText(formatter.format("\t %tF \t(Click To Edit)", calendar).toString());
	    	formatter.close();
	    }
	}

}
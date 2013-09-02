package com.example.homebuildguide;

import java.util.Calendar;
import java.util.Formatter;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

public class ItemEditActivity extends FragmentActivity {

	Item selectedItem;
	DialogFragment datePicker;
	
	EditText nameEditText;
	EditText priceEditText;
	EditText paidEditText;
	TextView remainingTextView;
	Button dateTextViewButton;
	CheckBox doneCheckBox;
	
	long deadlineMillis;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_edit);

		// retrieve selected item from the database
		// set values of the UI to that of the retrieved item
		
		selectedItem = MainActivity.database.getItem(MainActivity.SECONDARY_POSITION);
		nameEditText = (EditText) findViewById(R.id.nameEditText);
		priceEditText = (EditText) findViewById(R.id.priceEditText);
		paidEditText = (EditText) findViewById(R.id.paidEditText);
		remainingTextView = (TextView) findViewById(R.id.RemainingTextView);
		dateTextViewButton = (Button) findViewById(R.id.dateTextViewButton);
		doneCheckBox = (CheckBox) findViewById(R.id.doneCheckBox);
		
		nameEditText.setText("" + selectedItem.get_name());
		priceEditText.setText("" + selectedItem.get_price());
		paidEditText.setText("" + selectedItem.get_paid());
		remainingTextView.setText("" + selectedItem.get_remaining());
		doneCheckBox.setChecked((selectedItem.get_remaining() == 0)? true : false);
		
		Formatter formatter = new Formatter();
		dateTextViewButton.setText(formatter.format("\t %tF \t(Click To Edit)", selectedItem.get_deadline_calendar()).toString());
		formatter.close();
		
		datePicker = new DatePickerFragment();
	}
	
	
	protected void onPause() {
		super.onPause();
		
		// update item in database
		String name = "" + nameEditText.getText();
		float price = Float.parseFloat("" + priceEditText.getText());
		float paid = Float.parseFloat("" + paidEditText.getText());
		boolean done = doneCheckBox.isChecked();

		selectedItem.set_name(name);
		selectedItem.set_price(price);
		selectedItem.set_paid((done)? price : paid);
		
	
		MainActivity.database.updateItem(selectedItem);
		remainingTextView.setText("" + selectedItem.get_remaining());
	}
	
	public void dateOnClick(View view) {
		datePicker.show(getSupportFragmentManager(), "details_datePicker");
	}

	
	@SuppressLint("ValidFragment")
	public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
		
	    @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        // Use the current date as the default date in the picker
	        final Calendar c = Calendar.getInstance();
	        c.setTimeInMillis(selectedItem.get_deadline_calendar().getTimeInMillis());
	        int year = c.get(Calendar.YEAR);
	        int month = c.get(Calendar.MONTH);
	        int day = c.get(Calendar.DAY_OF_MONTH);
	        
	        // Create a new instance of DatePickerDialog and return it
	        return new DatePickerDialog(getActivity(), this, year, month, day);
	    }
	
	    public void onDateSet(DatePicker view, int year, int month, int day) {
	        // Do something with the date chosen by the user
	    	deadlineMillis = MainActivity.getMillis(year, month, day);
	    	selectedItem.get_deadline_calendar().setTimeInMillis(deadlineMillis);	
	    	
	    	Formatter formatter = new Formatter();
			dateTextViewButton.setText(formatter.format("\t %tF \t(Click To Edit)", selectedItem.get_deadline_calendar()).toString());
			formatter.close();
	    }
	}
}

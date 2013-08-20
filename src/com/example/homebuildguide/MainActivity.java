package com.example.homebuildguide;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	public static int MAIN_POSITION;
	public static int SECONDARY_POSITION;
	public static DatabaseHandler database;
	
	public static ListView mainListView;
	public static String[] values;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		database = new DatabaseHandler(this);
		
		mainListView = (ListView) findViewById(R.id.mainListView);
		values = new String[] { "To-Do", "Stats", "Floor", "Wall", "Rooms", "Other" };

	    final ArrayList<String> mainList = new ArrayList<String>();
	    for (int i = 0; i < values.length; i++) {
	    	mainList.add(values[i]);
	    }

	    ArrayAdapter<?> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mainList);
	    
        mainListView.setAdapter(adapter);
        
        final Intent secondaryActivityIntent = new Intent().setClass(this, SecondaryActivity.class);
        
        mainListView.setOnItemClickListener(new OnItemClickListener() {
        	  @Override
        	  public void onItemClick(AdapterView<?> parent, View view,
        	    int position, long id) {
        	    Toast.makeText(getApplicationContext(), "Clicked " + mainList.get(position), Toast.LENGTH_SHORT).show();

        	    MAIN_POSITION = position;
        	    startActivity(secondaryActivityIntent);
        	    
        	  }
        });
        
	}
	
	public static long getMillis(int year, int month, int day) {
		Calendar c = Calendar.getInstance();
		c.set(year, month, day);
		return c.getTimeInMillis();
	}

}
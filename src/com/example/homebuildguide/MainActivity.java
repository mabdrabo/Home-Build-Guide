package com.example.homebuildguide;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	public static int MAIN_POSITION;
	public static int SECONDARY_POSITION;
	public static DatabaseHandler database;
	
	public static ListView mainListView;
    public static ArrayList<Category> categoriesList;
    public static ArrayList<String> categoriesListValues;

    private Dialog addCategoryDialog;

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                Toast.makeText(this, "Menu Item 'about' selected", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

        return true;
    }

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		database = new DatabaseHandler(this);
		
		mainListView = (ListView) findViewById(R.id.mainListView);
//		values = new String[] { "To-Do", "Stats", "Floor", "Wall", "Rooms", "Other" };

        final Intent secondaryActivityIntent = new Intent().setClass(this, SecondaryActivity.class);
        
        mainListView.setOnItemClickListener(new OnItemClickListener() {
        	  @Override
        	  public void onItemClick(AdapterView<?> parent, View view,
        	    int position, long id) {
        	    Toast.makeText(getApplicationContext(), "Clicked " + categoriesListValues.get(position), Toast.LENGTH_SHORT).show();

        	    MAIN_POSITION = position;
        	    startActivity(secondaryActivityIntent);
        	  }
        });
        
	}

    protected void onResume() {
        super.onResume();

        categoriesList = database.getAllCategories();
        categoriesListValues = new ArrayList<String>();
        categoriesListValues.add("To-Do");
        categoriesListValues.add("Stats");
        for (Category cat : categoriesList)
            categoriesListValues.add(cat.get_name());

        ArrayAdapter<?> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, categoriesListValues);

        mainListView.setAdapter(adapter);
    }
	
	public static long getMillis(int year, int month, int day) {
		Calendar c = Calendar.getInstance();
		c.set(year, month, day);
		return c.getTimeInMillis();
	}


    public void categoryAddButtonOnClick(View view) {
        addCategoryDialog = new Dialog(this);
        addCategoryDialog.setTitle("Add Category");
        addCategoryDialog.setContentView(R.layout.add_category);
        addCategoryDialog.show();
        Button addCategoryButton = (Button) addCategoryDialog.findViewById(R.id.addCategoryButton);

        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = "" + ((EditText) addCategoryDialog.findViewById(R.id.editTextDialogUserInput)).getText();
                Category category = new Category(name);
                database.addCategory(category);
                addCategoryDialog.dismiss();
                onResume();
            }
        });
    }

}
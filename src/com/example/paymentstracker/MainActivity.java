package com.example.paymentstracker;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends Activity {
	
	public static int MAIN_POSITION;
	public static int SECONDARY_POSITION;
	public static DatabaseHandler database;
	
	public static ListView mainListView;
    public static ArrayList<Category> categories;

    private Dialog addCategoryDialog;

    List<HashMap<String, String>> categoriesList;
    List<HashMap<String, String>> statsList;
    List<HashMap<String, String>> toDoList;

    Menu main_menu;

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        main_menu = menu;
        menu.findItem(R.id.action_main).setVisible(false);
        menu.findItem(R.id.action_todo).setVisible(true);
        menu.findItem(R.id.action_stats).setVisible(true);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_category:
                categoryAddButtonOnClick();
                break;
            case R.id.action_todo:
                toDo();
                break;
            case R.id.action_stats:
                statistics();
                break;
            case R.id.action_main:
                categories();
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
		mainListView = (ListView) findViewById(R.id.listView);
        final Intent secondaryActivityIntent = new Intent().setClass(this, SecondaryActivity.class);
        
        mainListView.setOnItemClickListener(new OnItemClickListener() {
        	  @Override
        	  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        	    Toast.makeText(getApplicationContext(), "Clicked " + categories.get(position).get_name(), Toast.LENGTH_SHORT).show();

        	    MAIN_POSITION = categories.get(position).get_id();
        	    startActivity(secondaryActivityIntent);
        	  }
        });
	}

    protected void onResume() {
        super.onResume();
//        categories();
    }

	public static long getMillis(int year, int month, int day) {
		Calendar c = Calendar.getInstance();
		c.set(year, month, day);
		return c.getTimeInMillis();
	}


    public void categories() {
        setTitle("Categories");
        categories = database.getAllCategories();
        categoriesList = new ArrayList<HashMap<String, String>>();
        Formatter formatter;

        for (Category category : categories) {
            HashMap<String, String> datum = new HashMap<String, String>(2);
            datum.put("main", category.get_name());
            formatter = new Formatter();
            datum.put("sub", formatter.format("Paid %.1f/%.1f", database.getCategoryPaid(category), database.getCategoryPrice(category)).toString());
            formatter.close();
            categoriesList.add(datum);
        }

        SimpleAdapter adapter = new SimpleAdapter(this, categoriesList, android.R.layout.simple_list_item_2,
                new String[]{"main", "sub"},
                new int[]{android.R.id.text1, android.R.id.text2});
        ((ListView) findViewById(R.id.listView)).setAdapter(adapter);

        main_menu.findItem(R.id.action_main).setVisible(false);
        main_menu.findItem(R.id.action_stats).setVisible(true);
        main_menu.findItem(R.id.action_todo).setVisible(true);
    }

    public void categoryAddButtonOnClick() {
        addCategoryDialog = new Dialog(this);
        addCategoryDialog.setTitle("Add Category");
        addCategoryDialog.setContentView(R.layout.dialog_add_category);
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


    private void statistics() {
        setTitle("Statistics");
        String COUNT_PERCENT = "Finished %.1f%% of %d Items";  // doneItemsPercent, itemsCount
        String REMAINING_COUNT = "Remaining %d items";  // remainingItemsCount
        String PAID_PERCENT = "Paid for %.1f%% of total %.2f";  // totalPaidPercent, totalPrice
        String REMAINING_PAID = "Remaining %.2f to pay";  // remainingPrice
        String NEAREST_DEADLINE = "Nearest Deadline is %tF";  // nearestCalendar
        String FARTHEST_DEADLINE = "Farthest Deadline is %tF";  // farthestCalendar

        int itemsCount = MainActivity.database.getItemsCount();
        int doneItemsCount = MainActivity.database.getItemsDoneCount();
        int remainingItemsCount = itemsCount - doneItemsCount;
        float doneItemsPercent = (itemsCount > 0) ? doneItemsCount * 100 / itemsCount : 0;

        float totalPrice = MainActivity.database.getTotalPrice();
        float totalPaid = MainActivity.database.getTotalPaid();
        float remainingPrice = totalPrice - totalPaid;
        float totalPaidPercent = (totalPaid > 0) ? totalPaid * 100 / totalPrice : 0;

        Calendar nearestDeadline = MainActivity.database.getNearestDeadline();
        Calendar farthestDeadline = MainActivity.database.getFarthestDeadline();

        statsList = new ArrayList<HashMap<String, String>>();
        Formatter formatter;

        HashMap<String, String> datum1 = new HashMap<String, String>(2);
        formatter = new Formatter();
        datum1.put("main", formatter.format(COUNT_PERCENT, doneItemsPercent, itemsCount).toString());
        formatter.close();
        formatter = new Formatter();
        datum1.put("sub", formatter.format(REMAINING_COUNT, remainingItemsCount).toString());
        formatter.close();
        statsList.add(datum1);

        HashMap<String, String> datum2 = new HashMap<String, String>(2);
        formatter = new Formatter();
        datum2.put("main", formatter.format(PAID_PERCENT, totalPaidPercent, totalPrice).toString());
        formatter.close();
        formatter = new Formatter();
        datum2.put("sub", formatter.format(REMAINING_PAID, remainingPrice).toString());
        formatter.close();
        statsList.add(datum2);

        HashMap<String, String> datum3 = new HashMap<String, String>(2);
        formatter = new Formatter();
        datum3.put("main", formatter.format(NEAREST_DEADLINE, nearestDeadline).toString());
        formatter.close();
        formatter = new Formatter();
        datum3.put("sub", formatter.format(FARTHEST_DEADLINE, farthestDeadline).toString());
        formatter.close();
        statsList.add(datum3);

        SimpleAdapter adapter = new SimpleAdapter(this, statsList, android.R.layout.simple_list_item_2,
                new String[]{"main", "sub"},
                new int[]{android.R.id.text1, android.R.id.text2});
        ((ListView) findViewById(R.id.listView)).setAdapter(adapter);

        main_menu.findItem(R.id.action_stats).setVisible(false);
        main_menu.findItem(R.id.action_main).setVisible(true);
        main_menu.findItem(R.id.action_todo).setVisible(true);
    }


    private void toDo() {
        setTitle("To-Do");
        ArrayList<Item> toDoItems = MainActivity.database.getTodoItems();
        toDoList = new ArrayList<HashMap<String, String>>();
        Formatter formatter;
        for (Item item : toDoItems) {
            HashMap<String, String> datum = new HashMap<String, String>(2);
            datum.put("main", item.get_name());
            formatter = new Formatter();
            datum.put("sub", formatter.format("%tF", item.get_deadline_calendar()).toString());
            formatter.close();
            toDoList.add(datum);
        }

        SimpleAdapter adapter = new SimpleAdapter(this, toDoList, android.R.layout.simple_list_item_2,
                new String[]{"main", "sub"},
                new int[]{android.R.id.text1, android.R.id.text2});
        ((ListView) findViewById(R.id.listView)).setAdapter(adapter);

        main_menu.findItem(R.id.action_todo).setVisible(false);
        main_menu.findItem(R.id.action_stats).setVisible(true);
        main_menu.findItem(R.id.action_main).setVisible(true);
    }

}
package com.example.homebuildguide;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class SecondaryActivity extends Activity {
	
	Intent ItemDetailsActivityIntent;
	
	ListView secondaryListview;
	ArrayList<Item> secondaryItemsList;
	Button addButton;
	
	List<HashMap<String, String>> statsList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		switch (MainActivity.MAIN_POSITION) {
		case 0:  // the To-do
			toDo();
			break;
			
		case 1:  // Stats
			statistics();
			break;

		default:
			setContentView(R.layout.activity_secondary);
			addButton = (Button) findViewById(R.id.createSecondaryItembutton);
			addButton.setText("Add Item to " + MainActivity.values[MainActivity.MAIN_POSITION]);
			secondaryItemsList = MainActivity.database.filterItemsByParent(MainActivity.MAIN_POSITION);
			base();
			
		    break;
		}
	}

	
	private void base() {
		secondaryListview = (ListView) findViewById(R.id.secondaryListView);
	    System.out.println("mm " + secondaryItemsList.size());
	    
	    final ArrayList<String> secondaryItemsListValues = new ArrayList<String>();
	    for (int i = 0; i < secondaryItemsList.size(); i++) {
	    	secondaryItemsListValues.add(secondaryItemsList.get(i).get_name());
	    }
	    
	    ArrayAdapter<?> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, secondaryItemsListValues);
	    secondaryListview.setAdapter(adapter);

	    ItemDetailsActivityIntent = new Intent().setClass(this, ItemDetailsActivity.class);
        
	    secondaryListview.setOnItemClickListener(new OnItemClickListener() {
        	  @Override
        	  public void onItemClick(AdapterView<?> parent, View view,
        	    int position, long id) {
        	    Toast.makeText(getApplicationContext(), "Clicked " + secondaryItemsListValues.get(position), Toast.LENGTH_SHORT).show();

        	    MainActivity.SECONDARY_POSITION = secondaryItemsList.get(position).get_id();
        	    startActivity(ItemDetailsActivityIntent);
        	    
        	  }
        	});
	}
	
	
	@SuppressWarnings("unchecked")
	private void statistics() {
		setContentView(R.layout.statistics_view);
		
		String COUNT_PERCENT = "Finished %.1f%% of %d Items";  // doneItemsPercent, itemsCount
		String REMAINING_COUNT = "Remaining %d items";  // remainingItemsCount
		String PAID_PERCENT = "Paid for %.1f%% of total %.2f";  // totalPaidPercent, totalPrice
		String REMAINING_PAID = "Remaining %.2f to pay";  // remainingPrice
		String NEAREST_DEADLINE = "Nearest Deadline is %tF";  // nearestCalendar
		String FARTHEST_DEADLINE = "Farthest Deadline is %tF";  // farthestCalendar
		
		int itemsCount = MainActivity.database.getItemsCount();
		int doneItemsCount = MainActivity.database.getItemsDoneCount();
		int remainingItemsCount = itemsCount - doneItemsCount;
		float doneItemsPercent = (itemsCount > 0)? doneItemsCount * 100 / itemsCount : 0;
		
		float totalPrice = MainActivity.database.getTotalPrice();
		float totalPaid = MainActivity.database.getTotalPaid();
		float remainingPrice = totalPrice - totalPaid;
		float totalPaidPercent = (totalPaid > 0)? totalPaid * 100 / totalPrice : 0;
		
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
		                                          new String[] {"main", "sub"},
		                                          new int[] {android.R.id.text1, android.R.id.text2} );
		((ListView) findViewById(R.id.statsListView)).setAdapter(adapter);
		
	}
	
	
	private void toDo() {
		setContentView(R.layout.activity_secondary);
		secondaryItemsList = MainActivity.database.getTodoItems();
		base();
		addButton = (Button) findViewById(R.id.createSecondaryItembutton);
		addButton.setVisibility(View.GONE);
	}
	
	
	public void createSecondaryItembuttonOnClick(View view) {
		Intent intent = new Intent().setClass(this, AddItemActivity.class);
		startActivity(intent);
	}

}
package com.example.paymentstracker;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;

public class SecondaryActivity extends FragmentActivity {

    Intent ItemDetailsActivityIntent;
    Category selected_category;

    ListView secondaryListview;
    ArrayList<Item> secondaryItems;
    List<HashMap<String, String>> secondaryItemsList;

    Button addItemButton;
    Dialog addItemDialog;
    long deadlineMillis;


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.category, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_item:
                addItemDialog();
                break;
            case R.id.action_edit_category:
                break;
            case R.id.action_delete_category:
                MainActivity.database.deleteCategory(selected_category);
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_main);
        selected_category = MainActivity.database.getCategory(MainActivity.MAIN_POSITION);
        setTitle(selected_category.get_name());
        secondaryItems = MainActivity.database.getCategoryItems(selected_category.get_id());

        secondaryListview = (ListView) findViewById(R.id.listView);
        secondaryItemsList = new ArrayList<HashMap<String, String>>();
        Formatter formatter;
        for (Item item : secondaryItems) {
            HashMap<String, String> datum = new HashMap<String, String>(2);
            datum.put("main", item.get_name());
            formatter = new Formatter();
            datum.put("sub", formatter.format("deadline %tF -- paid %.1f/%.1f", item.get_deadline_calendar(), item.get_paid(), item.get_price()).toString());
            formatter.close();
            secondaryItemsList.add(datum);
        }

        SimpleAdapter adapter = new SimpleAdapter(this, secondaryItemsList, android.R.layout.simple_list_item_2,
                new String[]{"main", "sub"},
                new int[]{android.R.id.text1, android.R.id.text2});
        ((ListView) findViewById(R.id.listView)).setAdapter(adapter);

        ItemDetailsActivityIntent = new Intent().setClass(this, ItemDetailsActivity.class);

        secondaryListview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getApplicationContext(), "Clicked " + secondaryItems.get(position).get_name(), Toast.LENGTH_SHORT).show();

                MainActivity.SECONDARY_POSITION = secondaryItems.get(position).get_id();
                startActivity(ItemDetailsActivityIntent);
            }
        });
    }


    public void addItemDialog() {
        addItemDialog = new Dialog(this);
        addItemDialog.setContentView(R.layout.dialog_add_item);
        addItemDialog.setTitle("Add Item");
        addItemDialog.show();
        addItemButton = (Button) addItemDialog.findViewById(R.id.addItemButton);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemButtonOnClick(v);
            }
        });
    }

    public void addItemButtonOnClick(View view) {
        String name = "" + ((EditText) addItemDialog.findViewById(R.id.addNameEditText)).getText();
        float price = Float.parseFloat("" + ((EditText) addItemDialog.findViewById(R.id.addPriceEditText)).getText());

        DatePicker calendar = (DatePicker) addItemDialog.findViewById(R.id.datePicker);
        deadlineMillis = MainActivity.getMillis(calendar.getYear(), calendar.getMonth(), calendar.getDayOfMonth());

        Item newItem = new Item(selected_category.get_id(), name, price, 0, deadlineMillis);
        MainActivity.database.addItem(newItem);

        addItemDialog.dismiss();
        onResume();
    }

}

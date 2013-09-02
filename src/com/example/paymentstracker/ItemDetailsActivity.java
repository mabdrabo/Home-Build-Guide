package com.example.paymentstracker;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;

/**
 * Created by mahmoud on 8/22/13.
 */
public class ItemDetailsActivity extends Activity {

    TextView itemNameTextView;
    TextView deadlineTextView;
    TextView moneyTextView;
    ProgressBar progressBar;
    ListView listView;

    Item selectedItem;
    ArrayList<Payment> itemPayments;
    ArrayList<HashMap<String,String>> paymentsList;


    private Dialog addPaymentDialog;

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.item_details, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_payment:
                addPaymentDialog();
                break;
            case R.id.action_edit_item:
                Intent intent = new Intent().setClass(this, ItemEditActivity.class);
                startActivity(intent);
                break;
            case R.id.action_delete_item:
                MainActivity.database.deleteItem(selectedItem);
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        setTitle("Item Details");

        itemNameTextView = (TextView) findViewById(R.id.itemNameTextView);
        deadlineTextView = (TextView) findViewById(R.id.deadlineTextView);
        moneyTextView = (TextView) findViewById(R.id.moneyTextView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        listView = (ListView) findViewById(R.id.listView);

        onResume();
    }

    protected void onResume() {
        super.onResume();

        Formatter formatter;

        selectedItem = MainActivity.database.getItem(MainActivity.SECONDARY_POSITION);
        itemPayments = MainActivity.database.getItemPayments(selectedItem.get_id());

        itemNameTextView.setText(selectedItem.get_name());
        formatter = new Formatter();
        deadlineTextView.setText(formatter.format("%tF", selectedItem.get_deadline_calendar()).toString());
        formatter.close();

        paymentsList = new ArrayList<HashMap<String, String>>();
        float paid = 0;
        for (Payment payment : itemPayments) {
            paid += payment.get_amount();
            HashMap<String, String> datum = new HashMap<String, String>(2);
            formatter = new Formatter();
            datum.put("main", "Amount " + payment.get_amount());
            formatter.close();
            formatter = new Formatter();
            datum.put("sub", formatter.format("Date %tF", payment.get_date()).toString());
            formatter.close();
            paymentsList.add(datum);
        }

        selectedItem.set_paid(paid);
        MainActivity.database.updateItem(selectedItem);
        float price = selectedItem.get_price();
        int percent = (int) ((paid * 100) / price);
        formatter = new Formatter();
        moneyTextView.setText(formatter.format("%.1f / %.1f  (%d%%)", paid, price, percent).toString());
        formatter.close();

        progressBar.setProgress(percent);

        SimpleAdapter adapter = new SimpleAdapter(this, paymentsList, android.R.layout.simple_list_item_2,
                new String[]{"main", "sub"},
                new int[]{android.R.id.text1, android.R.id.text2});
        ((ListView) findViewById(R.id.listView)).setAdapter(adapter);
    }


    private void addPaymentDialog() {
        addPaymentDialog = new Dialog(this);
        addPaymentDialog.setTitle("Add Category");
        addPaymentDialog.setContentView(R.layout.dialog_add_payment);
        addPaymentDialog.show();
        Button addPaymentButton = (Button) addPaymentDialog.findViewById(R.id.addPaymentButton);

        addPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float amount = Float.parseFloat("" + ((EditText) addPaymentDialog.findViewById(R.id.paymentAmountEditText)).getText());
                DatePicker calendar = (DatePicker) addPaymentDialog.findViewById(R.id.paymentDateDatePicker);
                long dateMillis = MainActivity.getMillis(calendar.getYear(), calendar.getMonth(), calendar.getDayOfMonth());

                if(selectedItem.get_remaining() > amount) {
                    Payment payment = new Payment(selectedItem.get_id(), amount, dateMillis);
                    MainActivity.database.addPayment(payment);
                    selectedItem.set_paid(selectedItem.get_paid() + amount);
                    MainActivity.database.updateItem(selectedItem);
                } else {
                    Toast.makeText(getApplicationContext(), "what's remaining is Not that much :)", Toast.LENGTH_LONG).show();
                }

                addPaymentDialog.dismiss();
                onResume();
            }
        });
    }

}
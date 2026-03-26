package com.example.loyaltyfirst;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;


public class MainActivity4 extends AppCompatActivity {
    protected void fetchAllTransactionsByUser(String userId) {
        String url = "http://10.0.2.2:8080/loyaltyfirst/Transactions.jsp?cid="+userId;
        RequestQueue queue = Volley.newRequestQueue(MainActivity4.this);

        StringRequest req = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                ArrayList<String> spinnerData = new ArrayList<>();
                spinnerData.add(0, "Select an item");

                Spinner spinner = findViewById(R.id.spinner);

                s = s.trim();
                String[] rows = s.split("#");

                for (String row : rows) {
                    String t_ref = row.split(",")[0];

                    spinnerData.add(t_ref);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity4.this, android.R.layout.simple_spinner_item, spinnerData);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity4.this, "Failed to load transactions", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(req);
    }

    protected void fetchTransactionDetails(String t_id) {
        String url = "http://10.0.2.2:8080/loyaltyfirst/TransactionDetails.jsp?tref="+t_id;
        RequestQueue queue = Volley.newRequestQueue(MainActivity4.this);

        StringRequest req = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                TableLayout tableLayout = findViewById(R.id.tableLayoutPrizeDetail);
                TextView textViewDate = findViewById(R.id.textView9);
                TextView textViewPoints = findViewById(R.id.textView10);

                s = s.trim();
                String[] rows = s.split("#");

                textViewDate.setText(rows[0].split(",")[0].split(" ")[0]);
                textViewPoints.setText(rows[0].split(",")[1] + " points");

                TableRow headerRow = new TableRow(MainActivity4.this);
                String[] headers = {"Prod. Name", "Quantity", "Points"};

                for (String header : headers) {
                    TextView headerTextView = new TextView(MainActivity4.this);
                    headerTextView.setText(header);
                    headerTextView.setPadding(20, 8, 60, 8); // Adjust padding as needed
                    headerTextView.setTypeface(null, Typeface.BOLD);

                    // Add the header TextView to the header TableRow
                    headerRow.addView(headerTextView);
                }

                // Add the header TableRow to the TableLayout as the first row
                tableLayout.addView(headerRow);

                for (String row : rows) {
                    String[] columns = row.split(",");

                    TableRow dataRow = new TableRow(MainActivity4.this);

                    for (int i = 2; i < columns.length; i++) {
                        String column = columns[i].trim();
                        TextView textView = new TextView(MainActivity4.this);
                        textView.setText(column);
                        textView.setPadding(20, 8, 60, 8); // Adjust padding as needed

                        // Add the TextView to the data TableRow
                        dataRow.addView(textView);
                    }

                    // Add the data TableRow to the TableLayout
                    tableLayout.addView(dataRow);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity4.this, "Failed to load transactions", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(req);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        Intent intent = getIntent();
        String userId = intent.getStringExtra("userId");

        getActionBar().setDisplayHomeAsUpEnabled(true);

        fetchAllTransactionsByUser(userId);

        Spinner spinner = findViewById(R.id.spinner);
        TextView textViewDate = findViewById(R.id.textView9);
        TextView textViewPoints = findViewById(R.id.textView10);
        TableLayout tableLayout = findViewById(R.id.tableLayoutPrizeDetail);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tableLayout.removeAllViews();
                if (position == 0) {
                    // If "Select an item" is selected, hide TextViews and Table
                    textViewDate.setVisibility(View.GONE);
                    textViewPoints.setVisibility(View.GONE);
                    tableLayout.setVisibility(View.GONE);

                    textViewDate.setText("");
                    textViewPoints.setText("");
                } else {
                    // Show TextViews and Table for other selected items
                    textViewDate.setVisibility(View.VISIBLE);
                    textViewPoints.setVisibility(View.VISIBLE);
                    tableLayout.setVisibility(View.VISIBLE);


                    String selectedItemValue = parent.getItemAtPosition(position).toString();

                    fetchTransactionDetails(selectedItemValue.split(",")[0]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case where no item is selected if needed
            }
        });
    }
}
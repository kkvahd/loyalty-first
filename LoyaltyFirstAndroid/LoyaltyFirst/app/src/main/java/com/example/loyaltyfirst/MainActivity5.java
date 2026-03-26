package com.example.loyaltyfirst;

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

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity5 extends AppCompatActivity {

    protected void fetchAllProductIdsByUser(String userId) {
        String url = "http://10.0.2.2:8080/loyaltyfirst/PrizeIds.jsp?cid="+userId;
        RequestQueue queue = Volley.newRequestQueue(MainActivity5.this);

        StringRequest req = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                ArrayList<String> spinnerData = new ArrayList<>();
                spinnerData.add(0, "Select an item");

                Spinner spinner = findViewById(R.id.spinner4);

                s = s.trim();
                String[] rows = s.split("#");

                for (String row : rows) {
                    spinnerData.add(row);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity5.this, android.R.layout.simple_spinner_item, spinnerData);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity5.this, "Failed to load prizes", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(req);
    }

    protected void fetchPrizeDetails(String prize_id, String c_id) {
        String url = "http://10.0.2.2:8080/loyaltyfirst/RedemptionDetails.jsp?prizeid="+prize_id+"&cid="+c_id;
        RequestQueue queue = Volley.newRequestQueue(MainActivity5.this);

        StringRequest req = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                TableLayout tableLayout = findViewById(R.id.tableLayoutPrizeDetail);
                TextView textViewPrizeDesc = findViewById(R.id.textView16);
                TextView textViewPointsNeeded = findViewById(R.id.textView19);

                s = s.trim();
                String[] rows = s.split("#");

                textViewPrizeDesc.setText(rows[0].split(",")[0]);
                textViewPointsNeeded.setText(rows[0].split(",")[1]);

                TableRow headerRow = new TableRow(MainActivity5.this);
                String[] headers = {"Redemption Date", "Exchange Center"};

                for (String header : headers) {
                    TextView headerTextView = new TextView(MainActivity5.this);
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

                    TableRow dataRow = new TableRow(MainActivity5.this);

                    for (int i = 2; i < columns.length; i++) {
                        String column = columns[i].trim();

                        if (column.contains(":"))
                            column = column.split(" ")[0];

                        TextView textView = new TextView(MainActivity5.this);
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
                Toast.makeText(MainActivity5.this, "Failed to load transactions", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(req);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        Intent intent = getIntent();
        String userId = intent.getStringExtra("userId");

        fetchAllProductIdsByUser(userId);

        Spinner spinner = findViewById(R.id.spinner4);
        TextView textViewPrizeDesc = findViewById(R.id.textView16);
        TextView textViewPointsNeeded = findViewById(R.id.textView19);
        TableLayout tableLayout = findViewById(R.id.tableLayoutPrizeDetail);

        TextView textViewPrizeDescLabel = findViewById(R.id.textView15);
        TextView textViewPointsNeededLabel = findViewById(R.id.textView18);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tableLayout.removeAllViews();
                if (position == 0) {
                    // If "Select an item" is selected, hide TextViews and Table
                    textViewPrizeDesc.setVisibility(View.GONE);
                    textViewPointsNeeded.setVisibility(View.GONE);
                    tableLayout.setVisibility(View.GONE);
                    textViewPrizeDescLabel.setVisibility(View.GONE);
                    textViewPointsNeededLabel.setVisibility(View.GONE);

                    textViewPrizeDesc.setText("");
                    textViewPointsNeeded.setText("");
                } else {
                    // Show TextViews and Table for other selected items
                    textViewPrizeDesc.setVisibility(View.VISIBLE);
                    textViewPointsNeeded.setVisibility(View.VISIBLE);
                    tableLayout.setVisibility(View.VISIBLE);
                    textViewPrizeDescLabel.setVisibility(View.VISIBLE);
                    textViewPointsNeededLabel.setVisibility(View.VISIBLE);

                    String selectedItemValue = parent.getItemAtPosition(position).toString();

                    fetchPrizeDetails(selectedItemValue, userId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case where no item is selected if needed
            }
        });
    }
}
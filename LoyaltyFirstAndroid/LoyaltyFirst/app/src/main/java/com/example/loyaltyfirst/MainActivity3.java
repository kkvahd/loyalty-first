package com.example.loyaltyfirst;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
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

public class MainActivity3 extends AppCompatActivity {

    protected void fetchAllTransactionsByUser(String userId) {
        String url = "http://10.0.2.2:8080/loyaltyfirst/Transactions.jsp?cid="+userId;
        RequestQueue queue = Volley.newRequestQueue(MainActivity3.this);

        StringRequest req = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                String[] rows = s.split("#");
                TableLayout tableLayout = findViewById(R.id.tableLayoutPrizeDetail);

                // Create a TableRow for headers
                TableRow headerRow = new TableRow(MainActivity3.this);
                String[] headers = {"TXN Ref", "Date", "Points", "Total"};

                for (String header : headers) {
                    TextView headerTextView = new TextView(MainActivity3.this);
                    headerTextView.setText(header);
                    headerTextView.setPadding(40, 8, 60, 8); // Adjust padding as needed
                    headerTextView.setTypeface(null, Typeface.BOLD);

                    // Add the header TextView to the header TableRow
                    headerRow.addView(headerTextView);
                }

                // Add the header TableRow to the TableLayout as the first row
                tableLayout.addView(headerRow);

                for (String row : rows) {
                    String[] columns = row.split(",");

                    TableRow dataRow = new TableRow(MainActivity3.this);

                    for (int i = 0; i < columns.length; i++) {
                        String column = columns[i].trim();

                        if (column.contains(":")) {
                            column = column.split(" ")[0];
                        }

                        TextView textView = new TextView(MainActivity3.this);

                        // Check if this is the last column in the row
                        if (i == columns.length - 1 && i > 0) {
                            // Assuming the last column needs a dollar sign added
                            column = "$" + column;
                        }

                        textView.setText(column);
                        textView.setPadding(40, 8, 60, 8); // Adjust padding as needed

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
                Toast.makeText(MainActivity3.this, "Failed to load transactions", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(req);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        Intent intent = getIntent();
        String userId = intent.getStringExtra("userId");

        fetchAllTransactionsByUser(userId);
    }
}
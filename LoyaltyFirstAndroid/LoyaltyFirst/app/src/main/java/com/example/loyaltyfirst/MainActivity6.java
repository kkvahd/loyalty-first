package com.example.loyaltyfirst;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class MainActivity6 extends AppCompatActivity {

    protected void fetchAllTransactionsByUser(String userId) {
        String url = "http://10.0.2.2:8080/loyaltyfirst/Transactions.jsp?cid="+userId;
        RequestQueue queue = Volley.newRequestQueue(MainActivity6.this);

        StringRequest req = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                ArrayList<String> spinnerData = new ArrayList<>();
                spinnerData.add(0, "Select an item");

                Spinner spinner = findViewById(R.id.spinner2);

                s = s.trim();
                String[] rows = s.split("#");

                for (String row : rows) {
                    String t_ref = row.split(",")[0];

                    spinnerData.add(t_ref);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity6.this, android.R.layout.simple_spinner_item, spinnerData);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity6.this, "Failed to load transactions", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(req);
    }

    protected void fetchSupportFamily(String cid, String t_id) {
        String url = "http://10.0.2.2:8080/loyaltyfirst/SupportFamilyIncrease.jsp?cid=" + cid + "&tref=" + t_id;
        RequestQueue queue = Volley.newRequestQueue(MainActivity6.this);

        StringRequest req = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                TextView textViewTxnPoints = findViewById(R.id.textViewTxnPoints);
                TextView textViewFamilyId = findViewById(R.id.textViewFamilyId);
                TextView textViewFamilyPercent = findViewById(R.id.textViewFamilyPercent);

                s = s.trim();
                String data = s.split("#")[0];

                textViewFamilyId.setText(data.split(",")[0]);
                textViewTxnPoints.setText(data.split(",")[2]);
                textViewFamilyPercent.setText(data.split(",")[1]);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity6.this, "Failed to load transactions", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(req);
    }

    protected void fetchFamilyIncrease(String cid, String fid, String n_points) {
        String url = "http://10.0.2.2:8080/loyaltyfirst/FamilyIncrease.jsp?fid=" + fid + "&cid=" + cid + "&npoints=" + n_points;
        RequestQueue queue = Volley.newRequestQueue(MainActivity6.this);

        StringRequest req = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                s = s.trim();
                Toast.makeText(MainActivity6.this, s, Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity6.this, "Failed add points", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(req);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);

        Intent intent = getIntent();
        String userId = intent.getStringExtra("userId");

        fetchAllTransactionsByUser(userId);


        Spinner spinner = findViewById(R.id.spinner2);

        TextView textViewTxnPoints = findViewById(R.id.textViewTxnPoints);
        TextView textViewFamilyId = findViewById(R.id.textViewFamilyId);
        TextView textViewFamilyPercent = findViewById(R.id.textViewFamilyPercent);

        TextView textViewTxnPointsLabel = findViewById(R.id.textViewTxnPointsLabel);
        TextView textViewFamilyIdLabel = findViewById(R.id.textViewFamilyIdLabel);
        TextView textViewFamilyPercentLabel = findViewById(R.id.textViewFamilyPercentLabel);

        Button buttonAddFamilyPoints = findViewById(R.id.buttonAddFamilyPoints);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    // If "Select an item" is selected, hide TextViews and Table
                    textViewTxnPoints.setVisibility(View.GONE);
                    textViewFamilyId.setVisibility(View.GONE);
                    textViewFamilyPercent.setVisibility(View.GONE);

                    textViewTxnPointsLabel.setVisibility(View.GONE);
                    textViewFamilyIdLabel.setVisibility(View.GONE);
                    textViewFamilyPercentLabel.setVisibility(View.GONE);

                    buttonAddFamilyPoints.setVisibility(View.GONE);

                    textViewTxnPoints.setText("");
                    textViewFamilyId.setText("");
                    textViewFamilyPercent.setText("");
                } else {
                    // Show TextViews and Table for other selected items
                    textViewTxnPoints.setVisibility(View.VISIBLE);
                    textViewFamilyId.setVisibility(View.VISIBLE);
                    textViewFamilyPercent.setVisibility(View.VISIBLE);

                    textViewTxnPointsLabel.setVisibility(View.VISIBLE);
                    textViewFamilyIdLabel.setVisibility(View.VISIBLE);
                    textViewFamilyPercentLabel.setVisibility(View.VISIBLE);

                    buttonAddFamilyPoints.setVisibility(View.VISIBLE);

                    String selectedItemValue = parent.getItemAtPosition(position).toString();

                    fetchSupportFamily(userId, selectedItemValue);

                    buttonAddFamilyPoints.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String fid = textViewFamilyId.getText().toString();
                            String points = textViewTxnPoints.getText().toString();
                            String percent = textViewFamilyPercent.getText().toString();

                            String points_str = Integer.toString(Integer.parseInt(points) * Integer.parseInt(percent) / 100);

                            fetchFamilyIncrease(userId, fid, points_str);
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case where no item is selected if needed
            }
        });
    }
}
package com.example.loyaltyfirst;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity2 extends AppCompatActivity {

    private void setUserImage(String userId) {
        String imageUrl = "http://10.0.2.2:8080/loyaltyfirst/images/"+userId+".jpg";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        ImageView imageView = findViewById(R.id.imageView);

        ImageRequest imageRequest = new ImageRequest(imageUrl, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);
            }
        }, 0, 0, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(imageRequest);
    }

    private void setUserInfo(String userId) {
        String imageUrl = "http://10.0.2.2:8080/loyaltyfirst/Info.jsp?cid="+userId;
        RequestQueue queue = Volley.newRequestQueue(MainActivity2.this);

        StringRequest req = new StringRequest(Request.Method.GET, imageUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                TextView textViewUserName = findViewById(R.id.textViewUsernameActivity2);
                TextView textViewPoints = findViewById(R.id.textViewPoints);

                s = s.trim();
                String userName = s.split(",")[0];
                String points = s.split(",")[1].split("#")[0];

                textViewUserName.setText(userName);
                textViewPoints.setText(points);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity2.this, "Failed to get user data!", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(req);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        TextView textView = findViewById(R.id.textView);
        Button allTransactions = findViewById(R.id.buttonAllTxns);
        Button transactionDetails = findViewById(R.id.button2);
        Button redemptionDetails = findViewById(R.id.button3);
        Button addToFamily = findViewById(R.id.button4);
        Button exit = findViewById(R.id.button5);

        Intent intent = getIntent();
        String userId = intent.getStringExtra("userId");

        setUserImage(userId);
        setUserInfo(userId);

        allTransactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, MainActivity3.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        transactionDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, MainActivity4.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        redemptionDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, MainActivity5.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        addToFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, MainActivity6.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
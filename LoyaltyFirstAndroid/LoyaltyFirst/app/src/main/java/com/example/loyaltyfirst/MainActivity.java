package com.example.loyaltyfirst;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textViewUsername = findViewById(R.id.textViewUsername);
        TextView textViewPassword = findViewById(R.id.textViewPassword);

        EditText editTextUsername = findViewById(R.id.editTextUsername);
        EditText editTextPassword = findViewById(R.id.editTextPassword);

        Button buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();

                if (username.equals("") || password.equals(""))
                    Toast.makeText(MainActivity.this, "All fields are mandatory", Toast.LENGTH_LONG).show();

                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                String loginUrl = "http://10.0.2.2:8080/loyaltyfirst/login?user="+username+"&pass="+password;

                StringRequest loginRequest = new StringRequest(Request.Method.GET, loginUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (s.toLowerCase().contains("yes")) {
                            Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                            intent.putExtra("userId", s.split(":")[1]);
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this, "Invalid username/password", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(MainActivity.this, "Failed to login!", Toast.LENGTH_LONG).show();
                    }
                });
                queue.add(loginRequest);
            }
        });
    }
}
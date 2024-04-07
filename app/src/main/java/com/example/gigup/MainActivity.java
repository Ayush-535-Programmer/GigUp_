package com.example.gigup;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    String[] item = {"Bennett University","Galgotia University","Delhi University","IIT BHU"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;
    String selected_item = "";
    Button btnContinue;
    FirebaseAuth auth;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        autoCompleteTextView = findViewById(R.id.auto_completeTextview);
        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, item);
        btnContinue = findViewById(R.id.btnContinue);

        autoCompleteTextView.setAdapter(adapterItems);
        auth = FirebaseAuth.getInstance();

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selected_item = adapterView.getItemAtPosition(i).toString();
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = selected_item;
                Intent intent = new Intent(MainActivity.this, signin_activity.class);
                intent.putExtra("University_Name",data);
                startActivity(intent);
            }
        });
        if(auth.getCurrentUser()!=null){
            finish();
            Intent intent2 = new Intent(MainActivity.this, User_Home_Page.class);
            startActivity(intent2);
        }
    }
}
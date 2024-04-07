package com.example.gigup;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gigup.Model.TaskDetail;
import com.example.gigup.Model.User;
import com.example.gigup.Model.uploaderModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class UserUpload extends Fragment {

    public UserUpload() {
        // Required empty public constructor
    }

    EditText title, uploadPrice, uploadDesc;

    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;
    String selected_item = "";
    Button addTask;
    FirebaseDatabase database;
    FirebaseAuth auth;
    int valueInt  = 0;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_upload,container,false);

        title = view.findViewById(R.id.userUploadTitle);
        uploadPrice = view.findViewById(R.id.userUploadPrice);
        uploadDesc = view.findViewById(R.id.userUploadDesc);
        String[] item = {"PickUp","Studies","Assignments","Project","Miscellaneous"};

        autoCompleteTextView = view.findViewById(R.id.UserUpload_auto_completeTextview);
        adapterItems = new ArrayAdapter<String>(getContext(), R.layout.list2_item, item);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        addTask = view.findViewById(R.id.addTask);
        autoCompleteTextView.setAdapter(adapterItems);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selected_item = adapterView.getItemAtPosition(i).toString();
            }
        });

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!selected_item.isEmpty() && !title.getText().toString().isEmpty() && !uploadPrice.getText().toString().isEmpty()&& !uploadDesc.getText().toString().isEmpty()) {
                    String id = Objects.requireNonNull(auth.getUid());
                    LocalDate currentDate = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        currentDate = LocalDate.now();
                    }
                    String formattedDate = "2024-04-08";
                    // Define the date format
                    DateTimeFormatter formatter = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    }

                    // Format the current date
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        formattedDate = currentDate.format(formatter);
                    }

                    String[] lastNo = {"0"};
                    try {
                        String finalFormattedDate = formattedDate;
                        database.getReference().child("Tasks").child("TD").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String value = snapshot.getValue().toString();
                                value = value.substring(4, value.length() - 1);
                                lastNo[0] = value;
                                valueInt = Integer.parseInt(value);

                                // Update UI or perform further operations inside onDataChange

                                TaskDetail mod = new TaskDetail("" + (valueInt + 1));
                                uploaderModel upload = new uploaderModel(title.getText().toString(), uploadDesc.getText().toString(), selected_item, uploadPrice.getText().toString(), finalFormattedDate, "1", "null",auth.getUid()+":~T"+lastNo[0]);
                                database.getReference().child("Tasks").child(auth.getUid()).child("T"+lastNo[0]).setValue(upload);
                                database.getReference().child("Tasks").child("TD").setValue(mod);
                                Toast.makeText(getContext(), "Uploaded Task successfully", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle onCancelled
                            }
                        });
                        
                    } catch (Exception e) {
                        title.setText(e.getMessage());
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
//                    database.getReference().child("Tasks").child(auth.getUid()).child("T"+lastNo[0]).setValue(upload);
                
            }
        });
        return view;
    }
}
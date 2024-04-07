package com.example.gigup;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gigup.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends Fragment {

    public UserProfile() {
        // Required empty public constructor
    }
    FirebaseAuth auth;
    FirebaseDatabase database;
    TextView userName, email, eId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile,container,false);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        userName = view.findViewById(R.id.userName);
        email = view.findViewById(R.id.email);
        eId = view.findViewById(R.id.userId);

        database.getReference().child("Company").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                userName.setText(user.getUserName());
                eId.setText(user.geteId());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}
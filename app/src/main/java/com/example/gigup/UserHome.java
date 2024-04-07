package com.example.gigup;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.gigup.Adapters.UserHomeAdapter;
import com.example.gigup.Model.fragmentUserHome;
import com.example.gigup.Model.uploaderModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserHome extends Fragment {


    public UserHome() {
        // Required empty public constructor
    }

    RecyclerView recyclerView;
    FirebaseDatabase database;
    FirebaseAuth auth;
    View view;
    DatabaseReference tasksRef;
    ArrayList<fragmentUserHome> list;
    Button acceptBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_home,container,false);
        list = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        tasksRef = database.getReference().child("Tasks");
        acceptBtn = view.findViewById(R.id.acceptBtn);

        auth = FirebaseAuth.getInstance();


        updateRecycler();


        return view;
//        return inflater.inflate(R.layout.fragment_user_home, container, false);
    }

    public void updateRecycler(){
        tasksRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> sLst;
                for (DataSnapshot taskSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot childSnapshot : taskSnapshot.getChildren()) {
                        sLst = new ArrayList<>();
                        for (DataSnapshot cS : childSnapshot.getChildren()) {
                            String value = cS.getValue(String.class);
                            sLst.add(value);
                        }
                        if (sLst.size() >= 8 && sLst.get(5).equals("1") && !sLst.get(3).toString().substring(0,sLst.get(3).toString().indexOf(':')).equals(auth.getUid())) {
                            list.add(new fragmentUserHome(sLst.get(7), sLst.get(2), sLst.get(6), sLst.get(4),sLst.get(3)));
                        } else {
                            System.out.println("Not enough elements in sLst to create fragmentUserHome object");
                        }
//                        list.add(new fragmentUserHome(sLst.get(6), sLst.get(2), sLst.get(5), sLst.get(3)));
                    }
                }
                recyclerView = view.findViewById(R.id.homeRecyclerView);
                UserHomeAdapter adapter = new UserHomeAdapter(list, getContext());
                recyclerView.setAdapter(adapter);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(layoutManager);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
                System.out.println("Error: " + databaseError.getMessage());
            }
        });
    }
}
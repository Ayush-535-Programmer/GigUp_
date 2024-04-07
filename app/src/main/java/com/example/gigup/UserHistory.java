package com.example.gigup;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.gigup.Adapters.UserHistoryAdapter;
import com.example.gigup.Adapters.UserHomeAdapter;
import com.example.gigup.Model.fragmentUserHistory;
import com.example.gigup.Model.fragmentUserHome;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserHistory extends Fragment {

    public UserHistory() {
        // Required empty public constructor
    }
    FirebaseDatabase database;
    RecyclerView recyclerView;
    DatabaseReference tasksRef;
    ArrayList<fragmentUserHistory> list;
    View view;
    FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_history,container,false);
        database = FirebaseDatabase.getInstance();
        tasksRef = database.getReference().child("Tasks");
        list = new ArrayList<>();

        auth = FirebaseAuth.getInstance();

        updateRecycler();
        return view;
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
                        if (sLst.size() >= 8 && (sLst.get(5).equals("2")||sLst.get(5).equals("3")) && sLst.get(3).toString().substring(0,sLst.get(3).toString().indexOf(':')).equals(auth.getUid())) {
                            list.add(new fragmentUserHistory(sLst.get(7), sLst.get(6), sLst.get(5), sLst.get(1), sLst.get(4), sLst.get(3)));
                        } else {
                            System.out.println("Not enough elements in sLst to create fragmentUserHome object");
                        }
//                        list.add(new fragmentUserHome(sLst.get(6), sLst.get(2), sLst.get(5), sLst.get(3)));
                    }
                }
                recyclerView = view.findViewById(R.id.historyRecyclerView);
                UserHistoryAdapter adapter = new UserHistoryAdapter(list, getContext());
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
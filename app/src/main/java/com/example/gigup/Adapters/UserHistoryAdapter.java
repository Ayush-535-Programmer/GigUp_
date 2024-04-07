package com.example.gigup.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gigup.Model.fragmentUserHistory;
import com.example.gigup.Model.fragmentUserHome;
import com.example.gigup.Model.uploaderModel;
import com.example.gigup.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserHistoryAdapter extends RecyclerView.Adapter<UserHistoryAdapter.viewHolder> {

    ArrayList<fragmentUserHistory> list;
    Context context;
    FirebaseDatabase database;
    DatabaseReference tasksRef;

    public UserHistoryAdapter(ArrayList<fragmentUserHistory> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public UserHistoryAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_history, parent, false);
        return new UserHistoryAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHistoryAdapter.viewHolder holder, int position) {
        fragmentUserHistory model = list.get(position);
        holder.title.setText(model.getTitle());
        holder.date.setText(model.getDate());
        holder.price.setText(model.getPrice());
        holder.tag.setText(model.getTag());

        database = FirebaseDatabase.getInstance();
        tasksRef = database.getReference().child("Tasks");

        holder.endTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String location = model.getLocation().replaceAll(":~","/");
                database.getReference().child("Tasks").child(location).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        uploaderModel mod = snapshot.getValue(uploaderModel.class);
                        if(mod != null){
                            uploaderModel upload = new uploaderModel(mod.getTitle(),mod.getDesc(),mod.getTag(),mod.getPrice(),mod.getDate(),"3",mod.getAid(),mod.getLocation());
                            database.getReference().child("Tasks").child(location).setValue(upload);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        if (model.getStatus().equals("2")) {
            holder.endTask.setVisibility(View.VISIBLE);
            holder.status.setVisibility(View.VISIBLE);
        } else{
            holder.endTask.setVisibility(View.GONE);
            holder.status.setVisibility(View.VISIBLE);
            holder.status.setText("Done");
            holder.status.setTextColor(Color.parseColor("#55E2E9"));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class viewHolder extends RecyclerView.ViewHolder {

        TextView title, price, tag, date, status;
        Button endTask;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.userHistoryTitle);
            date = itemView.findViewById(R.id.userHistoryDate);
            price = itemView.findViewById(R.id.userHistoryPrice);
            tag = itemView.findViewById(R.id.userHistoryTag);
            endTask = itemView.findViewById(R.id.userEndTask);
            status = itemView.findViewById(R.id.userHistoryStatus);
        }
    }
}
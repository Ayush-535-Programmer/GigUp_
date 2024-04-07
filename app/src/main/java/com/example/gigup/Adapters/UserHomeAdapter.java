package com.example.gigup.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gigup.Model.fragmentUserHome;
import com.example.gigup.Model.uploaderModel;
import com.example.gigup.R;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserHomeAdapter extends RecyclerView.Adapter<UserHomeAdapter.viewHolder>{

    ArrayList<fragmentUserHome> list;
    Context context;
    DatabaseReference tasksRef;
    FirebaseDatabase database;
    RecyclerView recyclerView;

    public UserHomeAdapter(ArrayList<fragmentUserHome> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_home,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHomeAdapter.viewHolder holder, int position) {
        fragmentUserHome model = list.get(position);
        holder.title.setText(model.getTitle());
        holder.desc.setText(model.getDesc());
        holder.price.setText(model.getPrice());
        holder.tag.setText(model.getTag());

        database = FirebaseDatabase.getInstance();
        tasksRef = database.getReference().child("Tasks");
        
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String location = model.getLocation().replaceAll(":~","/");
                database.getReference().child("Tasks").child(location).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        uploaderModel mod = snapshot.getValue(uploaderModel.class);
                        if(mod != null){
                            uploaderModel upload = new uploaderModel(mod.getTitle(),mod.getDesc(),mod.getTag(),mod.getPrice(),mod.getDate(),"2",mod.getAid(),mod.getLocation());
                            database.getReference().child("Tasks").child(location).setValue(upload);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });


//        holder.cardView.setOnClickListener(view -> {
//            Intent intent = new Intent(context,Solution_view_farmer.class);
//            intent.putExtra("cropName",model.getCropName());
//            intent.putExtra("cropImage",model.getPic());
//            context.startActivity(intent);
//        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public class viewHolder extends RecyclerView.ViewHolder {
        TextView title, desc, price, tag;
        Button btn;
        DatabaseReference tasksRef;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.userTitle);
            desc = itemView.findViewById(R.id.userDescription);
            price = itemView.findViewById(R.id.price);
            tag = itemView.findViewById(R.id.userTag);

            btn = itemView.findViewById(R.id.acceptBtn);
        }

        public void acceptResponse() {
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
                            if (sLst.size() >= 7 && sLst.get(4).equals("1")) {
                                list.add(new fragmentUserHome(sLst.get(6), sLst.get(2), sLst.get(5), sLst.get(3),sLst.get(4)));
                            } else {
                                System.out.println("Not enough elements in sLst to create fragmentUserHome object");
                            }
//                        list.add(new fragmentUserHome(sLst.get(6), sLst.get(2), sLst.get(5), sLst.get(3)));
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle errors
                    System.out.println("Error: " + databaseError.getMessage());
                }
            });
        }
    }
}


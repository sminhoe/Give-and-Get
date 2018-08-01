package com.example.user8.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.foodviewholder>{

    private List<Food> list;

    public int curPos=-1;

    public RequestAdapter(List<Food> list) {
        this.list = list;
    }


    @Override
    public RequestAdapter.foodviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.view_request,parent,false);
        final RequestAdapter.foodviewholder vHolder = new RequestAdapter.foodviewholder(v);

        return vHolder;
    }

    @Override
    public void onBindViewHolder(final RequestAdapter.foodviewholder holder, final int position) {
        Food food = list.get(position);

        holder.txtname.setText(food.u_food);
        holder.txtdate.setText(food.u_date);
        holder.txttime.setText(food.u_time);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Food");
        ref.child(food.key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String check = (String) dataSnapshot.child("status").getValue();
                if (check.equals("ACCEPT"))
                {
                    holder.txtAccept.setVisibility(View.VISIBLE);
                }
                else
                {
                    holder.txtAccept.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ref.child(food.key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String check = (String) dataSnapshot.child("uid").getValue();
                DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("Users");
                ref2.child(check).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String check = (String) dataSnapshot.child("u_username").getValue();
                        holder.txtOwner.setText(check);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class foodviewholder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        TextView txtname,txtdate,txttime,txtAccept, txtOwner;

        public foodviewholder(View itemView) {
            super(itemView);
            txtname = (TextView)itemView.findViewById(R.id.text_food);
            txtdate = (TextView)itemView.findViewById(R.id.text_date);
            txttime = (TextView)itemView.findViewById(R.id.text_time);
            txtAccept = (TextView)itemView.findViewById(R.id.txtAccept);
            txtOwner = (TextView)itemView.findViewById(R.id.text_owner);

            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    curPos=getAdapterPosition();
                }});
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            curPos=getAdapterPosition();
            menu.add(0, 0, 0, "Delete Request");
            menu.add(0, 1, 0, "Cancel");
        }
    }
}

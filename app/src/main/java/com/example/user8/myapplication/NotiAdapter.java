package com.example.user8.myapplication;

import android.content.ClipData;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class NotiAdapter extends RecyclerView.Adapter<NotiAdapter.foodviewholder>{
    private List<Food> list;

    public int curPos=-1;

    private FirebaseDatabase database;
    private DatabaseReference ref;
    FirebaseAuth auth;
    FirebaseUser user;

    public NotiAdapter(List<Food> list) {
        this.list = list;
    }

    @Override
    public foodviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.view_noti,parent,false);
        final foodviewholder vHolder = new foodviewholder(v);

        return vHolder;
    }

    @Override
    public void onBindViewHolder(final foodviewholder holder, final int position) {
        Food food = list.get(position);

        holder.txtname.setText(food.u_food);
        holder.txtdate.setText(food.u_date);
        holder.txttime.setText(food.u_time);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Food");
        ref.child(food.key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String check = (String) dataSnapshot.child("status").getValue();
                String checkuid = (String) dataSnapshot.child("uid_request").getValue();
                if (check.equals("REQUEST"))
                {        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("Users");
                        ref2.child(checkuid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String name = (String) dataSnapshot.child("u_username").getValue();

                            if (name.length() > 0)
                            {
                                holder.txtuidR.setText(name);
                                holder.txtPending.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                holder.txtuidR.setText("Username");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else
                {
//                    holder.txtuidR.setText("NO ACCEPTOR");
//                    holder.txtPending.setVisibility(View.INVISIBLE);
                    holder.noti_list.setVisibility(View.GONE);
                }
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

        TextView txtname,txtdate,txttime,txtPending,txtuidR, txtstatus;
        LinearLayout noti_list;

        public foodviewholder(View itemView) {
            super(itemView);
            txtname = (TextView)itemView.findViewById(R.id.text_food);
            txtdate = (TextView)itemView.findViewById(R.id.text_date);
            txttime = (TextView)itemView.findViewById(R.id.text_time);
            txtuidR = (TextView)itemView.findViewById(R.id.txtuidR);
            txtstatus = (TextView)itemView.findViewById(R.id.txtstatus);
            txtPending = (TextView)itemView.findViewById(R.id.txtPending);

            noti_list = (LinearLayout)itemView.findViewById(R.id.noti_list);

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
            menu.add(0, 0, 0, "Accept");
            menu.add(0, 1, 0, "Reject");
            menu.add(0, 2, 0, "Back");
        }
    }
}

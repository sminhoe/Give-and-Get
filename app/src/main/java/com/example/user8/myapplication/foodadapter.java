package com.example.user8.myapplication;

import android.content.ClipData;
import android.content.Context;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

public class foodadapter extends RecyclerView.Adapter<foodadapter.foodviewholder>{

    private List<Food>list;

    FirebaseAuth auth;
    FirebaseUser user;

    private Context mContext;

    public int curPos=-1;

    public foodadapter(List<Food> list, Context context) {
        this.list = list;
        this.mContext = context;
    }

    @Override
    public foodviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.view_item,parent,false);
        final foodviewholder vHolder = new foodviewholder(v);

        return vHolder;
    }

    @Override
    public void onBindViewHolder(final foodviewholder holder, final int position) {
        Food food = list.get(position);

        holder.txtname.setText(food.u_food);
        holder.txtdate.setText(food.u_date);
        holder.txttime.setText(food.u_time);
        holder.txtpostdate.setText(food.u_postdate);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(food.uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = (String) dataSnapshot.child("u_username").getValue();
                holder.txtusername.setText(name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ref.child(food.uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String imgURL = (String) dataSnapshot.child("u_imgURL").getValue();
                if(imgURL != "0")
                {
                    Glide.with(mContext).load(imgURL).into(holder.imgUser);
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

        TextView txtname,txtdate,txttime,txtusername,txtpostdate;
        ImageView imgUser;

        public foodviewholder(View itemView) {
            super(itemView);
            txtname = (TextView)itemView.findViewById(R.id.text_food);
            txtdate = (TextView)itemView.findViewById(R.id.text_date);
            txttime = (TextView)itemView.findViewById(R.id.text_time);
            txtusername = (TextView)itemView.findViewById(R.id.text_username);
            txtpostdate = (TextView)itemView.findViewById(R.id.text_postdate);
            imgUser = (ImageView)itemView.findViewById(R.id.user_image);

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
            menu.add(0, 0, 0, "Request");
            menu.add(0, 1, 0, "Cancel");
        }
    }
}

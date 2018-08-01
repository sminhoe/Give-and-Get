package com.example.user8.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.security.AccessController.getContext;

public class MyListActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;

    private RecyclerView recyclerView;
    private TextView txtEmpty;
    private List<Food> result;
    private Mylistadapter adapter;

    private FirebaseDatabase database;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Food");

        result = new ArrayList<>();

        recyclerView = (RecyclerView)findViewById(R.id.myfood_list);
        txtEmpty = (TextView)findViewById(R.id.txtEmpty);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager lim = new LinearLayoutManager(this);
        lim.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(lim);

        adapter = new Mylistadapter(result);
        recyclerView.setAdapter(adapter);

        updateList();
        checkIfEmpty();
    }

    private void checkIfEmpty()
    {
        if(result.size()==0)
        {
            recyclerView.setVisibility(View.INVISIBLE);
            txtEmpty.setVisibility(View.VISIBLE);
        }
        else
        {
            recyclerView.setVisibility(View.VISIBLE);
            txtEmpty.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case 0:
                if(adapter.curPos>-1){
                    removeFood(adapter.curPos);
                }
                break;

            case 1:
                if(adapter.curPos>-1){
                    closeFood(adapter.curPos);
                }
                break;
        }
        return super.onContextItemSelected(item);
    }

    private int getItemIndex(Food food){
        int index = -1;
        for(int i = 0; i < result.size();i++)
        {
            if (result.get(i).key.equals(food.key))
            {
                index = i;
                break;
            }

        }
        return index;
    }

    private void updateList() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        String uid = user.getUid();
        ref.orderByChild("uid").equalTo(uid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                result.add(dataSnapshot.getValue(Food.class));
                adapter.notifyDataSetChanged();
                checkIfEmpty();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Food model = dataSnapshot.getValue(Food.class);

                int index = getItemIndex(model);

                result.set(index,model);
                adapter.notifyItemChanged(index);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Food model = dataSnapshot.getValue(Food.class);

                int index = getItemIndex(model);

                result.remove(index);
                adapter.notifyItemRemoved(index);
                checkIfEmpty();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

//    private void removeFood(int pos){
//        ref.child(result.get(pos).key).removeValue();
//    }

    private void removeFood(int pos){
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        String uid = currentFirebaseUser.getUid();
        Food food = result.get(pos);
        food.status="UNAVAILABLE";
        food.uid_request="none";

        Map<String,Object> foodValue = food.toMap();
        Map<String,Object> newFood = new HashMap<>();

        newFood.put(food.key,foodValue);

        ref.updateChildren(newFood);
    }

    private void closeFood(int pos){
        closeContextMenu();
    }

}

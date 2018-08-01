package com.example.user8.myapplication;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
//    ListView listView;
//    DatabaseReference db;
private RecyclerView recyclerView;
    private TextView txtEmpty,txtEmpty2;
    private List<Food> result;
    private foodadapter adapter;

    private FirebaseDatabase database;
    private DatabaseReference ref;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Food");

        result = new ArrayList<>();

        recyclerView = (RecyclerView)v.findViewById(R.id.food_list);
        txtEmpty = (TextView)v.findViewById(R.id.txtEmpty);
        txtEmpty2 = (TextView)v.findViewById(R.id.txtEmpty2);

        recyclerView.setHasFixedSize(false);
        LinearLayoutManager lim = new LinearLayoutManager(getContext());
        lim.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(lim);

        adapter = new foodadapter(result,getContext());
        recyclerView.setAdapter(adapter);

        updateList();
        checkIfEmpty();

        return v;
    }

    private void checkIfEmpty()
    {
        if(result.size()==0)
        {
            recyclerView.setVisibility(View.INVISIBLE);
            txtEmpty.setVisibility(View.VISIBLE);
            txtEmpty2.setVisibility(View.VISIBLE);
        }
        else
        {
            recyclerView.setVisibility(View.VISIBLE);
            txtEmpty.setVisibility(View.INVISIBLE);
            txtEmpty2.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case 0:
                if(adapter.curPos>-1){
                    requestFood(adapter.curPos);
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

        ref.orderByChild("status").equalTo("AVAILABLE").addChildEventListener(new ChildEventListener() {
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


    private void requestFood(int pos){
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        String uid = currentFirebaseUser.getUid();
        Food food = result.get(pos);
        food.status="REQUEST";
        food.uid_request=uid;

        Map<String,Object> foodValue = food.toMap();
        Map<String,Object> newFood = new HashMap<>();

        newFood.put(food.key,foodValue);

        ref.updateChildren(newFood);
    }

    private void closeFood(int pos){
        ((Activity) getContext()).closeContextMenu();
    }

}


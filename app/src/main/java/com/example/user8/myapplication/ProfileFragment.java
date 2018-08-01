package com.example.user8.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    FirebaseAuth auth;
    FirebaseUser user;

    TextView showemail,show_username;
    Button button,button2,button3,button4,button5;
    ImageView imgprofile;
    private ProgressDialog progressDialog;



    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        button = (Button) rootView.findViewById(R.id.btn_logout);
        button2 = (Button)rootView.findViewById(R.id.btn_list) ;
        button3 = (Button)rootView.findViewById(R.id.btn_acc) ;
        button4 = (Button)rootView.findViewById(R.id.btn_noti) ;
        button5 = (Button)rootView.findViewById(R.id.btn_request) ;
        show_username = (TextView) rootView.findViewById(R.id.user_name);
        imgprofile = (ImageView)rootView.findViewById(R.id.user_image);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Signing out...");

        String uid = user.getUid();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");

        ref.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String imgURL = (String) dataSnapshot.child("u_imgURL").getValue();
                if(imgURL.equals("0"))
                {
                    imgprofile.setImageDrawable(getResources().getDrawable(R.drawable.img_avatar2));
                }
                else
                {
                    Glide.with(getActivity()).load(imgURL).into(imgprofile);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ref.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username = (String) dataSnapshot.child("u_username").getValue();
                if(username.length() > 0)
                {
                    show_username.setText(username);
                }
                else
                {
                    show_username.setText("Username");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                progressDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        auth.signOut();
                        getActivity().finish();
                        if (auth.getCurrentUser() == null) {
                            progressDialog.cancel();
                            Intent i = new Intent(getContext(), LoginActivity.class);
                            startActivity(i);
                        }
                    }
                },5000);

            }
        });

        button2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getContext(), MyListActivity.class);
                startActivity(i);
            }
        });

        button3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getContext(), UserAccActivity.class);
                startActivity(i);
            }
        });

        button4.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getContext(), NotiActivity.class);
                startActivity(i);
            }
        });

        button5.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getContext(), RequestActivity.class);
                startActivity(i);
            }
        });

        return rootView;
    }


}
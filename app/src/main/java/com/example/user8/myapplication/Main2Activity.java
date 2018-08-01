package com.example.user8.myapplication;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Main2Activity extends AppCompatActivity {

    private BottomNavigationView mMainNav;
    private FrameLayout mMainFrame;

    private ShareFragment shareFragment;
    private ProfileFragment profileFragment;
    private HomeFragment homeFragment;

    private FirebaseAuth auth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        homeFragment = new HomeFragment();
        shareFragment = new ShareFragment();
        profileFragment = new ProfileFragment();

        mMainNav = (BottomNavigationView)findViewById(R.id.main_nav);
        mMainFrame = (FrameLayout)findViewById(R.id.main_frame);

        auth = FirebaseAuth.getInstance();


        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.menu_home:
//                        mMainNav.setItemBackgroundResource(R.color.grey);
                        setFragment(homeFragment);
                        return true;

                    case R.id.menu_share:
//                        mMainNav.setItemBackgroundResource(R.color.endgreen);
                        setFragment(shareFragment);
                        return true;

                    case R.id.menu_profile:
//                        mMainNav.setItemBackgroundResource(R.color.grey);
                        setFragment(profileFragment);
                        return true;

                        default:
                            return false;
                }
            }

            private void setFragment(Fragment fm) {
               FragmentTransaction fgtrans = getSupportFragmentManager().beginTransaction();
                fgtrans.replace(R.id.main_frame, fm);
                fgtrans.commit();
            }
        });

//        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//
//        boolean network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//
//        Location location;
//
//        if (network_enabled) {
//
//            if (ActivityCompat.checkSelfPermission(Main2Activity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
//                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Main2Activity.this,
//                    android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return;
//            }
//            location = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//
//            if (location != null) {
//                double longitude = location.getLongitude();
//                double latitude = location.getLatitude();
//
//                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
//
//                firebaseUser = auth.getCurrentUser();
//                String uid = firebaseUser.getUid();
//
//                ref.child(uid).child("u_longitude").setValue(longitude);
//                ref.child(uid).child("u_latitude").setValue(latitude);
//            }
//        }
    }
}

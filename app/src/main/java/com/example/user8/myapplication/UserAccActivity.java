package com.example.user8.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.storage.StorageManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class UserAccActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    TextView show_username,show_useremail;
    Button btn_choosepic,btn_uploadpic;
    ImageView imgprofile;
    Context context;

    Uri filepath;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_acc);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        String uid = user.getUid();

        show_username = (TextView) findViewById(R.id.u_username);
        show_useremail = (TextView) findViewById(R.id.u_email);
        imgprofile = (ImageView)findViewById(R.id.user_image);
        btn_choosepic = (Button)findViewById(R.id.btn_choosepic);
        btn_uploadpic = (Button)findViewById(R.id.btn_uploadpic);
        show_useremail.setText(user.getEmail());

        context = this;

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
                    Glide.with(context).load(imgURL).into(imgprofile);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ref.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = (String) dataSnapshot.child("u_username").getValue();
                show_username.setText(name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btn_choosepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),1);
            }
        });

        btn_uploadpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadimage();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK)
        {
            filepath = data.getData();
            try
            {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);
                imgprofile.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void uploadimage() {
        if (filepath != null)
        {
            final ProgressDialog pg = new ProgressDialog(this);
            pg.setTitle("Uploading...");
            pg.show();
            StorageReference ref = storageReference.child("image/" + UUID.randomUUID().toString());
            ref.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    pg.dismiss();
                    DatabaseReference databaseReference ;
                    databaseReference= FirebaseDatabase.getInstance().getReference("Users");
                    String imageurl = taskSnapshot.getDownloadUrl().toString();

                    String uid = user.getUid();
                    // Adding image upload id s child element into databaseReference.
                    databaseReference.child(uid).child("u_imgURL").setValue(imageurl);
                    Toast.makeText(UserAccActivity.this, "FINISH", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pg.dismiss();
                    Toast.makeText(UserAccActivity.this, "FAIL "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    pg.setMessage("Uploaded " + (int)progress+"%");
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(UserAccActivity.this, FirstActivity.class));
            finish();
        }
    }


}

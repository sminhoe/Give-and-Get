package com.example.user8.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity
{
    private Button btn_signin,btn_signup;
    private EditText inputEmail, inputPassword, inputconPassword,inputUserName;
    private ProgressDialog progressDialog;
    private FirebaseAuth auth;
    FirebaseUser firebaseUser;

    private static final String PASSWORD_PATTERN ="((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})";
    private static  final String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        auth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creating your account....");

        btn_signin = (Button) findViewById(R.id.btn_signin);
        btn_signup = (Button) findViewById(R.id.btn_signup);
        inputEmail = (EditText) findViewById(R.id.u_email);
        inputPassword = (EditText) findViewById(R.id.u_password);
        inputconPassword = (EditText) findViewById(R.id.u_conpassword);
        inputUserName = (EditText) findViewById(R.id.u_username);

        btn_signin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = inputEmail.getText().toString().trim();
                final String password = inputPassword.getText().toString().trim();
                final String username = inputUserName.getText().toString().trim();

                if (!validateForm())
                {
                    return;
                }

                progressDialog.show();

                //checkusername
                Query usernameQuery = FirebaseDatabase.getInstance().getReference("Users").orderByChild("u_username").equalTo(username);
                usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() > 0)
                        {
                            progressDialog.cancel();
                            Toast.makeText(SignUpActivity.this,  "Your user name has been used.",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            auth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            progressDialog.cancel();
                                            // If sign in fails, display a message to the user. If sign in succeeds
                                            // the auth state listener will be notified and logic to handle the
                                            // signed in user can be handled in the listener.
                                            if (task.isSuccessful())
                                            {
                                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");

                                                firebaseUser = auth.getCurrentUser();
                                                String uid = firebaseUser.getUid();

                                                User my = new User(email,username,"0");

                                                ref.child(uid).setValue(my).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task)
                                                    {
                                                        if(task.isSuccessful())
                                                        {
                                                            Toast.makeText(SignUpActivity.this,  getString(R.string.sign_up_success),Toast.LENGTH_SHORT).show();
                                                            finish();
                                                            startActivity(new Intent(getApplicationContext(), Main2Activity.class));
                                                        }
                                                        else
                                                        {
                                                            Toast.makeText(SignUpActivity.this,  getString(R.string.sign_in_up_error),Toast.LENGTH_SHORT).show();
                                                            finish();
                                                            startActivity(new Intent(getApplicationContext(), FirstActivity.class));
                                                        }
                                                    }
                                                });


                                            }
                                            else
                                            {
                                                Toast.makeText(SignUpActivity.this, getString(R.string.auth_failed) + task.getException(),
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

    }

    private boolean validateForm()
    {
        boolean valid = true;

        String email = inputEmail.getText().toString();
        String password =inputPassword.getText().toString();
        String conpassword = inputconPassword.getText().toString();
        String username = inputUserName.getText().toString().trim();

        if (TextUtils.isEmpty(email))
        {
            inputEmail.setError(getString(R.string.required_msg));
            valid = false;
        }

        if (TextUtils.isEmpty(username))
        {
            inputUserName.setError(getString(R.string.required_msg));
            valid = false;
        }

        if (TextUtils.isEmpty(password))
        {
            inputPassword.setError(getString(R.string.required_msg));
            valid = false;
        }

        if (TextUtils.isEmpty(conpassword))
        {
            inputconPassword.setError(getString(R.string.required_msg));
            valid = false;
        }

        if(email.length()>0 && password.length()>0 && conpassword.length()>0  && username.length()>0)
        {
            if (isEmailValid(email))
            {
                inputEmail.setError(null);
                if (isValidPassword(password))
                {
                    inputPassword.setError(null);
                    if (isValidPassword(conpassword))
                    {
                        inputconPassword.setError(null);
                        if (password.equals(conpassword))
                        {
                            return valid;
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),getString(R.string.password_not_match_msg) , Toast.LENGTH_SHORT).show();
                            valid = false;
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), getString(R.string.password_require_msg), Toast.LENGTH_SHORT).show();
                        valid = false;
                    }
                }
                else
                    {
                    Toast.makeText(getApplicationContext(), getString(R.string.password_require_msg), Toast.LENGTH_SHORT).show();
                    valid = false;
                }
            }
            else
                {
                Toast.makeText(getApplicationContext(), getString(R.string.email_invalid_msg), Toast.LENGTH_SHORT).show();
                valid = false;
            }
        }
        return valid;
    }

    public static boolean isEmailValid(String email)
    {
       Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidPassword(final String password)
    {
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        progressDialog.cancel();
    }
}


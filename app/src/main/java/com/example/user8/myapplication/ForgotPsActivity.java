package com.example.user8.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPsActivity extends AppCompatActivity {

    EditText edtPs,edtPs2;
    Button btnChange;
    FirebaseAuth auth;
    ProgressDialog dialog;

    private static final String PASSWORD_PATTERN ="((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_ps);
        dialog = new ProgressDialog(this);
        edtPs = (EditText)findViewById(R.id.edtForgotPw);
        edtPs2 = (EditText)findViewById(R.id.edtForgotPw2);
        btnChange = (Button)findViewById(R.id.btnChangeps);
        auth = FirebaseAuth.getInstance();

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateForm())
                {
                    return;
                }
                else
                    change(v);
            }
        });
    }

    public void change (View v)
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null)
        {
            dialog.setMessage("Changing Password... Please wait...");
            dialog.show();

            user.updatePassword(edtPs.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                    {
                        dialog.dismiss();
                        auth.signOut();
                        if (auth.getCurrentUser() == null) {
                            Toast.makeText(getApplicationContext(),"Password changed. Please sign in.",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ForgotPsActivity.this, LoginActivity.class));
                            finish();
                        }
                    }
                    else
                    {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Error. Try again.",Toast.LENGTH_SHORT);
                    }
                }
            });
        }
    }

    private boolean validateForm()
    {
        boolean valid = true;

        String password =edtPs.getText().toString();
        String password2 =edtPs2.getText().toString();
        if (TextUtils.isEmpty(password))
        {
            edtPs.setError(getString(R.string.required_msg));
            valid = false;
        }

        if (TextUtils.isEmpty(password2))
        {
            edtPs2.setError(getString(R.string.required_msg));
            valid = false;
        }

        if(password.length()>0 && password2.length()>0)
        {
            if(isValidPassword(password))
            {
                edtPs.setError(null);
                if (isValidPassword(password2))
                {
                    edtPs2.setError(null);
                    if (password.equals(password2))
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
        return valid;
    }

    public static boolean isValidPassword(final String password)
    {
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }
}


package com.example.user8.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
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
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResetPwActivity extends AppCompatActivity {

    private Button btn_reset_pw,btn_mainpage;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private EditText inputEmail;

    private static  final String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pw);
        inputEmail = (EditText)findViewById(R.id.u_email) ;
        btn_reset_pw = (Button) findViewById(R.id.btn_reset_pw);
        btn_mainpage = (Button)findViewById(R.id.btn_mainpg) ;
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        auth = FirebaseAuth.getInstance();

        btn_mainpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResetPwActivity.this, FirstActivity.class));
            }
        });

        btn_reset_pw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final String email = inputEmail.getText().toString().trim();

                    if (!validateForm())
                    {
                        return;
                    }
                    progressBar.setVisibility(View.VISIBLE);
                    auth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ResetPwActivity.this, getString(R.string.reset_pw_msg), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ResetPwActivity.this, getString(R.string.reset_pw_msgfail), Toast.LENGTH_SHORT).show();
                                    }
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                }
            });
        }

    private boolean validateForm()
    {
        boolean valid = true;

        String email = inputEmail.getText().toString();
        if (TextUtils.isEmpty(email))
        {
            inputEmail.setError(getString(R.string.required_msg));
            valid = false;
        }


        if(email.length()>0)
        {
            if (isEmailValid(email))
            {
                return valid;
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
}




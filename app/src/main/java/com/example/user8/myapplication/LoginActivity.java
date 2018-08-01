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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private Button btn_signin,btn_signup,btn_forgotpw;
    private EditText inputEmail, inputPassword;
    private ProgressDialog progressDialog;
    private FirebaseAuth auth;
    private FirebaseUser firebaseuser;

    private static final String PASSWORD_PATTERN ="((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})";
    private static  final String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");

        inputEmail = (EditText)findViewById(R.id.u_email);
        inputPassword = (EditText) findViewById(R.id.u_password);
        btn_forgotpw = (Button) findViewById(R.id.btn_forgotpw);
        btn_signin = (Button)findViewById(R.id.btn_signin);
        btn_signup = (Button)findViewById(R.id.btn_signup);
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, Main2Activity.class));
            finish();
        }

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        btn_forgotpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPwActivity.class));
            }
        });

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = inputEmail.getText().toString();
                final String passw = inputPassword.getText().toString();

                if (!validateForm())
                {
                    return;
                }

                progressDialog.show();
                auth.signInWithEmailAndPassword(email,passw).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.cancel();
                        if (!task.isSuccessful())
                        {
                            Toast.makeText(LoginActivity.this, getString(R.string.sign_in_up_error), Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this, getString(R.string.auth_success), Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(), Main2Activity.class));
                        }
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

        String password =inputPassword.getText().toString();
        if (TextUtils.isEmpty(password))
        {
            inputPassword.setError(getString(R.string.required_msg));
            valid = false;
        }

        if(email.length()>0 && password.length()>0)
        {
            if (isEmailValid(email))
            {
                inputEmail.setError(null);
                if (isValidPassword(password))
                {
                    inputPassword.setError(null);
                    return valid;
                }
                else
                {
                    Toast.makeText(getApplicationContext(), getString(R.string.auth_failed), Toast.LENGTH_SHORT).show();
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
}

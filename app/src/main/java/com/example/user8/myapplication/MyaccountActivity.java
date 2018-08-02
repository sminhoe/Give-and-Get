package com.example.user8.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MyaccountActivity extends AppCompatActivity {

    Button button,button2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myaccount);

        button = (Button)findViewById(R.id.btn_changepic);
        button2 = (Button)findViewById(R.id.btn_changeps) ;

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(MyaccountActivity.this, UserAccActivity.class);
                startActivity(i);
            }
        });

        button2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(MyaccountActivity.this, ForgotPsActivity.class);
                startActivity(i);
            }
        });
    }
}

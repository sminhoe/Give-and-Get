package com.example.user8.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowFoodActivity extends AppCompatActivity {

    int pos = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_food);

        Intent i = getIntent();
        pos = i.getExtras().getInt("Position");

        final CustomAdapter adapter = new CustomAdapter(this);
        final ImageView img = (ImageView)findViewById(R.id.imguser);
        final TextView name = (TextView)findViewById(R.id.u_food);
        final TextView price = (TextView)findViewById(R.id.u_date);

        img.setImageResource(adapter.images[pos]);
        name.setText(adapter.names[pos]);
        price.setText(adapter.price[pos]);

        Button btnnext = (Button)findViewById(R.id.btn_next);
        btnnext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                int position  = pos +1;

                img.setImageResource(adapter.images[position]);
                name.setText("Name : "+ adapter.names[position]);
                price.setText("Price : RM "+adapter.price[position]);

                if (!(position>=adapter.getCount()-1))
                {
                    pos+=1;
                }
                else
                {
                    pos=-1;
                }

            }
        });

    }
}

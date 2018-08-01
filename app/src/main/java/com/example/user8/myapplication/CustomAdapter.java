package com.example.user8.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

class CustomAdapter extends BaseAdapter {

    Context c;
    ArrayList<Food> food;

    public CustomAdapter(Context c, ArrayList<Food> food) {
        this.c = c;
        this.food = food;
    }

    String[] names = {"Samsung","Huawei","XIaomi","Apple","Oppo","Vivo"};
    String[] price = {"100","200","300","400","500","600"};
    int[] images = {R.drawable.i_samsung,R.drawable.i_huawei,R.drawable.i_xiaomi,R.drawable.i_apple,R.drawable.i_oppo,R.drawable.i_vivo};

    public CustomAdapter (Context ctx)
    {
        this.c = ctx;
    }

    @Override
    public int getCount() {
        return food.size();
    }

    @Override
    public Object getItem(int position) {
        return food.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView==null)
        {
            LayoutInflater inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.foodadapter,null);
//            convertView = LayoutInflater.from(c).inflate(R.layout.foodadapter,parent,false);
        }
//
        TextView nametxt = (TextView)convertView.findViewById(R.id.name);
        TextView pricetxt = (TextView)convertView.findViewById(R.id.price);
        ImageView img = (ImageView)convertView.findViewById(R.id.imguser);

        final Food f=(Food)this.getItem(position);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(c,f.getU_food(),Toast.LENGTH_SHORT).show();
            }
        });

        nametxt.setText(f.getU_food());
        pricetxt.setText(f.getU_date());
        img.setImageResource(images[position]);

        return convertView;
    }
}

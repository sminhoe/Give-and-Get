package com.example.user8.myapplication;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShareFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    private Spinner spinner1,spinner2,spinner3;
    private Button btn_submit,btn_selDate;
    private TextView txtDate;
    private FirebaseAuth auth;
    FirebaseUser firebaseUser;
    private ProgressDialog progressDialog;


    public ShareFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_share, container, false);

        btn_selDate = (Button) v.findViewById(R.id.btn_selDate);
        txtDate = (TextView) v.findViewById(R.id.tv);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Sharing your food...Please wait...");

        btn_selDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initialize a new date picker dialog fragment
                DialogFragment dFragment = new DatePickerFragment();

                // Show the date picker dialog fragment
                dFragment.show(getFragmentManager(), "Date Picker");
            }
        });

        btn_submit = (Button) v.findViewById(R.id.btn_submit_food);

        spinner1 = (Spinner)v.findViewById(R.id.spinner_selectFood);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),R.array.spinner_food,R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(this);

        spinner3 = (Spinner)v.findViewById(R.id.spinner_selectTime);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(getContext(),R.array.spinner_time,R.layout.support_simple_spinner_dropdown_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);
        spinner3.setOnItemSelectedListener(this);

        auth = FirebaseAuth.getInstance();
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        final String formattedDate = df.format(c);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseUser = auth.getCurrentUser();
                String uid = firebaseUser.getUid();

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Food");
                String key = ref.push().getKey();

                String u_food = spinner1.getSelectedItem().toString();
                String u_time= spinner3.getSelectedItem().toString();
                String txtD = txtDate.getText().toString();

                Food myFood = new Food(uid, u_food,txtD,u_time,formattedDate,"AVAILABLE",key,"none");
                progressDialog.show();

                ref.child(key).setValue(myFood).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), getString(R.string.share_food_success_msg), Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                            startActivity(new Intent(getContext(), Main2Activity.class));
                        } else {
                            progressDialog.cancel();
                            Toast.makeText(getContext(),  getString(R.string.share_food_fail_msg), Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                            startActivity(new Intent(getContext(), Main3Activity.class));
                        }
                    }
                });
            }
        });
        return v;
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                    AlertDialog.THEME_HOLO_LIGHT, this, year, month, day);

            /*
                add(int field, int value)
                    Adds the given amount to a Calendar field.
             */
            // Add 3 days to Calendar
            calendar.add(Calendar.DATE, 1);

            dpd.getDatePicker().setMaxDate(calendar.getTimeInMillis()+(1000*60*60*24*3));

            // Subtract 6 days from Calendar updated date
            calendar.add(Calendar.DATE, -1);

            // Set the Calendar new date as minimum date of date picker
            dpd.getDatePicker().setMinDate(calendar.getTimeInMillis());

            // So, now date picker selectable date range is 7 days only

            // Return the DatePickerDialog
            return dpd;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the chosen date
            TextView tv = (TextView) getActivity().findViewById(R.id.tv);

            // Create a Date variable/object with user chosen date
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(0);
            cal.set(year, month, day, 0, 0, 0);
            Date chosenDate = cal.getTime();

            // Format the date using style and locale
            DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);
            String formattedDate = df.format(chosenDate);

            // Display the chosen date to app interface
            tv.setText(formattedDate);//formattedDate is the data that I want to pass to myFood.
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
//        Toast.makeText(parent.getContext(),text,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}



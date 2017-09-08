package net.ridhoperdana.asumu;

import android.app.DatePickerDialog;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import net.ridhoperdana.asumu.utility.AsumuSessionManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class AddNewTargetActivity extends AppCompatActivity{

    EditText inputStartDate;
    long duration;
    int normalExpense;
    AsumuSessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_target);
        inputStartDate = (EditText)findViewById(R.id.input_target_startdate);
        TextInputLayout title = (TextInputLayout) findViewById(R.id.input_layout_target_title);
        EditText titleInput = (EditText)findViewById(R.id.input_target_title);
        final EditText valueInput = (EditText)findViewById(R.id.input_target_value);
        final EditText endDateInput = (EditText)findViewById(R.id.input_target_enddate);
        sessionManager = new AsumuSessionManager(this);
        HashMap<String, String> user;
        user = sessionManager.getUserDetails();
        Log.d("username: ", user.get("user_name"));

        inputStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("date", "masuk");
                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        int s = month;
                        String a = year+"-"+(s+1)+"-"+day;
                        inputStartDate.setText(""+a);
                    }
                };

                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog d = new DatePickerDialog(AddNewTargetActivity.this, dateSetListener, mYear ,
                        mMonth, mDay);
                d.show();
                }
        });

        endDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("enddate", "masuk");
                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        int s = month;
                        String a = year+"-"+(s+1)+"-"+day;
                        endDateInput.setText(""+a);
                        duration = dateDiff(inputStartDate.getText().toString(), endDateInput.getText().toString());
                        normalExpense = Integer.valueOf(valueInput.getText().toString())/(int)duration;
                        Log.d("normal expense: ", String.valueOf(normalExpense));
                    }
                };

                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog d = new DatePickerDialog(AddNewTargetActivity.this, dateSetListener, mYear ,
                        mMonth, mDay);
                d.show();
            }
        });



    }

    public long dateDiff(String start, String end)
    {
        SimpleDateFormat startFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat endFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dstart = null;
        Date dend = null;
        try {
            dstart = startFormat.parse(start);
            dend = endFormat.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.setTime(dstart);
        endDate.setTime(dend);
        long diff = endDate.getTimeInMillis() - startDate.getTimeInMillis();
        long days = diff / (24 * 60 * 60 * 1000);
        return days;
    }

}

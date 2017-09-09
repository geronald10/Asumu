package net.ridhoperdana.asumu.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import net.ridhoperdana.asumu.R;
import net.ridhoperdana.asumu.utility.AsumuSessionManager;
import net.ridhoperdana.asumu.utility.NetworkUtils;
import net.ridhoperdana.asumu.utility.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddNewTargetActivity extends AppCompatActivity{

    EditText inputStartDate;
    long duration;
    int normalExpense;
    AsumuSessionManager sessionManager;
    private SweetAlertDialog pDialog;
    int penghasilan;
    EditText endDateInput;
    EditText valueInput, titleInput;
    HashMap<String, String> user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_target);
        inputStartDate = (EditText)findViewById(R.id.input_target_startdate);
        titleInput = (EditText)findViewById(R.id.input_target_title);
        valueInput = (EditText)findViewById(R.id.input_target_value);
        endDateInput = (EditText)findViewById(R.id.input_target_enddate);
        Button submitNewTarget = (Button)findViewById(R.id.submit_new_target);
        sessionManager = new AsumuSessionManager(this);
        user = sessionManager.getUserDetails();
        getUserDetail(user.get("user_name"));

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



        submitNewTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewTarget(titleInput.getText().toString(), valueInput.getText().toString(),
                        inputStartDate.getText().toString(), endDateInput.getText().toString(),
                        String.valueOf(normalExpense), user.get("user_name"));
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

    private void getUserDetail(final String username) {
        showLoading();

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                NetworkUtils.GET_USER_DETAIL+"?username="+username,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.d("response detail login: ", response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(!jsonObject.getString("penghasilan").equals("null"))
                            {
                                penghasilan = Integer.valueOf(jsonObject.getString("penghasilan"));
                                Log.d("penghasilan user:", jsonObject.getString("penghasilan"));
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

                                        Log.d("normal expense", String.valueOf(normalExpense));
                                        Log.d("Selisih expense", String.valueOf(penghasilan-normalExpense));
                                        if(penghasilan-normalExpense<0)
                                        {
                                            Toast.makeText(getApplicationContext(), "Rencana tidak memungkinan", Toast.LENGTH_SHORT).show();
                                            endDateInput.setError("Target not Possible");
                                        }
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
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                showErrorResult();
            }
        });
        VolleySingleton.getmInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void addNewTarget(final String target_desc, final String target_amount, final String target_startdate,
                                   final String target_duedate, final  String normal_expense, final String username) {
        showLoading();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                NetworkUtils.ADD_NEW_TARGET,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showSuccessResult();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                showErrorResult();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("target_desc", target_desc);
                params.put("target_amount", target_amount);
                params.put("target_startdate", target_startdate);
                params.put("target_duedate", target_duedate);
                params.put("normal_expense", normal_expense);
                params.put("username", username);

                return params;
            }
        };
        VolleySingleton.getmInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void showLoading() {
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void showSuccessResult() {
        if (pDialog.isShowing()) {
            pDialog.dismiss();
            new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Success!")
                    .setContentText("Account was successfully created")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                            finish();
                        }
                    })
                    .show();
        }
    }

    private void showErrorResult() {
        if (pDialog.isShowing()) {
            pDialog.dismiss();
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Failed!")
                    .setContentText("Internal Server Error")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                            finish();
                        }
                    })
                    .show();
        }
    }

}

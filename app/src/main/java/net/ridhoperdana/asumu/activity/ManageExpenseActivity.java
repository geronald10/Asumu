package net.ridhoperdana.asumu.activity;

import android.graphics.Color;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import net.ridhoperdana.asumu.R;
import net.ridhoperdana.asumu.utility.NetworkUtils;
import net.ridhoperdana.asumu.utility.VolleySingleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ManageExpenseActivity extends AppCompatActivity {

    private final String TAG = ManageExpenseActivity.class.getSimpleName();
    private ViewGroup mLinearLayout;
    private SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_expense);

        mLinearLayout = (ViewGroup) findViewById(R.id.linear_layout_parent);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Manage expense");
        ImageView ivLogo = (ImageView) findViewById(R.id.iv_logo_toolbar);
        ivLogo.setVisibility(View.GONE);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button buttonAddMoreExpense = (Button) findViewById(R.id.add_more_expense);
        Button buttonUpdateExpense = (Button) findViewById(R.id.update_daily_expense);
        buttonAddMoreExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMoreItems();
            }
        });

        buttonUpdateExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAllText(mLinearLayout);
            }
        });
    }

    private void addMoreItems() {
        Log.d("more Items", "added");
        View layoutItem = LayoutInflater.from(this).inflate(R.layout.input_layout, mLinearLayout, false);

        EditText keyExpense = (EditText) findViewById(R.id.input_key_daily);
        EditText valueExpense = (EditText) findViewById(R.id.input_value_daily);

//        keyExpense.setId(View.generateViewId());
//        valueExpense.setId(View.generateViewId());
//
//        Log.d("edtKeyExpense", String.valueOf(keyExpense.getId()));
//        Log.d("edtValueExpense", String.valueOf(valueExpense.getId()));

        mLinearLayout.addView(layoutItem);
    }

    private void getAllText(ViewGroup parent) {
        List<EditText> edtKeyExpense = new ArrayList<>();
        List<EditText> edtValueExpense = new ArrayList<>();

        for (int i = 0; i < parent.getChildCount(); i++) {
            Log.d("childCount", String.valueOf(parent.getChildCount()));
            View child = parent.getChildAt(i);
            if (child instanceof EditText) {
                Log.d("found", "edittext");
                if (i % 2 == 0) {
                    edtKeyExpense.add((EditText) child);
                } else if (i % 2 == 1){
                    edtValueExpense.add((EditText) child);
                }
            }
            if(child instanceof ViewGroup) {
                //Nested Q&A
                ViewGroup group = (ViewGroup)child;
                getAllText(group);
            }
        }

        Bundle bundle = getIntent().getExtras();
        Log.d("bundle", String.valueOf(bundle));
        storeText(bundle.getString("username"), bundle.getString("targetId"),
                bundle.getString("date"), edtKeyExpense, edtValueExpense);
    }

    private void storeText(final String username, final String idTarget, final String date,
                           final List<EditText> key, final List<EditText> value) {
        showLoading();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                NetworkUtils.HISTORY_DAILY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showSuccessResult();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                showErrorResult();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("id_target", idTarget);
                params.put("date", date);
                for (int i = 0; i < key.size(); i++) {
                    params.put(key.get(i).getText().toString(), value.get(i).getText().toString());
                    Log.d("key", key.get(i).getText().toString());
                    Log.d("value", key.get(i).getText().toString());
                }
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Log.d("cek call", "masuk");
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
                    .setContentText("Expense updated successfully")
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

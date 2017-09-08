package net.ridhoperdana.asumu.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import net.ridhoperdana.asumu.R;
import net.ridhoperdana.asumu.utility.AsumuSessionManager;
import net.ridhoperdana.asumu.utility.NetworkUtils;
import net.ridhoperdana.asumu.utility.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RegisterActivity extends AppCompatActivity {

//    private static final String TAG = LoginActivity.class.getSimpleName();
//
//    private Context mContext;
//    private EditText edtEmail;
//    private EditText edtPassword;
//    private Button btnSignIn;
//    private SweetAlertDialog pDialog;
//    private AsumuSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);

//        mContext = this;
//
//        // Set up the login form.
//        edtEmail = (EditText) findViewById(R.id.edt_email_login);
//        edtPassword = (EditText) findViewById(R.id.edt_password_login);
//        btnSignIn = (Button) findViewById(R.id.btn_sign_in);
//
//        // Session manager
//        session = new AsumuSessionManager(mContext);
//
//        // Check if user is already logged in or not
//        if (session.isLoggedIn()) {
//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        }
//
//        if (checkPlayServices()) {
//            btnSignIn.setOnClickListener(operation);
//        }
    }

//    View.OnClickListener operation = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            int id = view.getId();
//            switch (id) {
//                case R.id.btn_sign_in:
//                    String email = edtEmail.getText().toString().trim();
//                    String password = edtPassword.getText().toString().trim();
//
//                    if (!email.isEmpty() && !password.isEmpty()) {
//                        checkLogin(email, password);
//                    } else {
//                        Toast.makeText(getApplicationContext(), "Please enter the credentials!", Toast.LENGTH_LONG).show();
//                    }
//                    break;
//            }
//        }
//    };
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//    }
//
//    private void checkLogin(final String email, final String password) {
//        showLoading();
//        StringRequest stringRequest = new StringRequest(Request.Method.POST,
//                NetworkUtils.LOGIN_ASUMU, new Response.Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//                Log.d(TAG, "Login Response: " + response);
//                try {
//                    JSONArray jsonArray = new JSONArray(response);
//                    JSONObject jsonObject = jsonArray.getJSONObject(0);
//                    boolean status = jsonObject.getBoolean("status");
//                    if (status) {
//                        String email = jsonObject.getString("username");
//                        String nameUser = jsonObject.getString("nama_user");
//
//                        session.createLoginSession(nameUser, email);
//                        showSuccessResult();
//                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                        startActivity(intent);
//                        finish();
//                    } else {
//                        showErrorNotFound();
//                    }
//                } catch (JSONException e1) {
//                    e1.printStackTrace();
//                    showErrorResult();
//                }
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG, "Login Error: " + error.getMessage());
//                showErrorResult();
//            }
//        }) {
//
//            @Override
//            protected Map<String, String> getParams() {
//                // Posting parameters to login url
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("username", email);
//                params.put("password", password);
//
//                return params;
//            }
//        };
//        VolleySingleton.getmInstance(getApplicationContext()).addToRequestQueue(stringRequest);
//    }
//
//    private void showLoading() {
//        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
//        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
//        pDialog.setTitleText("Loading..");
//        pDialog.setCancelable(false);
//        pDialog.show();
//    }
//
//    private void showSuccessResult() {
//        if (pDialog.isShowing()) {
//            pDialog.dismiss();
//        }
//    }
//
//    private void showErrorResult() {
//        if (pDialog.isShowing()) {
//            pDialog.dismiss();
//            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
//                    .setTitleText("Failed!")
//                    .setContentText("Internal server error")
//                    .show();
//        }
//    }
//
//    private void showErrorNotFound() {
//        if (pDialog.isShowing()) {
//            pDialog.dismiss();
//            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
//                    .setTitleText("Failed!")
//                    .setContentText("User data not found / Incorrect Email or Password")
//                    .show();
//        }
//    }
}

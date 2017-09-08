package net.ridhoperdana.asumu.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private TextInputLayout tilUserNama, tilEmail, tilPassword, tilKonfirmPassword;
    private EditText edtNama, edtEmail, edtPassword, edtKonfirmasiPassword;
    private SweetAlertDialog pDialog;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tilUserNama = (TextInputLayout) findViewById(R.id.layout_user_nama);
        tilEmail = (TextInputLayout) findViewById(R.id.layout_email_register);
        tilPassword = (TextInputLayout) findViewById(R.id.layout_password_register);
        tilKonfirmPassword = (TextInputLayout) findViewById(R.id.layout_confirm_password_register);

        edtNama = (EditText) findViewById(R.id.edt_user_nama);
        edtEmail = (EditText) findViewById(R.id.edt_email_register);
        edtPassword = (EditText) findViewById(R.id.edt_password_register);
        edtKonfirmasiPassword = (EditText) findViewById(R.id.edt_confirm_password_register);

        btnRegister = (Button) findViewById(R.id.btnRegister);

        edtPassword.addTextChangedListener(new MyTextWatcher(edtPassword));
        edtKonfirmasiPassword.addTextChangedListener(new MyTextWatcher(edtKonfirmasiPassword));

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kirimForm();
            }
        });
    }

    private boolean validasiUserNama() {
        if (edtNama.getText().toString().trim().isEmpty()) {
            tilUserNama.setError(getString(R.string.error_msg_nama));
            edtNama.requestFocus();
            return false;
        } else {
            tilUserNama.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validasiEmail() {
        if (edtEmail.getText().toString().trim().isEmpty()) {
            tilEmail.setError(getString(R.string.error_msg_email));
            edtEmail.requestFocus();
            return false;
        } else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(edtEmail.getText().toString()).matches()) {
            tilEmail.setError(getString(R.string.error_msg_email2));
            edtEmail.requestFocus();
            return false;
        } else {
            tilEmail.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validasiPassword() {
        if (edtPassword.getText().toString().trim().isEmpty()) {
            tilPassword.setError(getString(R.string.error_msg_password));
            edtPassword.requestFocus();
            return false;
        } else {
            tilPassword.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validasiKonfirmasiPassword() {
        if (edtKonfirmasiPassword.getText().toString().trim().isEmpty()) {
            tilKonfirmPassword.setError(getString(R.string.error_msg_konfirm_password));
            edtKonfirmasiPassword.requestFocus();
            return false;
        } else {
            tilKonfirmPassword.setErrorEnabled(false);
        }
        if (!edtPassword.getText().toString().equals(edtKonfirmasiPassword.getText().toString())) {
            tilKonfirmPassword.setError(getString(R.string.error_msg_konfirm_password_2));
            edtKonfirmasiPassword.requestFocus();
            return false;
        } else {
            tilKonfirmPassword.setErrorEnabled(false);
        }
        return true;
    }

    private void kirimForm() {
        if (!validasiUserNama())
            return;
        if (!validasiEmail())
            return;
        if (!validasiPassword())
            return;
        if (!validasiKonfirmasiPassword())
            return;
        kirimFormDataDiri(edtNama.getText().toString(), edtEmail.getText().toString(), edtPassword.getText().toString());
    }

    private void kirimFormDataDiri(final String nama, final String email, final String password) {
        showLoading();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                NetworkUtils.REGISTER_ASUMU,
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
                params.put("username", nama);
                params.put("password", email);
                params.put("nama_user", password);

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

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            switch (view.getId()) {
                case R.id.edt_user_nama:
                    validasiUserNama();
                    break;
                case R.id.edt_email_register:
                    validasiEmail();
                    break;
                case R.id.edt_password_register:
                    validasiPassword();
                    break;
                case R.id.edt_confirm_password_register:
                    validasiKonfirmasiPassword();
                    break;
            }
        }
    }
}

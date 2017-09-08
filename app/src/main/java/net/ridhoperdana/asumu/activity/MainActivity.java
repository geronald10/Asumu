package net.ridhoperdana.asumu.activity;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import net.ridhoperdana.asumu.R;
import net.ridhoperdana.asumu.fragment.ItemAccountFragment;
import net.ridhoperdana.asumu.fragment.ItemAchievementFragment;
import net.ridhoperdana.asumu.fragment.ItemHistoryFragment;
import net.ridhoperdana.asumu.fragment.ItemHomeFragment;
import net.ridhoperdana.asumu.helper.BottomNavigationViewHelper;
import net.ridhoperdana.asumu.utility.AsumuSessionManager;
import net.ridhoperdana.asumu.utility.NetworkUtils;
import net.ridhoperdana.asumu.utility.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private SweetAlertDialog pDialog;
    AsumuSessionManager sessionManager;
    public String penghasilan = null;
    HashMap<String, String> user;
    EditText inputPenghasilan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkCameraPermission() && checkStorageRPermission() && checkStorageWPermission()) {
                Log.e("permission", "Permission already granted.");
            } else {
                requestPermission();
            }
        }

        sessionManager = new AsumuSessionManager(this);
        user = sessionManager.getUserDetails();
        getUserDetail(user.get("user_name"));

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.action_item1:
                                selectedFragment = ItemHomeFragment.newInstance();
                                break;
                            case R.id.action_item2:
                                selectedFragment = ItemHistoryFragment.newInstance();
                                break;
                            case R.id.action_item3:
                                selectedFragment = ItemAchievementFragment.newInstance();
                                break;
                            case R.id.action_item4:
                                selectedFragment = ItemAccountFragment.newInstance();
                                break;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, ItemHomeFragment.newInstance());
        transaction.commit();

        //Used to select an item programmatically
        //bottomNavigationView.getMenu().getItem(2).setChecked(true);
    }

    private boolean checkCameraPermission() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CAMERA);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkStorageRPermission() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkStorageWPermission() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.INTERNET,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE},
                PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this,
                            "Permission accepted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this,
                            "Permission denied", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void updateIncome(final String username, final String income) {
        showLoading();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                NetworkUtils.UPDATE_PENGHASILAN,
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
                params.put("username", username);
                params.put("penghasilan", income);

                return params;
            }
        };
        VolleySingleton.getmInstance(getApplicationContext()).addToRequestQueue(stringRequest);
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
//                                penghasilan = jsonObject.getString("penghasilan");
                                Log.d("penghasilan nol: ", response.toString());
                            }
                            else
                            {
                                Log.d("penghasilan: ", jsonObject.getString("penghasilan"));
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                // Get the layout inflater
                                LayoutInflater inflater = getLayoutInflater();
                                final View view = inflater.inflate(R.layout.custom_dialog_input_penghasilan, null);
                                builder.setView(view)
                                        // Add action buttons
                                        .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int id) {
                                                inputPenghasilan = (EditText)view.findViewById(R.id.input_user_penghasilan);
                                                updateIncome(user.get("user_name"), inputPenghasilan.getText().toString());
                                            }
                                        })
                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                                builder.show();
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
                    .setContentText("Income is updated successfully")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
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

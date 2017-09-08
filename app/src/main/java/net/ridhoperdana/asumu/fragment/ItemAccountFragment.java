package net.ridhoperdana.asumu.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import net.ridhoperdana.asumu.R;
import net.ridhoperdana.asumu.activity.MainActivity;
import net.ridhoperdana.asumu.utility.AsumuSessionManager;
import net.ridhoperdana.asumu.utility.NetworkUtils;
import net.ridhoperdana.asumu.utility.RupiahCurrencyFormat;
import net.ridhoperdana.asumu.utility.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ItemAccountFragment extends Fragment {

    private AsumuSessionManager session;
    private SweetAlertDialog pDialog;
    TextView namauser, user_name, password, penghasilan;

    public static ItemAccountFragment newInstance() {
        ItemAccountFragment fragment = new ItemAccountFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_account, container, false);

        session = new AsumuSessionManager(getActivity());
        namauser = (TextView)view.findViewById(R.id.tvNamaUser);
        user_name = (TextView)view.findViewById(R.id.tvEmailUser);
        penghasilan = (TextView)view.findViewById(R.id.tvMoneyAmount);
        getUserDetail(session.getUserDetails().get("user_name"));

        Button btnLogout = (Button) view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(action);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    View.OnClickListener action = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnLogout:
                    showLogoutWarning();
            }
        }
    };

    public void showLogoutWarning() {
        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Logout?")
                .setContentText("Are you sure to logout from your account?")
                .setCancelText("Cancel")
                .setConfirmText("Yes")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        Toast.makeText(getActivity(), "Logout", Toast.LENGTH_LONG).show();
                        session.logoutUser();
                        getActivity().finish();
                    }
                })
                .show();
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
                            RupiahCurrencyFormat rupiahCurrencyFormat = new RupiahCurrencyFormat();
                            JSONObject jsonObject = new JSONObject(response);
                            namauser.setText(jsonObject.get("nama_user").toString());
                            user_name.setText(jsonObject.get("username").toString());
                            String penghasilanUser = jsonObject.get("penghasilan").toString();
                            Log.d("penghasilan User", penghasilanUser);
                            if (penghasilanUser.equals("null") || penghasilanUser.equals("0"))
                                penghasilan.setText(rupiahCurrencyFormat.toRupiahFormat("0"));
                            else
                                penghasilan.setText(rupiahCurrencyFormat.toRupiahFormat(penghasilanUser));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                showErrorResult();
            }
        });
        VolleySingleton.getmInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private void showLoading() {
        pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void showSuccessResult() {
        if (pDialog.isShowing()) {
            pDialog.dismiss();
            new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
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
            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Failed!")
                    .setContentText("Internal Server Error")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                            getActivity().finish();
                        }
                    })
                    .show();
        }
    }

}

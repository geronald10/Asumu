package net.ridhoperdana.asumu.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import net.ridhoperdana.asumu.activity.AddDefaultExpenseActivity;
import net.ridhoperdana.asumu.adapter.AdapterListHistory;
import net.ridhoperdana.asumu.model.ListofTargetModel;
import net.ridhoperdana.asumu.activity.ManageExpenseActivity;
import net.ridhoperdana.asumu.activity.AddNewTargetActivity;
import net.ridhoperdana.asumu.R;
import net.ridhoperdana.asumu.utility.AsumuSessionManager;
import net.ridhoperdana.asumu.utility.DateUtils;
import net.ridhoperdana.asumu.utility.NetworkUtils;
import net.ridhoperdana.asumu.utility.RupiahCurrencyFormat;
import net.ridhoperdana.asumu.utility.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class ItemHomeFragment extends Fragment implements View.OnClickListener{

    private SweetAlertDialog pDialog;
    AsumuSessionManager sessionManager;
    HashMap<String, String> user;
    RecyclerView recyclerViewHistory;
    LinearLayoutManager linearLayoutManager;
    List<ListofTargetModel> listofTargetModels;
    Context context;

    public static ItemHomeFragment newInstance() {
        ItemHomeFragment fragment = new ItemHomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_home, container, false);
        recyclerViewHistory = (RecyclerView)view.findViewById(R.id.list_history);
        Button submitDefaultExpense = (Button)view.findViewById(R.id.submit_default_expense);
        sessionManager = new AsumuSessionManager(getActivity());
        user = sessionManager.getUserDetails();
        getActiveHistory(user.get("user_name"));
        context = getContext();

        FloatingActionButton buttonAddTarget = (FloatingActionButton)view.findViewById(R.id.add_target_button);
        buttonAddTarget.setOnClickListener(this);
        submitDefaultExpense.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.add_target_button)
        {
            Log.d("berhasil", "add");
            Intent intent = new Intent(getActivity(), AddNewTargetActivity.class);
            startActivity(intent);
        }
        else if(view.getId()==R.id.submit_default_expense)
        {
            Log.d("berhasil", "tambah expense");
//            Bundle bundle = new Bundle();
//            bundle.putString("username", user.get("user_name"));
            Intent intent = new Intent(getActivity(), AddDefaultExpenseActivity.class);
            intent.putExtra("username", user.get("user_name"));
            startActivity(intent);
        }
    }

    private void getActiveHistory(final String username) {
        showLoading();

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                NetworkUtils.ACTIVE_TARGET+"?username="+username,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.d("response detail login: ", response.toString());
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            listofTargetModels = new ArrayList<>();
                            for(int i=0; i<jsonArray.length(); i++)
                            {
                                RupiahCurrencyFormat rupiahCurrencyFormat = new RupiahCurrencyFormat();
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ListofTargetModel listofTargetModel = new ListofTargetModel(jsonObject.getString("target_desc"),
                                        "xxxx", rupiahCurrencyFormat.toRupiahFormat(jsonObject.getString("target_amount")),
                                        jsonObject.getString("target_startdate"), jsonObject.getString("target_duedate"),
                                        jsonObject.getString("offset"), jsonObject.getString("status"),
                                        jsonObject.getString("id_target"));
                                listofTargetModels.add(listofTargetModel);
                            }
                            AdapterListHistory adapterListHistory = new AdapterListHistory(listofTargetModels,
                                    getContext(), new AdapterListHistory.ListHistoryOnClickHandler() {
                                @Override
                                public void onClick(ListofTargetModel listofTargetModel) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("username", sessionManager.getUserDetails().get("user_name"));
                                    bundle.putString("targetId", listofTargetModel.getId_target());
                                    bundle.putString("date", DateUtils.getCurrentDate(context));
                                    Intent intent = new Intent(getActivity(), ManageExpenseActivity.class);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });
                            recyclerViewHistory.setAdapter(adapterListHistory);
                            linearLayoutManager = new LinearLayoutManager(getActivity());
                            recyclerViewHistory.setLayoutManager(linearLayoutManager);
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

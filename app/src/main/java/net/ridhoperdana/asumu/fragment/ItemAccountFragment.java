package net.ridhoperdana.asumu.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import net.ridhoperdana.asumu.R;
import net.ridhoperdana.asumu.utility.AsumuSessionManager;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ItemAccountFragment extends Fragment {

    private AsumuSessionManager session;
    private SweetAlertDialog pDialog;

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

}

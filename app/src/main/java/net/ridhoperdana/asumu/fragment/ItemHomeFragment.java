package net.ridhoperdana.asumu.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.ridhoperdana.asumu.AddNewTargetActivity;
import net.ridhoperdana.asumu.R;

public class ItemHomeFragment extends Fragment implements View.OnClickListener{

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

        FloatingActionButton buttonAddTarget = (FloatingActionButton)view.findViewById(R.id.add_target_button);
        buttonAddTarget.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.add_target_button)
        {
            Log.d("berhasil", "add");
            Intent intent = new Intent(getActivity(), AddNewTargetActivity.class);
            startActivity(intent);
        }
    }
}

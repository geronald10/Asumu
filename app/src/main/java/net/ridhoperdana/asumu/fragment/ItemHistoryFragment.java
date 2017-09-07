package net.ridhoperdana.asumu.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.ridhoperdana.asumu.AdapterListHistory;
import net.ridhoperdana.asumu.R;

public class ItemHistoryFragment extends Fragment {

    RecyclerView recyclerViewHistory;
    LinearLayoutManager linearLayoutManager;

    public static ItemHistoryFragment newInstance() {
        ItemHistoryFragment fragment = new ItemHistoryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.fragment_item_history, container, false);
//        return inflater.inflate(R.layout.fragment_item_history, container, false);
        recyclerViewHistory = (RecyclerView)view.findViewById(R.id.list_history);
        AdapterListHistory adapterListHistory = new AdapterListHistory(getContext());
        recyclerViewHistory.setAdapter(adapterListHistory);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewHistory.setLayoutManager(linearLayoutManager);
        return view;
    }
}

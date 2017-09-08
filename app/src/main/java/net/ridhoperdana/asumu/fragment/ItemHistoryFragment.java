package net.ridhoperdana.asumu.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.ridhoperdana.asumu.AdapterListHistory;
import net.ridhoperdana.asumu.HistoryDetailActivity;
import net.ridhoperdana.asumu.ListofTargetModel;
import net.ridhoperdana.asumu.R;
import net.ridhoperdana.asumu.utility.RupiahCurrencyFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ItemHistoryFragment extends Fragment implements AdapterListHistory.ListHistoryOnClickHandler{

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

        Random random = new Random();
        int max=10;
        int min=1;
        int min_harga = 50000;
        int max_harga = 1000000;
        int nilai_random, harga_random;
        List<ListofTargetModel> listofTargetModels = new ArrayList<>();
        for (int i=0; i<10; i++)
        {
            RupiahCurrencyFormat rupiahCurrencyFormat = new RupiahCurrencyFormat();
            harga_random = random.nextInt(max_harga - min_harga +1) + min_harga;
            nilai_random = random.nextInt(max - min +1) +min;
            ListofTargetModel listofTargetModel = new ListofTargetModel("New Laptop", "xxxx",
                    rupiahCurrencyFormat.toRupiahFormat(String.valueOf(harga_random)),
                    "2017-8-30", "2017-12-30", "0", "Not Finished", String.valueOf(i));
            listofTargetModels.add(listofTargetModel);
        }

        Log.d("list of target: ", String.valueOf(listofTargetModels.size()));
        AdapterListHistory adapterListHistory = new AdapterListHistory(listofTargetModels, getContext(), this);
        recyclerViewHistory.setAdapter(adapterListHistory);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewHistory.setLayoutManager(linearLayoutManager);
        return view;
    }

    @Override
    public void onClick(ListofTargetModel listofTargetModel) {
        Bundle bundle = new Bundle();
//        bundle.putString("coba", listofTargetModel.);
        Intent intent = new Intent(getActivity(), HistoryDetailActivity.class);
        startActivity(intent);
    }
}

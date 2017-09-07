package net.ridhoperdana.asumu;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by RIDHO on 9/7/2017.
 */

public class AdapterListHistory extends RecyclerView.Adapter<ViewHolderListHistory>{

    Context context;

    @Override
    public ViewHolderListHistory onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_history, parent, false);
        ViewHolderListHistory holderListHistory = new ViewHolderListHistory(v);
        return holderListHistory;
    }

    @Override
    public void onBindViewHolder(ViewHolderListHistory holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public AdapterListHistory(Context context) {
        this.context = context;
    }
}

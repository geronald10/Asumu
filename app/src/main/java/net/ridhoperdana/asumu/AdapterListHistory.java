package net.ridhoperdana.asumu;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

/**
 * Created by RIDHO on 9/7/2017.
 */

public class AdapterListHistory extends RecyclerView.Adapter<ViewHolderListHistory>{

    List<ListofTarget> list = Collections.emptyList();
    Context context;

    @Override
    public ViewHolderListHistory onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_history, parent, false);
        ViewHolderListHistory holderListHistory = new ViewHolderListHistory(v);
        return holderListHistory;
    }

    @Override
    public void onBindViewHolder(ViewHolderListHistory holder, int position) {
        ListofTarget listofTarget = list.get(position);
        holder.titleTarget.setText(listofTarget.getTargetTitle());
        holder.statusTarget.setText(listofTarget.getTargetStatus());
        holder.savingTarget.setText(listofTarget.getTargetAmount());
        holder.imageTarget.setImageResource(R.drawable.community);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public AdapterListHistory(List<ListofTarget> list, Context context) {
        this.list = list;
        this.context = context;
    }
}

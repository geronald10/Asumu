package net.ridhoperdana.asumu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.ridhoperdana.asumu.R;
import net.ridhoperdana.asumu.model.ListofTargetModel;
import net.ridhoperdana.asumu.utility.RupiahCurrencyFormat;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by RIDHO on 9/7/2017.
 */

public class AdapterListHistory extends RecyclerView.Adapter<AdapterListHistory.ViewHolderListHistory>{

    List<ListofTargetModel> list = Collections.emptyList();
    Context context;
    RupiahCurrencyFormat rupiahCurrencyFormat = new RupiahCurrencyFormat();

    public interface ListHistoryOnClickHandler
    {
        void onClick(ListofTargetModel listofTargetModel);
    }

    public ListHistoryOnClickHandler listHistoryOnClickHandler;

    @Override
    public ViewHolderListHistory onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_history, parent, false);
        ViewHolderListHistory holderListHistory = new ViewHolderListHistory(v);
        return holderListHistory;
    }

    @Override
    public void onBindViewHolder(ViewHolderListHistory holder, int position) {
        ListofTargetModel listofTargetModel = list.get(position);
        holder.titleTarget.setText(listofTargetModel.getTarget_desc());
        holder.statusTarget.setText(listofTargetModel.getStatus());
        holder.savingTarget.setText(listofTargetModel.getTarget_amount());
        holder.imageTarget.setImageResource(R.drawable.community);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public AdapterListHistory(List<ListofTargetModel> list, Context context, ListHistoryOnClickHandler listHistoryOnClickHandler) {
        this.list = list;
        this.context = context;
        this.listHistoryOnClickHandler = listHistoryOnClickHandler;
    }

    public class ViewHolderListHistory extends RecyclerView.ViewHolder implements View.OnClickListener{

        CircleImageView imageTarget;
        TextView titleTarget, savingTarget, statusTarget;

        public ViewHolderListHistory(View itemView) {
            super(itemView);
            imageTarget = (CircleImageView) itemView.findViewById(R.id.image_target);
            titleTarget = (TextView)itemView.findViewById(R.id.title_target);
            savingTarget = (TextView)itemView.findViewById(R.id.saving_target);
            statusTarget = (TextView)itemView.findViewById(R.id.status_target);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            listHistoryOnClickHandler.onClick(list.get(adapterPosition));
        }
    }
}



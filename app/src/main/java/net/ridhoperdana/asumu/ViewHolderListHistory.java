package net.ridhoperdana.asumu;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by RIDHO on 9/7/2017.
 */

public class ViewHolderListHistory extends RecyclerView.ViewHolder {

    CircleImageView imageTarget;
    TextView titleTarget, savingTarget, statusTarget;

    public ViewHolderListHistory(View itemView) {
        super(itemView);
        imageTarget = (CircleImageView) itemView.findViewById(R.id.image_target);
        titleTarget = (TextView)itemView.findViewById(R.id.title_target);
        savingTarget = (TextView)itemView.findViewById(R.id.saving_target);
        statusTarget = (TextView)itemView.findViewById(R.id.status_target);
    }
}

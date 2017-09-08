package net.ridhoperdana.asumu.fragment;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import net.ridhoperdana.asumu.R;
import net.ridhoperdana.asumu.activity.MainActivity;

public class ItemAchievementFragment extends Fragment {

    private Button btnSendNotif;
    private NotificationManager mNotifyManager;
    private static final int NOTIFICATION_ID = 0;


    public static ItemAchievementFragment newInstance() {
        ItemAchievementFragment fragment = new ItemAchievementFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNotifyManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_achievement, container, false);
        btnSendNotif = (Button) view.findViewById(R.id.btnCekNotif);
        btnSendNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification();
            }
        });

        return view;
    }

    public void sendNotification() {
        Intent notificationIntent = new Intent(getActivity(), MainActivity.class);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(getActivity(), NOTIFICATION_ID,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(getActivity())
                .setContentTitle("Confirm your expenses!")
                .setContentText("or It will be send automatically")
                .setSmallIcon(R.drawable.ic_notification_logo)
                .setContentIntent(notificationPendingIntent)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

        Notification myNotification = notifyBuilder.build();
        mNotifyManager.notify(NOTIFICATION_ID, myNotification);
    }
}

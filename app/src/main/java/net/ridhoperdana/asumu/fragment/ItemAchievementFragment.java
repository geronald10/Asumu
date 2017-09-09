package net.ridhoperdana.asumu.fragment;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import net.ridhoperdana.asumu.utility.NotificationReceiver;
import net.ridhoperdana.asumu.R;

import java.util.Calendar;

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

        Calendar calendar = Calendar.getInstance();
        Log.d("hehehe", "bisa harusnya");

        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 55);
        calendar.set(Calendar.SECOND, 10);

        Intent notificationIntent = new Intent(getActivity(), NotificationReceiver.class);

        PendingIntent notificationPendingIntent = PendingIntent.getBroadcast(getActivity(), 100,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, notificationPendingIntent);
    }
}

package com.nutrihealth.broadcastReceivers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.nutrihealth.R;
import com.nutrihealth.activities.AlarmsActivity;
import com.nutrihealth.activities.HomeActivity;
import com.nutrihealth.app.NutriHealthApp;
import com.nutrihealth.constants.Constants;

/**
 * Created by Teo on 4/3/2018.
 */

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Vibrate the mobile phone
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(2000);

        AlarmsActivity.Alarms alarmType = AlarmsActivity.Alarms.BREAKFAST;

        if(intent.hasExtra(Constants.REQUEST_CODE_FOR_ALARM)){
            int requestCode = intent.getIntExtra(Constants.REQUEST_CODE_FOR_ALARM, 1);
            switch (requestCode){
                case Constants.ALARM_BREAKFAST_CODE:
                    alarmType = AlarmsActivity.Alarms.BREAKFAST;
                    break;
                case Constants.ALARM_FIRST_SNACK_CODE:
                    alarmType = AlarmsActivity.Alarms.FIRST_SNACK;
                    break;
                case Constants.ALARM_LUNCH_CODE:
                    alarmType = AlarmsActivity.Alarms.LUNCH;
                    break;
                case Constants.ALARM_SECOND_SNACK_CODE:
                    alarmType = AlarmsActivity.Alarms.SECOND_SNACK;
                    break;
                case Constants.ALARM_DINER_CODE:
                    alarmType = AlarmsActivity.Alarms.DINER;
                    break;

            }
        }

        sendNotification(context, alarmType);


    }

    public void sendNotification(Context context, AlarmsActivity.Alarms alarmType) {

        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Ora mesei")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Este timpul pentru a lua o mica pauza si a te bucura de masa"))
                .setContentText("Este timpul pentru a lua o mica pauza si a te bucura de masa")
                .setAutoCancel(true)
                .setSound(defaultSoundUri);


        Intent intent = new Intent(context, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        notificationBuilder.setContentIntent(pendingIntent);


        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}

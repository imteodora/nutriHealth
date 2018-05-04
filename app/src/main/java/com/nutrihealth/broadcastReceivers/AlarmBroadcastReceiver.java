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
            sendMealNotification(context);
        }else{
            sendWaterNotification(context);
        }




    }

    private void sendWaterNotification(Context context) {

        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_water_meal_ic)
                .setContentTitle(context.getResources().getString(R.string.notification_water_title))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(context.getResources().getString(R.string.notification_water_text)))
                .setContentText(context.getResources().getString(R.string.notification_water_text))
                .setAutoCancel(true)
                .setSound(defaultSoundUri);


        Intent intent = new Intent(context, AlarmsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        notificationBuilder.setContentIntent(pendingIntent);


        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

    public void sendMealNotification(Context context) {

        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_meal_ic)
                .setContentTitle(context.getResources().getString(R.string.notification_title))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(context.getResources().getString(R.string.notification_text)))
                .setContentText(context.getResources().getString(R.string.notification_text))
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

package com.nsappsstudio.shravanimela.Adapter;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.nsappsstudio.shravanimela.Camera;
import com.nsappsstudio.shravanimela.Gallery;
import com.nsappsstudio.shravanimela.Intro;
import com.nsappsstudio.shravanimela.R;
import com.nsappsstudio.shravanimela.Video2;

import java.util.Random;

public class MyFirebaseInstanceIDService extends FirebaseMessagingService {

    private Context context;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        SharedPreferences sharedPreferences1 = getSharedPreferences("Info", Context.MODE_PRIVATE);
        String project=sharedPreferences1.getString("project",null);

        context=this;

        if (remoteMessage.getNotification()!=null){
            //sendNotification(remoteMessage.getNotification().getBody());
            toastMessage(remoteMessage.getNotification().getBody());
        }
        if (remoteMessage.getData().size()>0){
            String dataType=remoteMessage.getData().get("type");
            String data1=remoteMessage.getData().get("data1");
            String data2=remoteMessage.getData().get("data2");
            if (dataType != null) {
                if (dataType.equals("Live")){
                    sendLiveNotification(data1,data2);
                }else if (dataType.equals("gallery")){
                    sendVotingRequestNotification(data1,data2);
                }else{

                }

            }
        }
    }


    private void sendLiveNotification(String title,String subtitle ) {


        Intent intent = new Intent(context, Video2.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Uri sound = Uri.parse("android.resource://"
                + context.getPackageName() + "/" + R.raw.two_pop);
        int random = new Random().nextInt(1000);


        sendNotification(intent, sound, title,subtitle,"Live Video", random);


    }
    private void sendVotingRequestNotification(String title,String subtitle ) {


                Intent intent = new Intent(context, Gallery.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                Uri sound = Uri.parse("android.resource://"
                        + context.getPackageName() + "/" + R.raw.pop);
                int random = new Random().nextInt(1000);


                sendNotification(intent, sound, title, subtitle , "Pic of the Day", random);



    }
    private void sendNotification(Intent intent, Uri sound, String title, String subtitle, String channelName,int notificationId){
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.project_id);
        long[] vibrate = { 0, 100, 200, 300 };

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(R.drawable.shravani_mela)
                        .setContentTitle(title)
                        .setContentText(subtitle)
                        .setAutoCancel(true)
                        .setSound(sound)
                        .setContentIntent(pendingIntent)
                        .setVibrate(vibrate);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(notificationId /* ID of notification */, notificationBuilder.build());
    }
    private void toastMessage(String message){
        //Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

}

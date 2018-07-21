package com.nsappsstudio.shravanimela;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;


public class NotificationServices extends Service {
    private long updatedTimeStamp;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        final DatabaseReference mDatabaseReference=FirebaseDatabase.getInstance().getReference();
        final SharedPreferences sharedPref=getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        Long defaultTimeLong=System.currentTimeMillis();
        String defaultTime=defaultTimeLong.toString();
        final String updatedTimeStampString=sharedPref.getString("updatedTS",defaultTime);
        updatedTimeStamp=Long.parseLong(updatedTimeStampString);
        //toastMessage(defaultTime);
        
        
        Query mNotificationRef= mDatabaseReference.child("NoticeKey").child("Notification").orderByValue().startAt(updatedTimeStamp);
        mNotificationRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                final String noticeKey = dataSnapshot.getKey();
                Long tsLong = dataSnapshot.getValue(Long.class);
                //toastMessage(noticeKey);
                if (tsLong != null) {
                    tsLong = tsLong + 1;
                    updatedTimeStamp = tsLong;
                    String ts = String.valueOf(tsLong);
                    //toastMessage(ts);

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("updatedTS", ts);
                    editor.apply();

                    assert noticeKey != null;
                    DatabaseReference mNoticeRef= mDatabaseReference.child("Notification").child(noticeKey);
                    mNoticeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            String title= dataSnapshot.child("title").getValue(String.class);
                            String body= dataSnapshot.child("body").getValue(String.class);
                            String type= dataSnapshot.child("type").getValue(String.class);
                            //toastMessage(title);

                            int requestCode;
                            Random r = new Random();
                            requestCode = r.nextInt(8000 - 5);

                            Intent intentNew = new Intent(getApplicationContext(), Notification.class);
                            intentNew.putExtra("noticeId", noticeKey);
                            PendingIntent pendingIntent = PendingIntent.getActivity(NotificationServices.this, requestCode, intentNew, PendingIntent.FLAG_UPDATE_CURRENT);

                            Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(NotificationServices.this, "2")
                                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                                    .setContentTitle("["+type+"]"+title)
                                    .setContentText(body)
                                    .setPriority(NotificationCompat.DEFAULT_SOUND)
                                    // Set the intent that will fire when the user taps the notification
                                    .setContentIntent(pendingIntent)
                                    .setSound(notificationSound)
                                    .setAutoCancel(true);
                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(NotificationServices.this);
                            int notificationId;
                            Random n = new Random();
                            notificationId = n.nextInt(8000 - 5);
                            // notificationId is a unique int for each notification that you must define
                            notificationManager.notify(notificationId, mBuilder.build());



                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }


                }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent){
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 5, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        assert alarmService != null;
        alarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartServicePendingIntent);

        super.onTaskRemoved(rootIntent);
    }
    private void toastMessage(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();

    }
}

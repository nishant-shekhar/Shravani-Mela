package com.nsappsstudio.shravanimela;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Notification extends AppCompatActivity {

    private DatabaseReference mDatabaseReference;
    private TextView nTitle;
    private TextView nBody;
    private TextView nType;
    private CardView nCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        mDatabaseReference= FirebaseDatabase.getInstance().getReference();
        nCardView=findViewById(R.id.full_notification_card);
        nCardView.setVisibility(View.GONE);
        nType=findViewById(R.id.notification_type);
        nTitle=findViewById(R.id.notification_title);
        nBody=findViewById(R.id.notification_body);


        try {
            Intent intent=getIntent();
            String noticeId = intent.getStringExtra("noticeId");
            if (noticeId!=null){

                showNotice(noticeId);
            }
        }catch (NullPointerException error){
            //do nothing
        }
       showAllNotifications();


    }
    private void showNotice(String noticeId){

        DatabaseReference mNoticeRef= mDatabaseReference.child("Notification").child(noticeId);


        mNoticeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                growAnim(nCardView);
                String title= dataSnapshot.child("title").getValue(String.class);
                String body= dataSnapshot.child("body").getValue(String.class);
                String type= dataSnapshot.child("type").getValue(String.class);

                nTitle.setText(title);
                nBody.setText(body);
                nType.setText(type);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void exitNotice(final View view){

        shrinkAnim(nCardView);
        view.setEnabled(false);
        new CountDownTimer(500,500){
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                view.setEnabled(true);
            }
        }.start();
    }
    private void growAnim(final View view){
        Animation entry = AnimationUtils.loadAnimation(this, R.anim.grow);
        view.startAnimation(entry);
        new CountDownTimer(20,20){
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                view.setVisibility(View.VISIBLE);
            }
        }.start();
    }
    private void shrinkAnim(final View view){
        Animation exit = AnimationUtils.loadAnimation(this, R.anim.shrink);
        view.startAnimation(exit);
        new CountDownTimer(500,500){
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                view.setVisibility(View.GONE);

            }
        }.start();
    }
    private void showAllNotifications(){

    }

}

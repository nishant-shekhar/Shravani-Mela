package com.nsappsstudio.shravanimela;

import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateFormat;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Notification extends AppCompatActivity {

    private DatabaseReference mDatabaseReference;
    private TextView nTitle;
    private TextView nBody;
    private TextView nType;
    private CardView nCardView;
    private List<CardListItem> cardListItems;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Intent intent = new Intent(this, NotificationPre.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else {
            // Implement this feature without material design


            setContentView(R.layout.activity_notification);

            mDatabaseReference = FirebaseDatabase.getInstance().getReference();
            nCardView = findViewById(R.id.full_notification_card);
            nCardView.setVisibility(View.GONE);
            nType = findViewById(R.id.notification_type);
            nTitle = findViewById(R.id.notification_title);
            nBody = findViewById(R.id.notification_body);
            progressBar = findViewById(R.id.n_progressBar);
            progressBar.setVisibility(View.VISIBLE);
            showAllNotifications();

            try {
                Intent intent = getIntent();
                String noticeId = intent.getStringExtra("noticeId");
                if (noticeId != null) {

                    showNotice(noticeId);
                }
            } catch (NullPointerException error) {
                //do nothing
            }

        }
    }

    private void showAllNotifications() {
        recyclerView=findViewById(R.id.n_recyclerView);
        recyclerView.hasFixedSize();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);

        cardListItems = new ArrayList<>();

        DatabaseReference mNoticeRef= mDatabaseReference.child("Notification");
        mNoticeRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                progressBar.setVisibility(View.GONE);
                String title= dataSnapshot.child("title").getValue(String.class);
                String body= dataSnapshot.child("body").getValue(String.class);
                String type= dataSnapshot.child("type").getValue(String.class);

                String date = null;
                try {
                    Long timeStamp= dataSnapshot.child("ts").getValue(Long.class);
                    Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                    cal.setTimeInMillis(timeStamp);
                    date = DateFormat.format("dd-MM-yyyy", cal).toString();
                }catch (NullPointerException e){
                    //do nothing
                }


                CardListItem cardListItem = new CardListItem(title,date,body,type,1);
                cardListItems.add(cardListItem);
                RecyclerView.Adapter adapter = new CardAdapter(cardListItems, Notification.this);
                recyclerView.setAdapter(adapter);

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




    }

    private void showNotice(String noticeId){

        DatabaseReference mNoticeRef= mDatabaseReference.child("Notification").child(noticeId);

        mNoticeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recyclerView.setVisibility(View.GONE);

                growAnim(nCardView);
                String title= dataSnapshot.child("title").getValue(String.class);
                String body= dataSnapshot.child("body").getValue(String.class);
                String type= dataSnapshot.child("type").getValue(String.class);

                nTitle.setText(title);
                nBody.setText(body);
                nBody.setMovementMethod(new ScrollingMovementMethod());
                nType.setText(type);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void exitNotice(final View view){
        recyclerView.setVisibility(View.VISIBLE);
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
    public void showNotificationOnClick(String title,String subtitle,String body,String type){
        recyclerView.setVisibility(View.GONE);

        growAnim(nCardView);
        nTitle.setText(title);
        String bodyString=subtitle+" | "+body;
        nBody.setText(bodyString);
        nType.setText(type);
    }

}

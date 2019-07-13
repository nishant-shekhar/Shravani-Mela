package com.nsappsstudio.shravanimela;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class NotificationPre extends AppCompatActivity {
    private DatabaseReference mDatabaseReference;
    private List<CardListItem> cardListItems;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_pre);

        mDatabaseReference= FirebaseDatabase.getInstance().getReference();

        progressBar=findViewById(R.id.n_progressBar);
        progressBar.setVisibility(View.VISIBLE);
        showAllNotifications();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
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


                CardListItem cardListItem = new CardListItem(title,date,body,type,2);
                cardListItems.add(cardListItem);
                RecyclerView.Adapter adapter = new CardAdapter(cardListItems, NotificationPre.this);
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
}

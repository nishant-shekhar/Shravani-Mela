package com.nsappsstudio.shravanimela;

import android.os.Bundle;
import android.view.View;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nsappsstudio.shravanimela.Adapter.GalleryAdapter;
import com.nsappsstudio.shravanimela.Model.PoDModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.stfalcon.imageviewer.StfalconImageViewer;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import okhttp3.internal.annotations.EverythingIsNonNull;

public class Gallery extends AppCompatActivity {
    private List<String> images;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        String project="Bhagalpur24";
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(project);
        loadGallery();



    }

    private void loadGallery(){
        final RecyclerView recyclerView=findViewById(R.id.photo_rc);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new GridLayoutManager(this,4));

        GalleryAdapter adapter=new GalleryAdapter(this);
        recyclerView.setAdapter(adapter);
        images=new ArrayList<>();

        mDatabaseReference.child("GlobalParameter").child("Gallery").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@EverythingIsNonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String data=snapshot.getValue(String.class);
                if (data!=null&& data.contains(";")){
                    String[] separator= data.split(";");
                    if (separator.length==5){
                        String url=separator[0];
                        String name=separator[1];
                        String college=separator[2];
                        String mobile=separator[3];
                        String date=separator[4];
                        if (url!=null && url.contains("https:")) {
                            adapter.insertItem(new PoDModel(url, name, college, mobile, date));
                            images.add(url);
                        }
                    }
                }

            }

            @Override
            public void onChildChanged(@EverythingIsNonNull  DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@EverythingIsNonNull  DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@EverythingIsNonNull  DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@EverythingIsNonNull  DatabaseError error) {

            }
        });
    }
    public void back(View view){
        onBackPressed();

    }
    public void loadFullScreenImage(String url){

        int index=images.indexOf(url);
        new StfalconImageViewer.Builder<>(this, images, (imageView, image) -> Picasso.get().load(image).networkPolicy(NetworkPolicy.OFFLINE).into(imageView, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {
                Picasso.get().load(image).into(imageView);

            }
        })).withStartPosition(index).show();
    }
}
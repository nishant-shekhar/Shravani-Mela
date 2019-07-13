package com.nsappsstudio.shravanimela;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.github.chrisbanes.photoview.PhotoView;

public class ImageHolder extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_holder);

        Intent intent=getIntent();
        String typeID = intent.getStringExtra("type");
        if (typeID!=null){
            PhotoView photoView =  findViewById(R.id.photo_view);

            switch (typeID){
                case "secure_ghats":
                    photoView.setImageResource(R.drawable.road_map);
                    break;

            }
        }

    }
}

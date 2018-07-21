package com.nsappsstudio.shravanimela;

import android.content.Intent;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AboutMela extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_mela);

        ConstraintLayout helplineLayout=findViewById(R.id.am_helpline);
        ConstraintLayout aboutLayout=findViewById(R.id.am_about_mela);


        try {
            Intent intent=getIntent();
            String typeID = intent.getStringExtra("type");
            if (typeID!=null){
                switch (typeID){
                    case "helpline":
                        helplineLayout.setVisibility(View.VISIBLE);
                        aboutLayout.setVisibility(View.GONE);
                        break;
                    case "about":
                        helplineLayout.setVisibility(View.GONE);
                        aboutLayout.setVisibility(View.VISIBLE);
                        break;
                }

            }
        }catch (IllegalArgumentException error){
            //do nothing
        }

    }
    public void call(View view){
        String mobileNum="7070747474";
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + mobileNum));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }


    }
}

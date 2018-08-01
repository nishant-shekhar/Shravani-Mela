package com.nsappsstudio.shravanimela;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DutyCard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duty_card);
        String filename="DoctorChart.json";
        try {
            Intent intent=getIntent();
            String typeID = intent.getStringExtra("type");
            if (typeID!=null){
                filename=typeID;
            }
        }catch (IllegalArgumentException error){
            //do nothing
            filename="DoctorChart.json";
        }

        loadTable(filename);
    }
    public String loadJSONFromAsset(String filename) {
        String json ;
        try {
            Context c=this;
            InputStream is = c.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void loadTable(String filename){
        RecyclerView recyclerView=findViewById(R.id.recylerview_duty);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);

        List<DetailCardItem> detailCardItems = new ArrayList<>();

        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset(filename));
            JSONArray m_jArray = obj.getJSONArray("table");

            for (int i = 0; i < m_jArray.length(); i++) {

                JSONObject jo_inside = m_jArray.getJSONObject(i);
                String s_num = jo_inside.getString("s_num");
                String mainTitle = jo_inside.getString("title");
                String name = jo_inside.getString("name");
                String designation = jo_inside.getString("designation");
                String shift = jo_inside.getString("shift");
                String mobile = jo_inside.getString("mobile");
                String description = jo_inside.getString("description");
                String type = jo_inside.getString("type");
                boolean isTitle;


                if (type.equals("1")){
                    isTitle=true;
                }else {
                    isTitle=false;
                }

                switch (filename) {
                    case "DoctorChart.json":
                    case "DoctorChart_eng.json": {
                        DetailCardItem detailCardItem = new DetailCardItem(s_num, description, name, designation,
                                null, shift, mobile, isTitle, mainTitle, 0);
                        detailCardItems.add(detailCardItem);
                        RecyclerView.Adapter adapter = new DetailCardAdaptor(this, detailCardItems);
                        recyclerView.setAdapter(adapter);
                        break;
                    }
                    case "Panda_eng.json": {
                        DetailCardItem detailCardItem = new DetailCardItem(s_num,  description, name,"Father's Name: " +designation,
                                shift, null, mobile, isTitle, mainTitle, 2);
                        detailCardItems.add(detailCardItem);
                        RecyclerView.Adapter adapter = new DetailCardAdaptor(this, detailCardItems);
                        recyclerView.setAdapter(adapter);
                        break;
                    }
                    default: {
                        DetailCardItem detailCardItem = new DetailCardItem(s_num, null, name, designation,
                                description, shift, mobile, isTitle, mainTitle, 0);
                        detailCardItems.add(detailCardItem);
                        RecyclerView.Adapter adapter = new DetailCardAdaptor(this, detailCardItems);
                        recyclerView.setAdapter(adapter);

                        break;
                    }
                }


            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void dialPhoneNumber(String mobileNum) {
        if(mobileNum!=null){
            if (mobileNum.length()>4){
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + mobileNum));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }else {
                Toast.makeText(this, "Proper Number isn't Available", Toast.LENGTH_LONG).show();

            }
        }else {
            Toast.makeText(this, "Number isn't Available", Toast.LENGTH_LONG).show();

        }
    }
}

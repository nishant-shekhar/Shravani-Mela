package com.nsappsstudio.shravanimela;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Table extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        int type=5;
        String filename="Puja Samagari.json";
        try {
            Intent intent=getIntent();
            String typeID = intent.getStringExtra("type");
            if (typeID!=null){
                filename=typeID;
            }
        }catch (IllegalArgumentException error){
            //do nothing
            filename="Puja Samagari.json";
        }

        String title = null;
        switch (filename){
            case "Puja Samagari.json":
                title="सुलतानगंज श्रावणी मेला- 2019\n" +
                        "प्रस्तावित मूल्य तालिका\n" +
                        "पूजा एवं अन्य सामग्री";
                type=5;
                break;
            case "Puja Samagari_eng.json":
                title="Sultangunj Shravani Mela- 2019\n" +
                        "Rate Chart\n" +
                        "Puja Samagari";
                type=5;
                break;
            case "Food Rate.json":
                title="सुलतानगंज श्रावणी मेला- 2019\n" +
                        "प्रस्तावित मूल्य  तालिका\n" +
                        "खाद्य सामग्री";
                type=4;
                break;
            case "Food Rate_eng.json":
                title="Sultangunj Shravani Mela- 2019\n" +
                        "Rate Chart\n" +
                        "Food Items";
                type=4;
                break;

            case "Disaster.json":
                title=getResources().getString(R.string.disaster);
                type=5;
                break;
            case "Disaster_eng.json":
                title=getResources().getString(R.string.disaster);
                type=5;
                break;

        }
        TextView titleView=findViewById(R.id.table_title);
        titleView.setText(title);
        loadTable(filename,type);

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

    private void loadTable(String filename,int type){
        RecyclerView recyclerView=findViewById(R.id.table_recyleview);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<TableList> tableLists = new ArrayList<>();

        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset(filename));
            JSONArray m_jArray = obj.getJSONArray("table");

            for (int i = 0; i < m_jArray.length(); i++) {

                JSONObject jo_inside = m_jArray.getJSONObject(i);
                String cell1 = jo_inside.getString("cell1");
                String cell2 = jo_inside.getString("cell2");
                String cell3 = jo_inside.getString("cell3");
                String cell4 = jo_inside.getString("cell4");
                String cell5 = jo_inside.getString("cell5");
                boolean title;
                boolean even;

                if (i==0){
                    title=true;
                    even=false;
                }else {
                    title=false;
                    if ((i % 2) == 0) {
                        // number is even
                        even=true;
                    }

                    else {
                        // number is odd
                        even=false;
                    }
                }
                TableList tableList= new TableList(title,cell1,cell2,cell3 ,cell4,cell5,even,type);
                tableLists.add(tableList);

                RecyclerView.Adapter adapter = new TableAdapter(tableLists, this);
                recyclerView.setAdapter(adapter);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

    }

}

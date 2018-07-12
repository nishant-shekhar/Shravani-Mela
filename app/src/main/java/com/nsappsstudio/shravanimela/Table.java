package com.nsappsstudio.shravanimela;

import android.content.Context;
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

public class Table extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        loadTable();

    }
    public String loadJSONFromAsset() {
        String json ;
        try {
            Context c=this;
            InputStream is = c.getAssets().open("Bhagalpur Contact.json");
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

    private void loadTable(){
        RecyclerView recyclerView=findViewById(R.id.table_recyleview);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<TableList> tableLists = new ArrayList<>();

        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray m_jArray = obj.getJSONArray("table");

            for (int i = 0; i < m_jArray.length(); i++) {

                JSONObject jo_inside = m_jArray.getJSONObject(i);
                String cell1 = jo_inside.getString("cell1");
                String cell2 = jo_inside.getString("cell2");
                String cell3 = jo_inside.getString("cell3");
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
                TableList tableList= new TableList(title,null,cell1,cell2,cell3 ,null,even,3);
                tableLists.add(tableList);

                RecyclerView.Adapter adapter = new TableAdapter(tableLists, this);
                recyclerView.setAdapter(adapter);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

    }

}

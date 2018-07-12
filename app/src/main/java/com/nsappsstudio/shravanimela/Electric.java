package com.nsappsstudio.shravanimela;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Electric extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electric);


        for(int i=0;i<5;i++){
            String filename;
            RecyclerView recyclerView;
            int type;
            switch (i){
                case 0:
                    recyclerView=findViewById(R.id.electric_list1);
                    filename="Electric1.json";
                    type=5;
                    loadTable(type,recyclerView,filename);
                    //CardView cardView=findViewById(R.id.electric_card1);
                    //ExpandContractList(cardView);
                    break;
                case 1:
                    recyclerView=findViewById(R.id.electric_list2);
                    filename="Electric2.json";
                    type=5;
                    loadTable(type,recyclerView,filename);
                    break;
                case 2:
                    recyclerView=findViewById(R.id.electric_list3);
                    filename="Electric3.json";
                    type=5;
                    loadTable(type,recyclerView,filename);
                    break;
                case 3:
                    recyclerView=findViewById(R.id.electric_list4);
                    filename="Electric4.json";
                    type=4;
                    loadTable(type,recyclerView,filename);
                    break;
                case 4:
                    recyclerView=findViewById(R.id.electric_list5);
                    filename="Electric5.json";
                    type=4;
                    loadTable(type,recyclerView,filename);
                    break;


            }



        }
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

    private void loadTable(int type, RecyclerView recyclerView, String filename){
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);

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
                TableList tableList= new TableList(title,cell1,cell2,cell3 ,cell4,null,even,type);
                tableLists.add(tableList);

                RecyclerView.Adapter adapter = new TableAdapter(tableLists, this);
                recyclerView.setAdapter(adapter);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void ExpandContractList(View view){
        RecyclerView recycler_View;
        switch (view.getId()){
            case R.id.electric_card1:
                recycler_View=findViewById(R.id.electric_list1);
                if (recycler_View.getVisibility()==View.VISIBLE){
                    shrinkAnim(recycler_View);
                }else {
                    growAnim(recycler_View);
                }
                break;
            case R.id.electric_card2:
                recycler_View=findViewById(R.id.electric_list2);
                if (recycler_View.getVisibility()==View.VISIBLE){
                    shrinkAnim(recycler_View);
                }else {
                    growAnim(recycler_View);
                }
                break;
            case R.id.electric_card3:
                recycler_View=findViewById(R.id.electric_list3);
                if (recycler_View.getVisibility()==View.VISIBLE){
                    shrinkAnim(recycler_View);
                }else {
                    growAnim(recycler_View);
                }
                break;
            case R.id.electric_card4:
                recycler_View=findViewById(R.id.electric_list4);
                if (recycler_View.getVisibility()==View.VISIBLE){
                    shrinkAnim(recycler_View);
                }else {
                    growAnim(recycler_View);
                }
                break;
            case R.id.electric_card5:
                recycler_View=findViewById(R.id.electric_list5);
                if (recycler_View.getVisibility()==View.VISIBLE){
                    shrinkAnim(recycler_View);
                }else {
                    growAnim(recycler_View);
                }
                break;

        }

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
}

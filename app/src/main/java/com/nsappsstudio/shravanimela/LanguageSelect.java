package com.nsappsstudio.shravanimela;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.Locale;

public class LanguageSelect extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_select);
    }

    public void SelectLanguage(View view){

        switch (view.getId()){
            case R.id.english:
                setLanguage("en");
                break;
            case R.id.hindi:
                setLanguage("hi");
                break;

        }

    }
    private void setLanguage(String language){
        Locale locale=new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration= new Configuration();
        configuration.locale=locale;
        getBaseContext().getResources().updateConfiguration(configuration,getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("lang", language);
        editor.apply();
        //toastMessage("lang: "+language+String.valueOf(reStart));

        loadInstruction();

    }
    private void loadInstruction(){
        Intent intent= new Intent(this,InstructionPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}

package com.nsappsstudio.shravanimela;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.nsappsstudio.shravanimela.Utils.Utils;

import java.util.Objects;

public class RegisterYourself extends AppCompatActivity {
    private Context ctx;
    private Dialog dialog;
    private DatabaseReference mDatabaseReference;
    private String mobileNo;
    private String dateOfComing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_yourself);
        ctx=this;
        String project="Bhagalpur24";
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(project);
        FirebaseAuth auth=FirebaseAuth.getInstance();
        FirebaseUser user=auth.getCurrentUser();
        if (user!=null){
            mobileNo=user.getPhoneNumber();
        }else {

        }


    }
    public void DatePickMain(View view){
        DatePickerDialog dialog = new DatePickerDialog(this, datePickerListener,
                Utils.thisYear(), Utils.thisMonth()-1,Utils.todayDate());
        dialog.show();
    }

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            if (!Utils.compareFuture(year, month + 1, dayOfMonth)) {

                String m=String.valueOf(month+1);
                if ((month+1)<10){
                    m="0"+(month+1);
                }
                String d=String.valueOf(dayOfMonth);
                if (dayOfMonth<10){
                    d="0"+dayOfMonth;
                }
                dateOfComing=year+m+d;
                String day= Utils.DayOfWeek3Letter(year,month+1,dayOfMonth);
                String displayDate=dayOfMonth+" "+Utils.getDisplayMonth(month+1)+" "+year;
                TextView dateView=findViewById(R.id.textView30);
                dateView.setText(displayDate);

                //Do whatever you want
            }else {
                Toast.makeText(ctx,"You cannot select Past dates",Toast.LENGTH_LONG).show();

            }
        }
    };
    public void loadDialog(View view){
        Dialog feedbackDialog = new Dialog(this);
        feedbackDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        feedbackDialog.setContentView(R.layout.thank_you_register);
        feedbackDialog.setCancelable(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(feedbackDialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(ctx, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        },1200);



        feedbackDialog.show();
        feedbackDialog.getWindow().setAttributes(lp);
    }
    public void back(View view){
        onBackPressed();

    }
}
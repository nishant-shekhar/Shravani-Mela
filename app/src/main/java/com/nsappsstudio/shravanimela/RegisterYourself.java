package com.nsappsstudio.shravanimela;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.nsappsstudio.shravanimela.Animation.Animations;
import com.nsappsstudio.shravanimela.Model.VisitingModel;
import com.nsappsstudio.shravanimela.Utils.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterYourself extends AppCompatActivity {
    private Context ctx;
    private Dialog dialog;
    private DatabaseReference mDatabaseReference;
    private String mobileNo;
    private String dateOfComing;
    private TextView expectedTextview;

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
            Toast.makeText(ctx,"Register Mobile First",Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, MobileReg.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("from","RegisterYourself");
            startActivity(intent);
        }
        expectedTextview=findViewById(R.id.expected_visitor);

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
                checkNumberExpectedVisitor(dateOfComing);

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
    private void checkNumberExpectedVisitor(String date){
        mDatabaseReference.child("GlobalParameter").child("ExpectedVisitors").child(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String expectedVisitor=snapshot.getValue(String.class);
                    String t="Expected visitor on this day: "+expectedVisitor;
                    expectedTextview.setText(t);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void upload(View view){
        Animations.squeeze(view,ctx);
        view.setEnabled(false);

        EditText nameView=findViewById(R.id.name);
        EditText streetView=findViewById(R.id.street);
        EditText cityView=findViewById(R.id.city);
        EditText stateView=findViewById(R.id.state);
        EditText personCountView=findViewById(R.id.person_count);

        String name=nameView.getText().toString().trim();
        String street=streetView.getText().toString().trim();
        String city=cityView.getText().toString().trim();
        String state=stateView.getText().toString().trim();
        String personCount=personCountView.getText().toString().trim();

        if (name.length()>0 && street.length()>0 && city.length()>0 && state.length()>0 && personCount.length()>0 && dateOfComing!=null){

            try {
                long number = Long.parseLong(personCount);
                if (number>20){
                    Toast.makeText(ctx, "You cannot register more than 20 person from one mobile number for a specific date", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Successfully converted, use the 'number' variable as needed
                VisitingModel visitingModel=new VisitingModel(name,street,city,state ,number);
                mDatabaseReference.child("Registration").child(dateOfComing).child(mobileNo).setValue(visitingModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        mDatabaseReference.child("RegistrationCount").child(mobileNo).child(dateOfComing).setValue(number).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                loadDialog();
                            }
                        });
                        analytics(dateOfComing);
                        analyticsVisitor(dateOfComing,number);
                    }
                });

            } catch (NumberFormatException e) {
                // Handle the error when the string cannot be parsed as a long
            }


        }else {
            Toast.makeText(ctx, "All fields are mandatory", Toast.LENGTH_SHORT).show();
        }

    }
    public void analytics(String analysisOf){
        Map<String, Object> updates = new HashMap<>();
        updates.put(analysisOf, ServerValue.increment(1));

        mDatabaseReference.child("GlobalParameter").child("RegistrationCount").updateChildren(updates);



    }
    public void analyticsVisitor(String analysisOf,long count){
        Map<String, Object> updates = new HashMap<>();
        updates.put(analysisOf, ServerValue.increment(count));

        mDatabaseReference.child("GlobalParameter").child("VisitorCount").updateChildren(updates);



    }
    private void loadDialog(){
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
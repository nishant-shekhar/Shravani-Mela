package com.nsappsstudio.shravanimela;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

public class FeedBack extends AppCompatActivity {

    private TextView nBodyView;
    private DatabaseReference mDatabaseReference;
    private ConstraintLayout box_Layout;
    private TextView messageView;
    private String mobileNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        mDatabaseReference= FirebaseDatabase.getInstance().getReference();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user= mAuth.getCurrentUser();
        nBodyView=findViewById(R.id.noti_body);
        box_Layout=findViewById(R.id.n_message_box);
        messageView=findViewById(R.id.n_message);
        box_Layout.setVisibility(View.GONE);


        if (user==null) {
            GoToMobileReg();
        }else {
            mobileNo=user.getPhoneNumber();
        }

    }
    public void sendFeedback(View view){
        String nBody=nBodyView.getText().toString();

        if (nBody.length()>0 && mobileNo.length()>0) {
            box_Layout.setVisibility(View.VISIBLE);
            String message = "Wait! Uploading Feedback";
            messageView.setText(message);
            view.setEnabled(false);


            final DatabaseReference mFeedBackRef= mDatabaseReference.child("FeedBack").child(mobileNo);
            mFeedBackRef.child("body").setValue(nBody).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    mFeedBackRef.child("ts").setValue(ServerValue.TIMESTAMP).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            String message = "Successful";
                            messageView.setText(message);
                            toastMessage("Successfully Uploaded. Thank you for the feedback");

                            Intent intent = new Intent(FeedBack.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            });



        }
    }
    public void GoToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    private void GoToMobileReg() {
        toastMessage("Please Register Mobile before providing feedback");
        Intent intent = new Intent(this, MobileRegistration.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    private void toastMessage(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();

    }
}

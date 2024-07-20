package com.nsappsstudio.shravanimela;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.Objects;

public class Feedback extends AppCompatActivity {

    private DatabaseReference mDatabaseReference;
    private String mobileNo;
    private Context ctx;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        String project="Bhagalpur24";
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(project);
        ctx=this;
        FirebaseAuth auth=FirebaseAuth.getInstance();
        FirebaseUser user=auth.getCurrentUser();
        if (user!=null){
            mobileNo=user.getPhoneNumber();
        }
    }
    public void uploadFeedBack(View view){
        if (mobileNo==null){
            Toast.makeText(ctx,"Register Mobile First",Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, MobileReg.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("from","Feedback");
            startActivity(intent);
        }else {

            EditText editText=findViewById(R.id.name3);
            String feedBack=editText.getText().toString().trim();
            if (feedBack.length()>0){

                updateDialog("Uploading...");
                String key=mDatabaseReference.child("FeedBack").child(mobileNo).push().getKey();
                if (key!=null){
                    mDatabaseReference.child("FeedBack").child(mobileNo).child(key).child("Feedback").setValue(feedBack).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            mDatabaseReference.child("FeedBack").child(mobileNo).child(key).child("ts").setValue(ServerValue.TIMESTAMP).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    dialog.dismiss();
                                    loadSuccessDialog();
                                }
                            });

                        }
                    });
                }


            }

        }
    }
    private void loadSuccessDialog() {
        Dialog lottieDialog = new Dialog(this);
        lottieDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        lottieDialog.setContentView(R.layout.virtual_pooja);
        lottieDialog.setCancelable(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(lottieDialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        LottieAnimationView lottieAnimationView=lottieDialog.findViewById(R.id.lottieAnim);
        lottieAnimationView.setAnimation(R.raw.successful);
        lottieAnimationView.playAnimation();
        TextView t1=lottieDialog.findViewById(R.id.textView11);
        t1.setVisibility(View.INVISIBLE);
        Toast.makeText(ctx, "Successfully Uploaded", Toast.LENGTH_LONG).show();

        Button button=lottieDialog.findViewById(R.id.button);
        String ok="Done";
        button.setText(ok);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                lottieDialog.dismiss();
                onBackPressed();
            }
        });
        lottieDialog.show();
        lottieDialog.getWindow().setAttributes(lp);
    }
    private void loadDialog(String message){
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_simple);
        dialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        TextView dialogMessage=dialog.findViewById(R.id.dialog_text);

        ProgressBar progressBar=dialog.findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);
        dialogMessage.setText(message);

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
    private void updateDialog(String message){
        if (dialog!=null&& dialog.isShowing()){
            TextView dialogMessage=dialog.findViewById(R.id.dialog_text);
            dialogMessage.setText(message);
        }else {
            loadDialog(message);
        }
    }
    public void back(View view){
        onBackPressed();

    }
}
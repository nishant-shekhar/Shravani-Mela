package com.nsappsstudio.shravanimela;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nsappsstudio.shravanimela.Animation.Animations;
import com.nsappsstudio.shravanimela.Model.PoDModel;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class SubmitPhoto extends AppCompatActivity {

    private Uri fileUri;
    private Context ctx;
    private Dialog dialog;
    private StorageReference storageReference;
    private DatabaseReference mDatabaseReference;
    private String mobileNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_photo);
        ctx=this;
        String fromClass=getIntent().getStringExtra("from");
        String project="Muzaffarpur 2022";
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(project);
        storageReference = FirebaseStorage.getInstance().getReference().child(project);
        FirebaseAuth auth=FirebaseAuth.getInstance();
        FirebaseUser user=auth.getCurrentUser();
        if (user!=null){
            mobileNo=user.getPhoneNumber();
        }
        if (fromClass!=null && fromClass.equals("MobileReg") && mobileNo!=null){
            ImagePicker.Companion.with(this).crop(16, 9)
                    //.compress(512)
                    //.cameraOnly()//Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                    .start();
        }
    }
    public void addImage(View view){

        if (mobileNo==null){
            Toast.makeText(ctx,"Register Mobile First",Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, MobileReg.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("from","SubmitPhoto");
            startActivity(intent);
        }else {

            //updateDialog("Capture your Selfie");
            ImagePicker.Companion.with(this).crop(16, 9)
                    //.compress(512)
                    //.cameraOnly()//Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                    .start();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                fileUri = data.getData();
                if (fileUri != null) {
                    //uploadDP();

                    ImageView pic2=findViewById(R.id.slide_image);
                    pic2.setImageURI(fileUri);



                }

            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(ctx, "Error"+ImagePicker.RESULT_ERROR, Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(ctx, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }

    }
    public void back(View view){
        onBackPressed();

    }
    public void upload(View v){
        EditText nameV=findViewById(R.id.name);
        EditText collegeV=findViewById(R.id.name2);

        if(fileUri!=null) {
            String name=nameV.getText().toString().trim();
            String college=collegeV.getText().toString().trim();
            if (name.length()>1 && college.length()>2) {

                loadDialog("Uploading...");
                uploadPhoto(name,college);
                Animations.collapse(v);
            }else {
                Toast.makeText(ctx, "Enter your Name and School, College or City Name", Toast.LENGTH_SHORT).show();

            }
        }else {
            //Toast.makeText(ctx,"Select Image First",Toast.LENGTH_LONG).show();
            addImage(v);
        }
    }
    private void uploadPhoto(String name,String college) {
        updateDialog("Uploading Photo ");

        String key=mDatabaseReference.child("PhotoEntry").child(mobileNo).push().getKey();
        if (key!=null) {
            StorageReference storageReference1 = storageReference.child("PhotoEntry").child(mobileNo).child(key);
            UploadTask uploadTask = storageReference1.putFile(fileUri);
            uploadTask.addOnFailureListener(exception -> {
                // Handle unsuccessful uploads
                Toast.makeText(ctx, "Failed...", Toast.LENGTH_LONG).show();
                Button uploadBtn=findViewById(R.id.button2);
                Animations.expand(uploadBtn);

            }).addOnSuccessListener(taskSnapshot -> storageReference1.getDownloadUrl().addOnSuccessListener(uri -> {
                if (uri!=null) {
                    PoDModel poDModel = new PoDModel(uri.toString(), name, college, mobileNo, null);

                    mDatabaseReference.child("PhotoEntry").child(mobileNo).child(key).setValue(poDModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            mDatabaseReference.child("PhotoEntry").child(mobileNo).child(key).child("ts").setValue(ServerValue.TIMESTAMP).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    dialog.dismiss();
                                    loadSuccessDialog();
                                }
                            });
                        }
                    });
                }else {
                    Toast.makeText(ctx, "Failed...Try again", Toast.LENGTH_LONG).show();

                }

            }));
        }
    }

    private void loadSuccessDialog() {
        Dialog lottieDialog = new Dialog(this);
        lottieDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        lottieDialog.setContentView(R.layout.virtual_pooja);
        lottieDialog.setCancelable(true);
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
        dialog.setCancelable(false);
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

}
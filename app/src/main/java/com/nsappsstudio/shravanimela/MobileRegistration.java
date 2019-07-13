package com.nsappsstudio.shravanimela;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MobileRegistration extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String mVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    private EditText mobileNumberView;
    private TextView countTimeView;
    private ConstraintLayout signUpForm;
    private ConstraintLayout verificationForm;
    private ConstraintLayout autoVerificationView;
    private String mobileNo;
    private String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_registration);

        try {
            Intent intent=getIntent();
            from = intent.getStringExtra("from");

        }catch (NullPointerException error){
            //do nothing
        }

        mobileNumberView =findViewById(R.id.mr_mobile_no);
        mobileNumberView.setFilters(new InputFilter[] {new InputFilter.LengthFilter(10)});
        signUpForm= findViewById(R.id.mr_form);
        signUpForm.setVisibility(View.VISIBLE);
        verificationForm=findViewById(R.id.mr_verify_layout);
        autoVerificationView=findViewById(R.id.mr_timer_layout);
        verificationForm.setVisibility(View.GONE);
        autoVerificationView.setVisibility(View.GONE);
        countTimeView= findViewById(R.id.mr_timer);
        mAuth = FirebaseAuth.getInstance();
        Button signUpButton=findViewById(R.id.mr_signup);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isNetworkAvailable()){
                    toastMessage("No Network");
                    return;
                }if (mobileNumberView.getText().toString().trim().length()==10){

                    mobileNo= mobileNumberView.getText().toString().trim();
                    startPhoneNumberVerification("+91"+mobileNo);

                    signUpForm.setVisibility(View.GONE);
                    autoVerificationView.setVisibility(View.VISIBLE);
                    //start Countdown 60
                    new CountDownTimer(60000,1000){
                        int countdown=60;

                        @Override
                        public void onTick(long l) {
                            countdown--;
                            countTimeView.setText(String.valueOf(countdown));
                        }

                        @Override
                        public void onFinish() {
                            autoVerificationView.setVisibility(View.GONE);
                            verificationForm.setVisibility(View.VISIBLE);
                        }
                    }.start();

                }else {
                    toastMessage("Enter 10 Digit Mobile No");
                }
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                //  Log.d(TAG, "onVerificationCompleted:" + credential);

                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                //  Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                    toastMessage("Invalid request");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                    toastMessage("Too Many Request Sent");
                }
                autoVerificationView.setVisibility(View.GONE);
                verificationForm.setVisibility(View.VISIBLE);
                toastMessage("Verification Failed");
                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                // Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                toastMessage("Code sent");

                // ...
            }
        };

    }

    public void verifyBtn(View view){
        EditText vCodeView = findViewById(R.id.mr_otp);
        String vCodeString= vCodeView.getText().toString().trim();


        verifyPhoneNumberWithCode(mVerificationId,vCodeString);

    }

    public void resendButton(View v){

        resendVerificationCode(mobileNo,mResendToken);

    }
    private  void startPhoneNumberVerification(String phoneNumber){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            // Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            //onDisplayData();
                            if (user != null) {
                                // String mobiln= user.getPhoneNumber();
                                // Toast.makeText(this, mobiln,Toast.LENGTH_SHORT).show();
                                goToProfile();
                            }

                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            //  Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                toastMessage("Invalid OTP");
                            }
                        }
                    }
                });
    }

    public void backButton(View view){
        goToProfile();
    }

    private void goToProfile(){
        Intent intent;
        if (from!=null){
            switch (from){
                case "sos":
                    intent = new Intent(this, SOS.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    break;
                case "main":
                    intent = new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    break;
                case "feedback":
                    intent = new Intent(this, FeedBack.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    break;
                default:
                    intent = new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    break;
            }
        }else {
            intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

    }
    private void toastMessage(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();

    }
}

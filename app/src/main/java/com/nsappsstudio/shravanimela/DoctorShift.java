package com.nsappsstudio.shravanimela;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nsappsstudio.shravanimela.Adapter.ContactAdapter;
import com.nsappsstudio.shravanimela.Animation.Animations;
import com.nsappsstudio.shravanimela.Model.ContactModel;
import com.nsappsstudio.shravanimela.Model.DocShiftModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import okhttp3.internal.annotations.EverythingIsNonNull;

public class DoctorShift extends AppCompatActivity {

    private DatabaseReference mDatabaseReference;
    private Context ctx;
    private Dialog dialog;
    private String place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_shift);
        ctx=this;
        String project="Bhagalpur24";
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(project);
        loadPlaceSpinner();
    }
    private void loadPlaceSpinner(){
        ArrayList<String> schemesList=new ArrayList<>();
        Spinner spinner = findViewById(R.id.spinner_scheme);
        updateDialog("Loading Doctor Shift...");

        mDatabaseReference.child("GlobalParameter").child("List").child("DocPlace").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @EverythingIsNonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    String value=snapshot1.getValue(String.class);
                    if (value!=null){
                        schemesList.add(value);
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(ctx,
                        R.layout.custom_spinner, schemesList);
                adapter.setDropDownViewResource(R.layout.custom_spinner);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        place =spinner.getSelectedItem().toString();
                        loadShift();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }

            @Override
            public void onCancelled(@EverythingIsNonNull  DatabaseError error) {

            }
        });


    }
    private void loadShift(){
        updateDialog("Loading Shifts..");
        RecyclerView recyclerView1=findViewById(R.id.shift1_rc);
        recyclerView1.hasFixedSize();
        recyclerView1.setLayoutManager(new LinearLayoutManager(ctx));
        ContactAdapter adapter1=new ContactAdapter(ctx,2);
        recyclerView1.setAdapter(adapter1);
        recyclerView1.setNestedScrollingEnabled(false);

        RecyclerView recyclerView2=findViewById(R.id.shift2_rc);
        recyclerView2.hasFixedSize();
        recyclerView2.setLayoutManager(new LinearLayoutManager(ctx));
        ContactAdapter adapter2=new ContactAdapter(ctx,2);
        recyclerView2.setAdapter(adapter2);
        recyclerView2.setNestedScrollingEnabled(false);

        RecyclerView recyclerView3=findViewById(R.id.shift3_rc);
        recyclerView3.hasFixedSize();
        recyclerView3.setLayoutManager(new LinearLayoutManager(ctx));
        ContactAdapter adapter3=new ContactAdapter(ctx,2);
        recyclerView3.setAdapter(adapter3);
        recyclerView3.setNestedScrollingEnabled(false);


        TextView doc1=findViewById(R.id.doc_name1);
        TextView doc2=findViewById(R.id.doc_name2);
        TextView doc3=findViewById(R.id.doc_name3);

        TextView designation1=findViewById(R.id.designation1);
        TextView designation2=findViewById(R.id.designation2);
        TextView designation3=findViewById(R.id.designation3);

        TextView contact1=findViewById(R.id.contact1);
        TextView contact2=findViewById(R.id.contact2);
        TextView contact3=findViewById(R.id.contact3);

        TextView shift1=findViewById(R.id.shift1);
        TextView shift2=findViewById(R.id.shift2);
        TextView shift3=findViewById(R.id.shift3);

        LinearLayout card1=findViewById(R.id.card);
        LinearLayout card2=findViewById(R.id.card2);
        LinearLayout card3=findViewById(R.id.card3);

        mDatabaseReference.child("GlobalParameter").child("DoctorShift").child(place).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @EverythingIsNonNull DataSnapshot snapshot) {
                dialog.dismiss();
                ArrayList<String> shiftList = new ArrayList<>();
                ArrayList<String> docList = new ArrayList<>();
                ArrayList<String> docContactList = new ArrayList<>();
                ArrayList<String> docDesignationList = new ArrayList<>();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                    String shift = snapshot1.getKey();
                    shiftList.add(shift);
                    DocShiftModel docShiftModel = snapshot1.getValue(DocShiftModel.class);

                    if (docShiftModel != null && docShiftModel.getDoc() != null) {
                        String[] mainSeparator = docShiftModel.getDoc().split(",");
                        for (int i = 0; i < mainSeparator.length; i++) {
                            String docName = mainSeparator[0];
                            String docDesignation = mainSeparator[1];
                            docList.add(docName);
                            docDesignationList.add(docDesignation);
                        }
                        docContactList.add(docShiftModel.getDocContact());
                    }
                    if (docShiftModel != null && docShiftModel.getStaff() != null) {
                        String[] mainSeparator = docShiftModel.getStaff().split(":");
                        for (int i = 0; i < mainSeparator.length; i++) {
                            String[] separator = mainSeparator[i].split(";");
                            if (separator.length == 3) {
                                String staffName = separator[0];
                                String designation = separator[1];
                                String contact = separator[2];
                                if (shiftList.size() > 0 && shift != null && shift.equals(shiftList.get(0))) {
                                    adapter1.insertItem(new ContactModel(staffName, staffName, designation, designation, contact));
                                }
                                if (shiftList.size() > 1 && shift != null && shift.equals(shiftList.get(1))) {
                                    adapter2.insertItem(new ContactModel(staffName, staffName, designation, designation, contact));
                                }
                                if (shiftList.size() > 2 && shift != null && shift.equals(shiftList.get(2))) {
                                    adapter3.insertItem(new ContactModel(staffName, staffName, designation, designation, contact));
                                }
                            }
                        }
                    }


                    /*
                    DocShiftModel docShiftModel=snapshot1.getValue(DocShiftModel.class);
                    if (docShiftModel!=null && docShiftModel.getStaff()!=null){
                        String[] mainSeparator= docShiftModel.getStaff().split(":");
                        for (int i=0;i<mainSeparator.length;i++){
                            String[] separator= mainSeparator[i].split(";");
                            if (separator.length==3){
                                String staffName=separator[0];
                                String designation=separator[1];
                                String contact=separator[2];
                                if (Objects.requireNonNull(shift).equals("सुबह 07 बजे से दोपहर 02 बजे")) {
                                    shift1.setText(shift);
                                    adapter1.insertItem(new ContactModel(staffName,staffName, designation,designation ,contact));
                                    doc1.setText(docShiftModel.getDoc());
                                    designation1.setText("चिकित्सा पदाधिकारी");
                                    contact1.setText(docShiftModel.getDocContact());
                                    card1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Animations.squeeze(view,ctx);
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    call(docShiftModel.getDocContact());
                                                    if (ctx instanceof DoctorShift){
                                                        Toast.makeText(ctx,"Calling "+docShiftModel.getDoc(),Toast.LENGTH_LONG).show();
                                                    }

                                                }
                                            },300);
                                        }
                                    });
                                }
                                if (Objects.requireNonNull(shift).equals("दोपहर 02 बजे से संध्या 07 बजे तक")) {
                                    shift2.setText(shift);
                                    adapter2.insertItem(new ContactModel(staffName,staffName, designation,designation ,contact));
                                    doc2.setText(docShiftModel.getDoc());
                                    designation2.setText("चिकित्सा पदाधिकारी");
                                    contact2.setText(docShiftModel.getDocContact());
                                    card2.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Animations.squeeze(view,ctx);

                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    call(docShiftModel.getDocContact());
                                                    if (ctx instanceof DoctorShift){
                                                        Toast.makeText(ctx,"Calling "+docShiftModel.getDoc(),Toast.LENGTH_LONG).show();
                                                    }

                                                }
                                            },300);
                                        }
                                    });
                                }
                                if (Objects.requireNonNull(shift).equals("संध्या 07 बजे से सुबह 07 बजे तक")) {
                                    shift3.setText(shift);
                                    adapter3.insertItem(new ContactModel(staffName,staffName, designation,designation ,contact));
                                    doc3.setText(docShiftModel.getDoc());
                                    designation3.setText("चिकित्सा पदाधिकारी");
                                    contact3.setText(docShiftModel.getDocContact());
                                    card3.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Animations.squeeze(view,ctx);

                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    call(docShiftModel.getDocContact());
                                                    if (ctx instanceof DoctorShift){
                                                        Toast.makeText(ctx,"Calling "+docShiftModel.getDoc(),Toast.LENGTH_LONG).show();
                                                    }

                                                }
                                            },300);
                                        }
                                    });
                                }

                            }


                        }


                    }*/
                }
                if (shiftList.size() > 0) {
                    shift1.setText(shiftList.get(0));
                    doc1.setText(docList.get(0));
                    designation1.setText(docDesignationList.get(0));
                    contact1.setText(docContactList.get(0));

                }
                if (shiftList.size() > 1) {
                    shift2.setText(shiftList.get(1));
                    doc2.setText(docList.get(1));
                    designation2.setText(docDesignationList.get(1));
                    contact2.setText(docContactList.get(1));

                }
                if (shiftList.size() > 2) {
                    shift3.setText(shiftList.get(2));
                    doc3.setText(docList.get(2));
                    designation3.setText(docDesignationList.get(2));
                    contact3.setText(docContactList.get(2));

                }
            }
                //shift2.setText(shiftList.get(1));
                //shift3.setText(shiftList.get(2));

                //doc2.setText(docList.get(1));
                //doc3.setText(docList.get(2));

                //designation2.setText(docDesignationList.get(1));
                //designation3.setText(docDesignationList.get(2));

                //contact2.setText(docContactList.get(1));
                //contact3.setText(docContactList.get(2));



            @Override
            public void onCancelled(@EverythingIsNonNull  DatabaseError error) {

            }
        });

    }
    public void back(View view){
        onBackPressed();

    }
    private void call(String mobileNum) {
        if(mobileNum!=null){
            if (mobileNum.length()>4){
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + mobileNum));
                if (intent.resolveActivity(ctx.getPackageManager()) != null) {
                    startActivity(intent);
                }
            }else {
                Toast.makeText(ctx,"Number isn't Available",Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(ctx,"Number isn't Available",Toast.LENGTH_LONG).show();
        }
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
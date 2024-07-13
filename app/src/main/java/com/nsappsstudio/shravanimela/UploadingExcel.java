package com.nsappsstudio.shravanimela;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class UploadingExcel extends AppCompatActivity {
    private Workbook workbook;
    private DatabaseReference mDatabaseReference;
    private String project;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        String project="Bhagalpur24";
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(project);

        loadExcel();


    }
    private void loadExcel() {
        String targetExcel = "ambulance.xls";
        //File filePath = new File( Environment.getExternalStorageDirectory().getPath(),targetExcel);

        File file = new File(getExternalFilesDir(null), targetExcel);
        FileInputStream fileInputStream = null;

        Toast.makeText(this, "file Exist", Toast.LENGTH_SHORT).show();
        List<String> dates=new ArrayList<>();

        dates.add("20220716");
        dates.add("20220717");
        dates.add("20220718");
        dates.add("20220723");
        dates.add("20220724");
        dates.add("20220725");
        dates.add("20220730");
        dates.add("20220731");
        dates.add("20220801");
        dates.add("20220806");
        dates.add("20220807");
        dates.add("20220808");

        try {
            fileInputStream = new FileInputStream(file);
            workbook = Workbook.getWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheet(0);
            Cell[] titles = sheet.getRow(0);

            for (int i = 0; i < sheet.getRows(); i++) {
                int finalI = i;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Cell[] row = sheet.getRow(finalI);
                        Toast.makeText(UploadingExcel.this, row.length + "row : " + sheet.getRows(), Toast.LENGTH_SHORT).show();

                        //docshift
                        /*if (row.length == 6) {
                            String place = row[0].getContents().trim();
                            String shift = Replace(row[1].getContents().toUpperCase().trim());
                            String doc = Replace(row[2].getContents().trim());
                            String doc_contact = Replace(row[3].getContents().trim());
                            String staff = Replace(row[4].getContents().trim());

                            DocShiftModel docShiftModel = new DocShiftModel(null, doc, doc_contact, staff);
                            for (int i=0;i<dates.size();i++) {
                                String date=dates.get(i);

                                DatabaseReference docShiftRef = mDatabaseReference.child("GlobalParameter").child("DocShift").child(place).child(date).child(shift);

                                docShiftRef.setValue(docShiftModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(UploadingExcel.this, "Added Successfully " + doc, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }*/
                        //centralizeContact
                        /*if (row.length == 4) {
                            String srNo = row[0].getContents().trim();
                            String designationHindi = Replace(row[1].getContents().toUpperCase().trim());
                            String number = Replace(row[2].getContents().trim());
                            String designationEng = Replace(row[3].getContents().trim());


                            String data=designationEng+";"+designationHindi+";"+number;
                            mDatabaseReference.child("GlobalParameter").child("Contacts").child("Centralize Contact").child(srNo).setValue(data);

                        }*/
                        //ambulance
                        if (row.length == 6) {
                            String carNo = row[0].getContents().trim();
                            String address = Replace(row[1].getContents().toUpperCase().trim());
                            String driver1 = row[2].getContents().trim();
                            String driver2 = row[3].getContents().trim();
                            String emt1 = row[4].getContents().trim();
                            String emt2 = row[5].getContents().trim();

                            String data = carNo + ";" + address + ";" + driver1+ ";" +driver2+ ";" +emt1+ ";" +emt2;
                            mDatabaseReference.child("GlobalParameter").child("Ambulance").child(String.valueOf(finalI+1)).setValue(data);


                        }
                    }
                    }, i * 100);
            }


            } catch(IOException | BiffException e){
                e.printStackTrace();
                Toast.makeText(this, "error" + e.toString(), Toast.LENGTH_LONG).show();

            }

    }
    private String Replace(String t1){
        if (t1!=null && t1.contains(".")){
            t1=t1.trim().replaceAll("."," ");
        }
        if (t1!=null && t1.contains("/")){
            t1=t1.trim().replaceAll("/"," ");
        }
        if (t1!=null && t1.contains(".")){
            t1=t1.trim().replaceAll("."," ");
        }
        if (t1!=null && t1.contains("#")){
            t1=t1.trim().replaceAll("#"," ");
        }
        if (t1!=null && t1.contains("[")){
            t1=t1.trim().replaceAll(getString(R.string.spe)," ");
        }
        if (t1!=null && t1.contains("]")){
            t1=t1.trim().replaceAll("]"," ");
        }
        return t1;
    }

}
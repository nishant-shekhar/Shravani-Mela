package com.nsappsstudio.shravanimela;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.common.annotation.NonNullApi;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.nsappsstudio.shravanimela.Adapter.AmbulanceAdapter;
import com.nsappsstudio.shravanimela.Adapter.ContactAdapter;
import com.nsappsstudio.shravanimela.Adapter.CrowdAdapter;
import com.nsappsstudio.shravanimela.Adapter.EventAdapter;
import com.nsappsstudio.shravanimela.Adapter.FacilitiesAdapter;
import com.nsappsstudio.shravanimela.Adapter.PhotoSlideAdapter;
import com.nsappsstudio.shravanimela.Adapter.PotDAdapter;
import com.nsappsstudio.shravanimela.Animation.Animations;
import com.nsappsstudio.shravanimela.Model.AmbulanceModel;
import com.nsappsstudio.shravanimela.Model.ContactModel;
import com.nsappsstudio.shravanimela.Model.CrowdItemList;
import com.nsappsstudio.shravanimela.Model.EventModel;
import com.nsappsstudio.shravanimela.Model.FacilityItem;
import com.nsappsstudio.shravanimela.Model.PoDModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.stfalcon.imageviewer.StfalconImageViewer;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.internal.annotations.EverythingIsNonNull;


public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private DatabaseReference mDatabaseReference;
    private TextView[] dots;
    private LinearLayout dotsLayout;
    private Context ctx;
    private ViewPager2 photoViewPager2;
    private List<String> images;
    private int bannerCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String project="Bhagalpur24";
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(project);
        mDrawerLayout=findViewById(R.id.drawer_layout);
        ctx=this;
        welcomeDialog();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@EverythingIsNonNull MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();
                        Toast.makeText(MainActivity.this,String.valueOf(menuItem),Toast.LENGTH_SHORT).show();
                        int id= menuItem.getItemId();
                        loadFromMenu(id);


                        return true;
                    }
                });


        dotsLayout = findViewById(R.id.dot_container);
        photoViewPager2 = findViewById(R.id.slideshow_pager);
        LoadBanner();


        //welcomeDialog();
        loadPotD();

        //loadCrowdStatus();
        loadFacilities();
        loadEvents();
        /*photoList.add("https://firebasestorage.googleapis.com/v0/b/shravanimela18.appspot.com/o/ShravaniMela22%2FSlidingPhotos%2FPhoto%20Contest%20Poster%202.png?alt=media&token=174cfc09-2436-474a-8f4d-641e3de5b1f3");
        photoList.add("https://firebasestorage.googleapis.com/v0/b/shravanimela18.appspot.com/o/ShravaniMela22%2FSlidingPhotos%2Fbanner2.png?alt=media&token=4b02d381-15f1-4c33-b318-ea23b4e5d8da");
        photoList.add("");
        photoList.add("");
        photoList.add("https://firebasestorage.googleapis.com/v0/b/shravanimela18.appspot.com/o/ShravaniMela22%2FSlidingPhotos%2FPhoto%20Contest%20Poster%202.png?alt=media&token=174cfc09-2436-474a-8f4d-641e3de5b1f3");
        photoList.add("");
        */

    }
    private void loadFromMenu(int id) {
        SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String language = sharedPref.getString("lang", null);
        Intent intent;

        if (id == R.id.nav_ambulance) {
            loadAmbulanceList();
        } if (id == R.id.nav_ambulance) {
            loadAmbulanceList();
        } else if (id == R.id.nav_doctor) {
            goToDocShift();
        } else if (id == R.id.nav_parking) {
            openPlaces("Parking", "Parking");
        } else if (id == R.id.nav_centralize_helpline) {
            loadContactList("Centralize Contact");
        } else if (id == R.id.nav_mela_helpline) {
            loadContactList("Mela HelpLine");
        } else if (id == R.id.nav_disaster_helpline) {
            loadContactList("Emergency");
        } else if (id == R.id.nav_health_centre) {
            openPlaces("Medical Camp", "Health Center");
        } else if (id == R.id.nav_shivir) {
            openPlaces("Shivir", "Shivir");
        } else if (id == R.id.nav_change_lang) {
            intent = new Intent(this, LanguageSelect.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_how_to_reach) {
            intent = new Intent(this, HowToReach.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }else if (id == R.id.nav_register) {
            intent = new Intent(this, RegisterYourself.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }else if (id == R.id.nav_feedback) {
            intent = new Intent(this, Feedback.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_reg_mobile) {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                String mobileNo = user.getPhoneNumber();
                toastMessage("Mobile is registered with " + mobileNo);
            } else {
                intent = new Intent(this, MobileReg.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("from", "Main");
                startActivity(intent);
            }
        } else {
            toastMessage("To be Updated");
        }
    }

    private void toastMessage(String s) {
        Toast.makeText(ctx,s,Toast.LENGTH_SHORT).show();
    }

    private void LoadBanner(){

        mDatabaseReference.child("Banner").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@EverythingIsNonNull  DataSnapshot snapshot) {
                bannerCount=0;
                List<String> photoList=new ArrayList<>();

                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    String picUrl=snapshot1.getValue(String.class);
                    if (picUrl!=null && picUrl.contains("https:")){
                        bannerCount++;
                        photoList.add(picUrl);
                        //Toast.makeText(ctx,picUrl,Toast.LENGTH_SHORT).show();

                    }
                }
                dots = new TextView[photoList.size()];


                PhotoSlideAdapter photoSlideAdapter = new PhotoSlideAdapter(photoList, ctx);
                photoViewPager2.setAdapter(photoSlideAdapter);
                dotsIndicator();
                photoViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        selectIndicator(position);
                        super.onPageSelected(position);
                    }
                });


                new CountDownTimer(5000000, 5000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        if (photoViewPager2.getCurrentItem()<(photoList.size()-1)){
                            photoViewPager2.setCurrentItem(photoViewPager2.getCurrentItem()+1 );
                        } else {
                            photoViewPager2.setCurrentItem(0);
                        }
                    }

                    @Override
                    public void onFinish() {

                    }
                }.start();
            }

            @Override
            public void onCancelled(@EverythingIsNonNull  DatabaseError error) {

            }
        });

    }
    private void openPlaces(String name ,String display){
        Intent intent=new Intent(ctx, FindPlaces.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("type",name);
        intent.putExtra("display",display);
        ctx.startActivity(intent);
    }
    private void loadEvents(){
        final RecyclerView recyclerView=findViewById(R.id.events_rc);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        EventAdapter adapter=new EventAdapter(this);
        recyclerView.setAdapter(adapter);

        images=new ArrayList<>();
        EventModel event1 = new EventModel(
                "Shravani Mela Inauguration",
                "22-07-2024",
                "10:00 AM",
                "Join us for the grand inauguration of Shravani Mela with traditional rituals and performances.",
                "https://example.com/images/shravani_mela_inauguration.jpg",
                4.8f,
                1658448000L
        );

        EventModel event2 = new EventModel(
                "Cultural Dance Performance",
                "29-07-2024",
                "05:00 PM",
                "Experience the rich cultural heritage of Bhagalpur with mesmerizing dance performances by local artists.",
                "https://example.com/images/cultural_dance_performance.jpg",
                4.7f,
                1659052800L
        );

        EventModel event3 = new EventModel(
                "Traditional Music Concert",
                "05-08-2024",
                "07:00 PM",
                "Enjoy an evening of soulful traditional music featuring renowned musicians from Bhagalpur.",
                "https://example.com/images/traditional_music_concert.jpg",
                4.9f,
                1659657600L
        );

        EventModel event4 = new EventModel(
                "Handicraft Exhibition",
                "12-08-2024",
                "11:00 AM",
                "Explore and purchase beautiful handicrafts made by local artisans, showcasing the talent of Bhagalpur.",
                "https://example.com/images/handicraft_exhibition.jpg",
                4.6f,
                1660262400L
        );

        EventModel event5 = new EventModel(
                "Food Festival",
                "19-08-2024",
                "01:00 PM",
                "Savor the flavors of Bhagalpur with a variety of traditional dishes and street food at our food festival.",
                "https://example.com/images/food_festival.jpg",
                4.7f,
                1660867200L
        );


        adapter.insertItem(event1);
        adapter.insertItem(event2);
        adapter.insertItem(event3);
        adapter.insertItem(event4);
        adapter.insertItem(event5);



    }
    private void loadPotD(){
        final RecyclerView recyclerView=findViewById(R.id.potd_rc);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        PotDAdapter adapter=new PotDAdapter(this);
        recyclerView.setAdapter(adapter);

        images=new ArrayList<>();
        adapter.insertItem(new PoDModel("","Submit Your Entry","Daily Winner","","Participate"));
        /*String link1="https://firebasestorage.googleapis.com/v0/b/shravanimela18.appspot.com/o/ShravaniMela22%2Fbaba%2Fbaba1.jpg?alt=media&token=48fc2504-090a-4096-986d-2895c1999655";
        String link2="https://firebasestorage.googleapis.com/v0/b/shravanimela18.appspot.com/o/ShravaniMela22%2Fbaba%2Fbaba2.jpg?alt=media&token=cc319e51-dbda-4e04-be77-fe2e4f3711dd";
        String link3="https://firebasestorage.googleapis.com/v0/b/shravanimela18.appspot.com/o/ShravaniMela22%2Fbaba%2Fbaba3.jpg?alt=media&token=5c48a7a6-aeaa-44b9-8215-e6794e54a754";
        String link4="https://firebasestorage.googleapis.com/v0/b/shravanimela18.appspot.com/o/ShravaniMela22%2Fbaba%2Fbaba4.jpg?alt=media&token=204004e3-62a8-44e0-abf1-b8a5dfc67d90";
        String link5="https://firebasestorage.googleapis.com/v0/b/shravanimela18.appspot.com/o/ShravaniMela22%2Fbaba%2Fbaba5.jpg?alt=media&token=9fa84df4-8278-4118-8511-7ae2c585a455";
        String link6="https://firebasestorage.googleapis.com/v0/b/shravanimela18.appspot.com/o/ShravaniMela22%2Fbaba%2Fbaba6.jpg?alt=media&token=65251413-6724-4df4-bfd4-ca0aeefe8683";
        adapter.insertItem(new PoDModel(link1,"Nishant Shekhar","BIT Mesra","834039178","03 July 2022"));
        images.add(link1);

        adapter.insertItem(new PoDModel(link2,"Ashish Kumar","BIT Mesra","834039178","04 July 2022"));
        images.add(link2);
        adapter.insertItem(new PoDModel(link3,"Vijay Shekhar","BIT Mesra","834039178","05 July 2022"));
        images.add(link3);
        adapter.insertItem(new PoDModel(link4,"Anil Kumar","BIT Mesra","834039178","06 July 2022"));
        images.add(link4);
        adapter.insertItem(new PoDModel(link5,"Sahil Kumar","BIT Mesra","834039178","07 July 2022"));
        images.add(link5);
        adapter.insertItem(new PoDModel(link6,"Vinayak","BIT Mesra","834039178","08 July 2022"));
        images.add(link6);*/

        mDatabaseReference.child("PotD").child("Winner").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@EverythingIsNonNull  DataSnapshot snapshot, @Nullable  String previousChildName) {
                PoDModel poDModel=snapshot.getValue(PoDModel.class);
                if (poDModel!=null && poDModel.getUrl()!=null) {
                    adapter.insertItem(poDModel);
                    images.add(poDModel.getUrl());
                }

            }

            @Override
            public void onChildChanged(@EverythingIsNonNull  DataSnapshot snapshot, @Nullable  String previousChildName) {

            }

            @Override
            public void onChildRemoved(@EverythingIsNonNull  DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@EverythingIsNonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@EverythingIsNonNull  DatabaseError error) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }




    public void GoToCamera(View view){
        Intent intent=new Intent(this,Video2.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        /*
        String urlString = "https://rtsp.me/embed/kzEEkH3A";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.android.chrome");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            // Chrome browser presumably not installed so allow user to choose instead
            intent.setPackage(null);
            startActivity(intent);
        }*/
    }
    public void virtualAarti(View view) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.virtual_pooja);
        dialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        LottieAnimationView lottieAnimationView=dialog.findViewById(R.id.lottieAnim);
        lottieAnimationView.setAnimation(R.raw.shiv_phool);
        lottieAnimationView.setFrame(1);
        MediaPlayer mPlayer2= MediaPlayer.create(ctx, R.raw.om_nanah_shivaye);


        Button button=dialog.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mPlayer2.isPlaying()) {
                    analytics("VirtualAarti");
                    lottieAnimationView.setSpeed(1f);
                    lottieAnimationView.playAnimation();
                    mPlayer2.start();
                    Animations.goneTranslateWithAlpha(button,0,200,2,1f,0f);
                    Animations.goneTranslateWithAlpha(button,0,200,2,1f,0f);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Animations.translateWithAlpha(button,0,200,2,1f,0f);
                        }
                    },mPlayer2.getDuration());
                }
            }
        });
        lottieAnimationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mPlayer2.isPlaying()) {
                    analytics("VirtualAarti");

                    lottieAnimationView.setSpeed(1f);
                    lottieAnimationView.playAnimation();
                    mPlayer2.start();
                    Animations.goneTranslateWithAlpha(button,0,200,2,1f,0f);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Animations.translateWithAlpha(button,0,200,2,1f,0f);
                        }
                    },mPlayer2.getDuration());
                }
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
    public void analytics(String analysisOf){
            Map<String, Object> updates = new HashMap<>();
            updates.put(analysisOf, ServerValue.increment(1));

            mDatabaseReference.child("Analytics").updateChildren(updates);



    }
    private void welcomeDialog() {
        SharedPreferences sharedPreferences = getSharedPreferences("info", Context.MODE_PRIVATE);
        boolean first=sharedPreferences.getBoolean("firstTime",true);
        if (first) {
            Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
            dialog.setContentView(R.layout.photo_dialog);
            dialog.setCancelable(true);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

            dialog.show();
            dialog.getWindow().setAttributes(lp);
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("firstTime", false);
        editor.apply();
    }
    private void selectIndicator(int position) {
        for (int i=0;i<dots.length;i++) {
            if (i==position){
                dots[i].setTextColor(ContextCompat.getColor(this,R.color.salmon));
                dots[i].setTextSize(16);

            }else {
                dots[i].setTextColor(ContextCompat.getColor(this,R.color.white));
                dots[i].setTextSize(14);

            }
        }
    }
    private void dotsIndicator(){
        dotsLayout.removeAllViews();
        for (int i=0;i<dots.length;i++){
            dots[i]=new TextView(this);
            dots[i].setText(Html.fromHtml("&#9679; "));
            dots[i].setTextSize(16);
            dots[i].setTextColor(ContextCompat.getColor(this,R.color.white));
            dotsLayout.addView(dots[i]);
        }

    }
    private void loadFacilities(){
        RecyclerView recyclerView=findViewById(R.id.facilities);
        recyclerView.hasFixedSize();
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,4);
        recyclerView.setLayoutManager(gridLayoutManager);
        FacilitiesAdapter adapter=new FacilitiesAdapter(this);

        recyclerView.setAdapter(adapter);
        adapter.insertItem(new FacilityItem("Mela Route"));
        adapter.insertItem(new FacilityItem("Gallery"));
        adapter.insertItem(new FacilityItem("Centralize Contact"));
        adapter.insertItem(new FacilityItem("Ambulance"));
        adapter.insertItem(new FacilityItem("Police Station"));
        adapter.insertItem(new FacilityItem("Control Room"));

        adapter.insertItem(new FacilityItem("Drinking Water"));

        adapter.insertItem(new FacilityItem("Toilets"));
        adapter.insertItem(new FacilityItem("Bathroom"));
        adapter.insertItem(new FacilityItem("Rest Room"));
        adapter.insertItem(new FacilityItem("Dharamshala"));
        adapter.insertItem(new FacilityItem("Parking"));
        adapter.insertItem(new FacilityItem("Health Centre"));
        adapter.insertItem(new FacilityItem("Shivir"));



    }

    public void loadContactList(String type){
        //Toast.makeText(this,"This feature will be activated by 28th July",Toast.LENGTH_LONG).show();
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.rc_dialog);
        dialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ConstraintLayout layout=dialog.findViewById(R.id.layout);
        RecyclerView recyclerView=dialog.findViewById(R.id.rc);
        TextView title=dialog.findViewById(R.id.textView3);
        ProgressBar pb=dialog.findViewById(R.id.progressBar);
        pb.setVisibility(View.VISIBLE);
        title.setText(type);

        // Gets linearlayout
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        params.height = displayMetrics.heightPixels-400;

        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        layout.setLayoutParams(params);


        dialog.show();
        dialog.getWindow().setAttributes(lp);


        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ContactAdapter adapter=new ContactAdapter(ctx,1);
        recyclerView.setAdapter(adapter);

        DatabaseReference mContactRef= mDatabaseReference.child("GlobalParameter").child("Contacts").child(type);
        mContactRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@EverythingIsNonNull DataSnapshot dataSnapshot, @Nullable String s) {
                pb.setVisibility(View.GONE);

                String data=dataSnapshot.getValue(String.class);
                if (data!=null&& data.contains(";")){
                    String[] separator= data.split(";");
                    if (separator.length==3){
                        String designationEng=separator[0];
                        String designationHindi=separator[1];
                        String contact=separator[2];

                        if ( designationEng!=null && contact!=null) {
                            adapter.insertItem(new ContactModel(null,null, designationEng,designationHindi,contact));
                        }
                    }
                }



            }

            @Override
            public void onChildChanged(@EverythingIsNonNull DataSnapshot dataSnapshot, @Nullable String s) {



            }

            @Override
            public void onChildRemoved(@EverythingIsNonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@EverythingIsNonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@EverythingIsNonNull DatabaseError databaseError) {

            }
        });

    }
    public void loadAmbulanceList(){
        //Toast.makeText(this,"This feature will be activated by 28th July",Toast.LENGTH_LONG).show();
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.rc_dialog);
        dialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ConstraintLayout layout=dialog.findViewById(R.id.layout);
        RecyclerView recyclerView=dialog.findViewById(R.id.rc);
        TextView title=dialog.findViewById(R.id.textView3);
        ProgressBar pb=dialog.findViewById(R.id.progressBar);
        pb.setVisibility(View.VISIBLE);
        String t1="Ambulance List";
        title.setText(t1);

        // Gets linearlayout
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        params.height = displayMetrics.heightPixels-400;

        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        layout.setLayoutParams(params);


        dialog.show();
        dialog.getWindow().setAttributes(lp);


        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        AmbulanceAdapter adapter=new AmbulanceAdapter(ctx);
        recyclerView.setAdapter(adapter);

        DatabaseReference mContactRef= mDatabaseReference.child("GlobalParameter").child("Ambulance");
        mContactRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@EverythingIsNonNull DataSnapshot dataSnapshot, @Nullable String s) {
                pb.setVisibility(View.GONE);

                String data=dataSnapshot.getValue(String.class);
                if (data!=null&& data.contains(";")){
                    String[] mainSeparator= data.split(";");
                    if (mainSeparator.length==6){
                        String carNo=mainSeparator[0];
                        String address=mainSeparator[1];
                        List<ContactModel> contactModels=new ArrayList<>();
                        for (int i=2;i<mainSeparator.length;i++){
                            if (mainSeparator[i]!=null && mainSeparator[i].contains(",") ) {
                                String[] separator = mainSeparator[i].split(",");
                                if (separator.length==3) {
                                    String designation = separator[0];
                                    String name=separator[1];
                                    String contact=separator[2];
                                    ContactModel contactModel=new ContactModel(name,name,designation,designation,contact);
                                    contactModels.add(contactModel);
                                }
                            }
                        }
                        AmbulanceModel ambulanceModel=new AmbulanceModel(carNo,address,contactModels);
                        adapter.insertItem(ambulanceModel);

                    }
                }



            }

            @Override
            public void onChildChanged(@EverythingIsNonNull DataSnapshot dataSnapshot, @Nullable String s) {



            }

            @Override
            public void onChildRemoved(@EverythingIsNonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@EverythingIsNonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@EverythingIsNonNull DatabaseError databaseError) {

            }
        });

    }
    public void loadCrowdStatus(View view){
        //Toast.makeText(this,"This feature will be activated by 28th July",Toast.LENGTH_LONG).show();
        Animations.squeeze(view,ctx);

        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.rc_dialog);
        dialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ConstraintLayout layout=dialog.findViewById(R.id.layout);
        RecyclerView recyclerView=dialog.findViewById(R.id.rc);
        TextView title=dialog.findViewById(R.id.textView3);
        ProgressBar pb=dialog.findViewById(R.id.progressBar);
        pb.setVisibility(View.VISIBLE);
        title.setText("Crowd Status");

        // Gets linearlayout
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        params.height = displayMetrics.heightPixels-400;

        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        layout.setLayoutParams(params);


        dialog.show();
        dialog.getWindow().setAttributes(lp);


        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        List<CrowdItemList> crowdItemLists = new ArrayList<>();
        DatabaseReference mCrowdRef= mDatabaseReference.child("CrowdStatus").child("Live");
        mCrowdRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@EverythingIsNonNull DataSnapshot dataSnapshot, @Nullable String s) {
                pb.setVisibility(View.GONE);

                String place=dataSnapshot.getKey();
                String crowdLevel=dataSnapshot.child("level").getValue(String.class);
                String date = null;

                try {
                    Long timeStamp= dataSnapshot.child("ts").getValue(Long.class);
                    if (timeStamp!=null) {

                        CharSequence ago =
                                DateUtils.getRelativeDateTimeString(MainActivity.this, timeStamp, DateUtils.MINUTE_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, 0);

                        //DateUtils.getRelativeTimeSpanString(timeStamp, now, DateUtils.MINUTE_IN_MILLIS);
                        date = String.valueOf(ago);
                    }

                }catch (NullPointerException e){
                    //do nothing
                }

                //Toast.makeText(MainActivity.this, date, Toast.LENGTH_LONG).show();

                CrowdItemList crowdItemList=new CrowdItemList(date,place,crowdLevel);
                crowdItemLists.add(crowdItemList);
                CrowdAdapter adapter = new CrowdAdapter(crowdItemLists, MainActivity.this);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onChildChanged(@EverythingIsNonNull DataSnapshot dataSnapshot, @Nullable String s) {



            }

            @Override
            public void onChildRemoved(@EverythingIsNonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@EverythingIsNonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@EverythingIsNonNull DatabaseError databaseError) {

            }
        });

    }
    public void loadFullScreenImage(String url){

        int index=images.indexOf(url);
        new StfalconImageViewer.Builder<>(this, images, (imageView, image) -> Picasso.get().load(image).networkPolicy(NetworkPolicy.OFFLINE).into(imageView, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {
                Picasso.get().load(image).into(imageView);

            }
        })).withStartPosition(index).show();
    }
    public void openDrawer(View view){
        mDrawerLayout.openDrawer(Gravity.LEFT);

    }
    public void goToSubmitPhoto() {
    }
    private void goToDocShift() {
        Intent intent=new Intent(ctx, DoctorShift.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    public void routeImage(){
        String image1="https://firebasestorage.googleapis.com/v0/b/shravanimela18.appspot.com/o/ShravaniMela22%2FSlidingPhotos%2Fshrawani%20road%20chart%20anumandal%20copy.png?alt=media&token=9014ee52-dcd0-47d2-a25c-53ecec6965d7";
        String image2="https://firebasestorage.googleapis.com/v0/b/shravanimela18.appspot.com/o/ShravaniMela22%2FSlidingPhotos%2Froute%20dto.png?alt=media&token=247216ab-944b-45b4-9a51-5e197bc85fb2";
        List<String> route=new ArrayList<>();
        route.add(image1);
        route.add(image2);
        new StfalconImageViewer.Builder<>(this, route, (imageView, image) -> Picasso.get().load(image).placeholder(R.drawable.ic_route).networkPolicy(NetworkPolicy.OFFLINE).into(imageView, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {
                Picasso.get().load(image).placeholder(R.drawable.ic_route).into(imageView);

            }
        })).withStartPosition(0).show();
    }

    public void loadEventDialog(EventModel item) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.event_dialog);
        dialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        TextView eventName=dialog.findViewById(R.id.first_line);
        TextView eventDetails=dialog.findViewById(R.id.first_line2);
        TextView eventStar=dialog.findViewById(R.id.first_line3);
        TextView eventDate=dialog.findViewById(R.id.first_line4);
        TextView eventTime=dialog.findViewById(R.id.first_line5);
        TextView feedback=dialog.findViewById(R.id.first_line6);
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animations.squeeze(v,ctx);
                loadEventFeedbackDialog(item);
            }
        });
        SimpleRatingBar simpleRatingBar=dialog.findViewById(R.id.simpleRatingBar);
        eventName.setText(item.getEventName());
        eventDetails.setText(item.getDetails());
        eventDate.setText(item.getEventDate());
        eventTime.setText(item.getEventTime());
        eventStar.setText(String.valueOf(item.getStars()));
        simpleRatingBar.setRating(item.getStars());
        simpleRatingBar.setEnabled(false);


        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
    public void loadEventFeedbackDialog(EventModel item) {
        Dialog feedbackDialog = new Dialog(this);
        feedbackDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        feedbackDialog.setContentView(R.layout.event_feedback_dialog);
        feedbackDialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(feedbackDialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        EditText feedback=feedbackDialog.findViewById(R.id.name4);
        Button button=feedbackDialog.findViewById(R.id.button5);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedbackDialog.dismiss();
            }
        });

        SimpleRatingBar simpleRatingBar=feedbackDialog.findViewById(R.id.simpleRatingBar);



        feedbackDialog.show();
        feedbackDialog.getWindow().setAttributes(lp);
    }
}
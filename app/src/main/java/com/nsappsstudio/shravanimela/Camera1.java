package com.nsappsstudio.shravanimela;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Camera1.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Camera1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Camera1 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private PlayerView playerView;
    private SimpleExoPlayer exoPlayer;
    private View v;
    private Context c;

    private OnFragmentInteractionListener mListener;
    private int cameraId;
    private DatabaseReference mDatabaseReferance;

    public Camera1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Camera1.
     */
    // TODO: Rename and change types and number of parameters
    public static Camera1 newInstance(String param1, String param2) {
        Camera1 fragment = new Camera1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v = inflater.inflate(R.layout.fragment_camera1,container, false);
        cameraId = getArguments().getInt("camera");
        mDatabaseReferance= FirebaseDatabase.getInstance().getReference();

        playerView=v.findViewById(R.id.playerView);
        c = getContext();

        if (exoPlayer==null) {
            exoPlayer = ExoPlayerFactory.newSimpleInstance(c, new DefaultTrackSelector());
            playerView.setPlayer(exoPlayer);
        }
        return v;    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        DatabaseReference mCameraLinkRef=mDatabaseReferance.child("GlobalParameter").child("Camera");
        mCameraLinkRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String link1=dataSnapshot.child("Link1").getValue(String.class);
                String link2=dataSnapshot.child("Link2").getValue(String.class);
                String link3=dataSnapshot.child("Link3").getValue(String.class);
                String link4=dataSnapshot.child("Link4").getValue(String.class);
                String linkPlay="http://techslides.com/demos/sample-videos/small.mp4";
                switch (cameraId){
                    case 1:
                        if (link1!=null){
                            linkPlay=link1;
                        }
                        break;
                    case 2:
                        if (link2!=null){
                            linkPlay=link2;
                        }
                        break;
                    case 3:
                        if (link3!=null){
                            linkPlay=link3;
                        }
                        break;
                    case 4:
                        if (link4!=null){
                            linkPlay=link4;
                        }
                        break;
                }


                try {
                    DefaultDataSourceFactory dataSourceFactory=new DefaultDataSourceFactory(c, Util.getUserAgent(c,"exo-demo"));
                    ExtractorMediaSource mediaSource=new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(linkPlay));
                    exoPlayer.prepare(mediaSource);
                    exoPlayer.setPlayWhenReady(false);
                }catch (NullPointerException error){
                    exoPlayer = ExoPlayerFactory.newSimpleInstance(c, new DefaultTrackSelector());
                    playerView.setPlayer(exoPlayer);
                    DefaultDataSourceFactory dataSourceFactory=new DefaultDataSourceFactory(c, Util.getUserAgent(c,"exo-demo"));
                    ExtractorMediaSource mediaSource=new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(linkPlay));
                    exoPlayer.prepare(mediaSource);
                    exoPlayer.setPlayWhenReady(false);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        playerView.setPlayer(null);
        exoPlayer.release();
        exoPlayer=null;
    }

    @Override
    public void onDetach() {
        super.onDetach();


        mListener = null;

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

package com.nsappsstudio.shravanimela;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.rtsp.RtspMediaSource;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.util.EventLogger;
import com.google.android.exoplayer2.util.Log;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class Camera extends AppCompatActivity {

    private String linkPlay;
    private StyledPlayerView playerView;
    private Context ctx;
    private DatabaseReference mDatabaseReference;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ctx=this;
        String project="Muzaffarpur 2022";
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(project);
        webView=findViewById(R.id.web_view);
        //webView.setWebViewClient(new WebViewClient());
        //webView.loadUrl("https://www.youtube.com/watch?v=WTk6Pc2qbYA");


        playerView=findViewById(R.id.playerView);
        //rtps();
    }


    private void loadLink(){
        mDatabaseReference.child("GlobalParameter").child("LiveCam").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    String camLink=snapshot1.getValue(String.class);
                    if (camLink!=null){
                        linkPlay=camLink;
                        liveS();
                    }
                }

            }

            @Override
            public void onCancelled(@NotNull DatabaseError error) {

            }
        });
    }


    public void createPlayer(View view){
        ExoPlayer player = new ExoPlayer.Builder(this).build();
        // Build the media item.
        MediaItem mediaItem = MediaItem.fromUri(Uri.parse(linkPlay));
        player.setMediaItem(mediaItem);
        playerView.setPlayer(player);

        player.prepare();
        player.play();
        player.addAnalyticsListener(new EventLogger());



    }
    private void liveStream(){
        // Global settings.
        ExoPlayer player =
                new ExoPlayer.Builder(this)
                        .setMediaSourceFactory(
                                new DefaultMediaSourceFactory(this).setLiveTargetOffsetMs(5000))
                        .build();

// Per MediaItem settings.
        MediaItem mediaItem =
                new MediaItem.Builder()
                        .setUri(linkPlay)
                        .setLiveConfiguration(
                                new MediaItem.LiveConfiguration.Builder()
                                        .setMaxPlaybackSpeed(1.02f)
                                        .build())
                        .build();
        player.setMediaItem(mediaItem);
        StyledPlayerView playerView=findViewById(R.id.playerView);
        playerView.setPlayer(player);
        player.prepare();
        player.play();
    }
    private void liveS(){
        // Global settings.
        // Create a data source factory.
        DataSource.Factory dataSourceFactory = new DefaultHttpDataSource.Factory();
// Create a HLS media source pointing to a playlist uri.
        HlsMediaSource hlsMediaSource =
                new HlsMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(MediaItem.fromUri(linkPlay));
// Create a player instance.
        ExoPlayer player = new ExoPlayer.Builder(ctx).build();
// Set the media source to be played.
        player.setMediaSource(hlsMediaSource);
// Prepare the player.
        StyledPlayerView playerView=findViewById(R.id.playerView);
        playerView.setPlayer(player);
        player.prepare();
        player.play();
    }
    private void rtps(){
        linkPlay="rtsp.me/embed/kzEEkH3A";

        // Create an RTSP media source pointing to an RTSP uri.
        MediaSource mediaSource =
                new RtspMediaSource.Factory()
                        .createMediaSource(MediaItem.fromUri(linkPlay));
// Create a player instance.
        ExoPlayer player = new ExoPlayer.Builder(ctx).build();
// Set the media source to be played.
        player.setMediaSource(mediaSource);
// Prepare the player.
        playerView.setPlayer(player);
        player.prepare();
        player.play();
        player.addAnalyticsListener(new EventLogger());

    }
}
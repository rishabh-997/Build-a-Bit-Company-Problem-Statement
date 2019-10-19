package com.example.buildabit.VideoTesting;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.buildabit.R;


public class VideoTestingActivity extends AppCompatActivity
{

    MediaController mediaController;
    Uri uri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        VideoView container=findViewById(R.id.videoview);
        Button play_button=findViewById(R.id.button);

        mediaController = new MediaController(this);
        mediaController.setAnchorView(container);

        uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.sample);

        container.setVideoURI(uri);
        container.start();

    }
}

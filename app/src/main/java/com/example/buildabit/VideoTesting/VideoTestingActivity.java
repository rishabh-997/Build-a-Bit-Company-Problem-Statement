package com.example.buildabit.VideoTesting;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.buildabit.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class VideoTestingActivity extends AppCompatActivity
{
    @BindView(R.id.videoview)
    VideoView container;
    @BindView(R.id.seekto)
    Button seekto;

    MediaController mediaController;
    Uri uri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        ButterKnife.bind(this);

        mediaController = new MediaController(this);
        mediaController.setAnchorView(container);

        uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.sample);

        container.setVideoURI(uri);
        container.start();

        seekto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekToPosition();
            }
        });
    }

    private void seekToPosition()
    {
        container.seekTo(50000);
        container.start();
    }
}

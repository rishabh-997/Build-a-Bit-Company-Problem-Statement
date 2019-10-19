package com.example.buildabit.VideoTesting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.buildabit.DataPojo;
import com.example.buildabit.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import butterknife.BindView;
import butterknife.ButterKnife;


public class VideoTestingActivity extends AppCompatActivity
{
    @BindView(R.id.videoview)
    VideoView container;
    @BindView(R.id.token)
    TextView search_result;
    @BindView(R.id.seek_button)
    Button seek;
    @BindView(R.id.progress)
    ProgressBar progressBar;

    MediaController mediaController;
    Uri uri;

    List<DataPojo> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        ButterKnife.bind(this);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("1");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    String tag = ds.getKey().toLowerCase();
                    String time = ds.getValue().toString();
                    list.add(new DataPojo(tag, time));
                    Log.i("hello world",tag+" "+time);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mediaController = new MediaController(this);
        container.setMediaController(mediaController);
        mediaController.setAnchorView(container);
        uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.sample);
        container.setVideoURI(uri);
        container.start();

        seek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String searched_result=search_result.getText().toString().toLowerCase();
                progressBar.setVisibility(View.GONE);

                for(int i = 0; i < list.size(); i++){
                    if(list.get(i).getTag().trim().equals(searched_result.trim())){
                        Toast.makeText(VideoTestingActivity.this, list.get(i).getTime(), Toast.LENGTH_SHORT).show();
                        container.seekTo(Integer.parseInt(list.get(i).getTime()));
                        break;
                    }
                }
            }
        });
    }
    public void getSpeechInput(View view) {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String search=result.get(0);

                    seek.setVisibility(View.VISIBLE);
                    search_result.setVisibility(View.VISIBLE);
                    search_result.setText(search);
                }
                break;
        }
    }

}
package com.example.buildabit.VideoTesting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.util.Pair;
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

import com.example.buildabit.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
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

    List<Pair<String, String>> list = new ArrayList<>();

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

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("1");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    String tag = ds.getKey().toLowerCase();
                    String time = ds.getValue().toString();

                    list.add(Pair.create(tag, time));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        seek.setOnClickListener(v ->
        progressBar.setVisibility(View.VISIBLE));
        new Handler().postDelayed(() -> {
            String se=search_result.toString().toLowerCase();

            progressBar.setVisibility(View.GONE);
            ListIterator<Pair<String,String>>
                    iterator = list.listIterator(0);
            while (iterator.hasNext()) {
                String x=iterator.next().first;
                String time=iterator.next().second;
                if(x.equals(se)){
                    container.seekTo(Integer.parseInt(time));
                    container.start();
                    break;
                }

            }


        },2000);




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
                    Toast.makeText(this, search, Toast.LENGTH_SHORT).show();

                    seek.setVisibility(View.VISIBLE);
                    search_result.setVisibility(View.VISIBLE);
                    search_result.setText(search);

                }
                break;
        }
    }

}

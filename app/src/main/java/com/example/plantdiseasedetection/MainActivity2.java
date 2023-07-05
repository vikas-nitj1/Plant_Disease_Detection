package com.example.plantdiseasedetection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

public class MainActivity2 extends AppCompatActivity {
private static final String TAG="MainActivity2";

    TextView accuracy , disease_txt ;
    Button checkAgain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        LottieAnimationView background = findViewById(R.id.lottieAnimationView_background);
        LottieAnimationView checkMark = findViewById(R.id.lottieAnimationView_tick);

        accuracy = findViewById(R.id.accuracy_text_view);
        disease_txt = findViewById(R.id.disease_textView);

        checkAgain = findViewById(R.id.checkAgain_btn);

        background.setVisibility(View.VISIBLE);
        background.playAnimation();


        Handler handler = new Handler(Looper.getMainLooper());

        Runnable r = new Runnable() {
            @Override
            public void run() {
                checkMark.setVisibility(View.VISIBLE);
                checkMark.playAnimation();
            }
        };
        handler.postDelayed(r,2000);


        Intent intent = getIntent();
        String disease = intent.getStringExtra("class");
        String acc = intent.getStringExtra("confidence");

        Toast.makeText(this, "onCreate: "+acc + " "+ disease,Toast.LENGTH_SHORT).show();
        accuracy.setText(acc+"%");
        disease_txt.setText(disease);

        Log.d(TAG, "onCreate: "+acc + " "+ disease);
        checkAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity2.this,MainActivity.class);
                startActivity(intent1);
            }
        });
    }
}
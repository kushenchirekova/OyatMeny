package com.example.aluad.p.firstsScreens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import com.example.aluad.p.MathAlarm.SetAlarmFromHistory;
import com.example.aluad.p.R;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton b1,b2,b3,b4;
    public static final String TAG = MainActivity.class.getSimpleName();
    Animation animationShake;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b1=(ImageButton)findViewById(R.id.button1);

        b2=(ImageButton)findViewById(R.id.button2);

        b3=(ImageButton)findViewById(R.id.button3);

        b4=(ImageButton)findViewById(R.id.button4);
        animationShake = AnimationUtils.loadAnimation(this, R.anim.incorrect_button_shake);
        animationShake.setRepeatCount(3);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        startActivity(new Intent(this,SetAlarmFromHistory.class));
    }
}

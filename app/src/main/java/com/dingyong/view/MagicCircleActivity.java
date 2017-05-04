package com.dingyong.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.dingyong.view.custom.MyMagicCircle;

public class MagicCircleActivity extends AppCompatActivity {
    private MyMagicCircle mMagicCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magic_circle);
        mMagicCircle = (MyMagicCircle) findViewById(R.id.circle3);
    }

    public void start(View view) {
        mMagicCircle.startAnimation();
    }
}

package com.dingyong.view;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dingyong.view.custom.ColorTrackView;

public class ColorTrackViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_track_view);
        ColorTrackView mView = (ColorTrackView) findViewById(R.id.id_changeTextColorView);
        ObjectAnimator.ofFloat(mView, "progress", 0, 1).setDuration(2000)
                .start();
    }
}

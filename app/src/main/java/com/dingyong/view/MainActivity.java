package com.dingyong.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void customTitleView(View view) {
        Intent intent = new Intent(this, CustomTitleTextActivity.class);
        startActivity(intent);
    }
    public void customProgressBar(View view){
        Intent intent = new Intent(this, CustomProgressBarActivity.class);
        startActivity(intent);
    }
    public void customVolumeControlBar(View view){
        Intent intent = new Intent(this, CustomVolumeControlBarActivity.class);
        startActivity(intent);
    }
    public void qqListView(View view){
        Intent intent = new Intent(this, QQListViewActivity.class);
        startActivity(intent);
    }
    public void customScrollView(View view){
        Intent intent = new Intent(this, CustomScrollViewActivity.class);
        startActivity(intent);
    }
    public void zoomImageView(View view){
        Intent intent = new Intent(this, ZoomImageViewActivity.class);
        startActivity(intent);
    }
    public void viewPagerIndicator(View view){
        Intent intent = new Intent(this, ViewPagerIndicatorActivity.class);
        startActivity(intent);
    }
    public void colorTrackView(View view){
        Intent intent = new Intent(this, ColorTrackViewActivity.class);
        startActivity(intent);
    }

    public void radarView(View view){
        Intent intent = new Intent(this, RadarViewActivity.class);
        startActivity(intent);
    }
    public void waveView(View view){
        Intent intent = new Intent(this, WaveViewActivity.class);
        startActivity(intent);
    }
    public void magicCircle(View viw){
        Intent intent = new Intent(this, MagicCircleActivity.class);
        startActivity(intent);
    }
    public void creditScoreView(View viw){
        Intent intent = new Intent(this, CreditScoreViewActivity.class);
        startActivity(intent);
    }
    public void tempControlView(View viw){
        Intent intent = new Intent(this, TempControlViewActivity.class);
        startActivity(intent);
    }
    public void roundIndicatorView(View viw){
        Intent intent = new Intent(this, RoundIndicatorViewActivity.class);
        startActivity(intent);
    }
    public void scoreTrendView(View viw){
        Intent intent = new Intent(this, BrokenLineTrendViewActivity.class);
        startActivity(intent);
    }

}

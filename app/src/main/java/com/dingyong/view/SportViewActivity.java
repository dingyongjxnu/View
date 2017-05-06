package com.dingyong.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dingyong.view.custom.SportView;

public class SportViewActivity extends AppCompatActivity {
    private SportView mSportView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_view2);
        mSportView = (SportView) findViewById(R.id.sportView);
    }
}

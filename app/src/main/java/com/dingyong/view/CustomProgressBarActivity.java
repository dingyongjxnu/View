package com.dingyong.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dingyong.view.custom.CustomProgressBar;

public class CustomProgressBarActivity extends AppCompatActivity {
    private CustomProgressBar mCustomProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_progress_bar);
        mCustomProgressBar = (CustomProgressBar) findViewById(R.id.CustomProgressBar);
    }

    public void stop(View view) {
        Button button = (Button) view;
        if (!mCustomProgressBar.isStop()) {
            mCustomProgressBar.setStop(true);
            button.setText("Continue");
        } else {
            mCustomProgressBar.setStop(false);
            mCustomProgressBar.start();
            button.setText("stop");
        }

    }
}

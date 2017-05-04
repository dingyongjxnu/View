package com.dingyong.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.dingyong.view.custom.ScoreView;

public class SportViewActivity extends AppCompatActivity {
    private ScoreView mScoreView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_view);
        mScoreView = (ScoreView) findViewById(R.id.sportView);

    }

    public void onStartNumber(View view) {
        EditText editText = (EditText) findViewById(R.id.edit);
        String text = editText.getText().toString();
        if (!TextUtils.isEmpty(text)) {
            mScoreView.setNumber(Integer.valueOf(text));
        }

    }
}

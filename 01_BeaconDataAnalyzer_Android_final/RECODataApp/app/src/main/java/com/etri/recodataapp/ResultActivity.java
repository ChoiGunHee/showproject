package com.etri.recodataapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

/**
 * Created by user on 2016-01-04.
 */
public class ResultActivity extends Activity {
    ResultListAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();
        String result = intent.getStringExtra("result");

        String[] resultList = result.split("!");

        adapter = new ResultListAdapter(this, R.layout.result, resultList);

        ListView resultListView = (ListView)findViewById(R.id.resultListView);
        resultListView.setAdapter(adapter);
    }

}

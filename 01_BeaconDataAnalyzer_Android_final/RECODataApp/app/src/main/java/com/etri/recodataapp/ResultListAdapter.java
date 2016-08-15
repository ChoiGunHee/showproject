package com.etri.recodataapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by user on 2016-01-04.
 */
public class ResultListAdapter extends BaseAdapter {

    private Context resultCon;
    private String[] dataList;
    private int layout;
    private LayoutInflater inflater;

    public ResultListAdapter(Context context, int layout, String[] dataList) {
        this.resultCon = context;
        this.layout = layout;
        this.dataList = dataList;

        inflater = (LayoutInflater) resultCon.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return dataList.length;
    }

    @Override
    public Object getItem(int i) {
        return dataList[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null) {
            view = inflater.inflate(layout, viewGroup, false);
        }

        String[] splitData = dataList[i].split("\\?");

        TextView resultUUIDTextView = (TextView) view.findViewById(R.id.resultUUIDTextView);
        TextView resultMajorMinorTextView = (TextView) view.findViewById(R.id.resultMajorMinorTextView);
        TextView resultRangeTextView = (TextView) view.findViewById(R.id.resultRangeTextView);
        TextView resultD2TextView = (TextView) view.findViewById(R.id.resultD2TextView);
        TextView resultD3TextView = (TextView) view.findViewById(R.id.resultD3TextView);

        resultUUIDTextView.setText(splitData[0]);
        resultMajorMinorTextView.setText(splitData[1]);
        resultRangeTextView.setText(splitData[2]);
        resultD2TextView.setText(splitData[3]);
        resultD3TextView.setText(splitData[4]);

        return view;
    }
}

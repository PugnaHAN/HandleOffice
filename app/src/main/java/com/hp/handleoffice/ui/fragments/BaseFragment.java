package com.hp.handleoffice.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hp.handleoffice.R;

/**
 * Created by zhangjuh on 2016/1/22.
 */
public class BaseFragment extends Fragment {
    private static final String DATA = "data";

    private String mTitle = "";
    private int indicatorColor = Color.BLUE;
    private int dividerColor = Color.GRAY;


    public String getTitle(){
        return mTitle;
    }

    public void setTitle(String title){
        mTitle = title;
    }

    public int getIndicatorColor(){
        return indicatorColor;
    }

    public void setIndicatorColor(int indicatorColor){
        this.indicatorColor = indicatorColor;
    }

    public int getDividerColor(){
        return dividerColor;
    }

    public void setDividerColor(int dividerColor){
        this.dividerColor = dividerColor;
    }
}

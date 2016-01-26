package com.hp.handleoffice.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.hp.handleoffice.R;

/**
 * Created by zhangjuh on 2016/1/22.
 */
public class WebFragment extends BaseFragment {
    private static final String TAG = WebFragment.class.getSimpleName();

    WebView mWebView;

    public static WebFragment newInstance(String title, int indicatorColor, int dividerColor){
        WebFragment webFragment = new WebFragment();
        webFragment.setTitle(title);
        webFragment.setIndicatorColor(indicatorColor);
        webFragment.setDividerColor(dividerColor);
        return webFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.d(TAG, this.getClass().getSimpleName() + " is created");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_webview, container, false);
        mWebView = (WebView) v.findViewById(R.id.web_view);
        return v;
    }

    public WebView getWebView(){
        return mWebView;
    }
}

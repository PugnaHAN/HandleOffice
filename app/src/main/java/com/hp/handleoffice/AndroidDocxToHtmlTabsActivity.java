package com.hp.handleoffice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hp.handleoffice.threads.HandleOfficeThread;
import com.hp.handleoffice.ui.fragments.BaseFragment;
import com.hp.handleoffice.ui.fragments.TabFragment;

import java.lang.ref.WeakReference;

/**
 * Created by zhangjuh on 2016/1/22.
 */
public class AndroidDocxToHtmlTabsActivity extends FragmentActivity{

    public static final int UPDATE_WEBVIEW = 1000;
    public static final int UPDATE_CODE = 1001;
    public static final int UPDATE_PDF = 1002;
    public static final int UPDATE_UI = 1003;

    private static final String IMAGE_DIR_NAME = "images";

    private ProgressBar mProgressBar;
    private TabFragment mTabFragment;

    private String mBaseURL;

    private MyHandler mHandler = new MyHandler(this);

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mProgressBar = (ProgressBar) findViewById(R.id.waiting_bar);

        try {
            mBaseURL = getDir(IMAGE_DIR_NAME, Context.MODE_PRIVATE)
                    .toURI().toURL().toString();
        } catch (Exception e){
            e.printStackTrace();
        }

        HandleOfficeThread handleOfficeThread = new HandleOfficeThread(this, mHandler);
        handleOfficeThread.start();

        initTabFragment(savedInstanceState);
    }

    private void initTabFragment(Bundle savedInstanceState){
        if(savedInstanceState == null){
            mTabFragment = TabFragment.newInstance();

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.content_fragment, mTabFragment)
                    .commit();
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        mHandler.removeMessages(UPDATE_UI);
    }

    private static class MyHandler extends Handler{
        private WeakReference<AndroidDocxToHtmlTabsActivity> mActivity;

        public MyHandler(AndroidDocxToHtmlTabsActivity activity){
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg){
            AndroidDocxToHtmlTabsActivity activity = mActivity.get();
            switch (msg.what){
                case UPDATE_UI:
                    activity.mProgressBar.setVisibility(View.GONE);
                    activity.mTabFragment.updateUI(activity.mBaseURL,
                            msg.obj.toString());
                    break;
            }
        }
    }
}

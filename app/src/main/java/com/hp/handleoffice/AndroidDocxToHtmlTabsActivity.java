package com.hp.handleoffice;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hp.handleoffice.threads.HandleOfficeThread;
import com.hp.handleoffice.ui.fragments.TabFragment;

import java.lang.ref.WeakReference;

/**
 * Created by zhangjuh on 2016/1/22.
 */
public class AndroidDocxToHtmlTabsActivity extends AppCompatActivity{

    public static final int UPDATE_WEBVIEW = 1000;
    public static final int UPDATE_CODE = 1001;
    public static final int UPDATE_PDF = 1002;
    public static final int UPDATE_UI = 1003;

    private WebView mWebView;
    private TextView mTextView;
    private ProgressBar mProgressBar;

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
                    activity.mWebView.loadUrl(msg.obj.toString());
                    activity.mTextView.setText(msg.obj.toString());
                    break;
            }
        }
    }

    private MyHandler mHandler = new MyHandler(this);

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mWebView = (WebView) findViewById(R.id.web_view);
        mTextView = (TextView) findViewById(R.id.tv_code);
        mProgressBar = (ProgressBar) findViewById(R.id.waiting_bar);

        initTabFragment(savedInstanceState);

        HandleOfficeThread handleOfficeThread = new HandleOfficeThread(this, mHandler);
        handleOfficeThread.start();
    }

    private void initTabFragment(Bundle savedInstanceState){
        if(savedInstanceState == null){
            TabFragment tabFragment = new TabFragment();

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.content_fragment, tabFragment)
                    .commit();
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        mHandler.removeMessages(UPDATE_UI);
    }
}

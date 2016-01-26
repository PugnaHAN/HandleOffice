package com.hp.handleoffice.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hp.handleoffice.R;

/**
 * Created by zhangjuh on 2016/1/22.
 */
public class CodeFragment extends BaseFragment {
    private static final String TAG = CodeFragment.class.getSimpleName();

    TextView mTextView;

    public static CodeFragment newInstance(String title, int indicatorColor, int dividerColor){
        CodeFragment codeFragment = new CodeFragment();
        codeFragment.setTitle(title);
        codeFragment.setIndicatorColor(indicatorColor);
        codeFragment.setDividerColor(dividerColor);
        return codeFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.d(TAG, this.getClass().getSimpleName() + " is created");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_textview, container, false);
        mTextView = (TextView) v.findViewById(R.id.tv_code);
        return v;
    }
}

package com.hp.handleoffice.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hp.handleoffice.R;

/**
 * Created by zhangjuh on 2016/1/22.
 */
public class PdfFragment extends BaseFragment {

    public static CodeFragment newInstance(String title, int indicatorColor, int dividerColor){
        CodeFragment codeFragment = new CodeFragment();
        codeFragment.setTitle(title);
        codeFragment.setIndicatorColor(indicatorColor);
        codeFragment.setDividerColor(dividerColor);
        return codeFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_common, container, false);
        return v;
    }
}

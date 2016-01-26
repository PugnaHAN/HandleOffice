package com.hp.handleoffice.ui.fragments;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hp.handleoffice.R;
import com.hp.handleoffice.ui.view.SlidingTabLayout;

/**
 * Created by zhangjuh on 2016/1/22.
 */
public class PdfFragment extends BaseFragment {
    private static final String TAG = PdfFragment.class.getSimpleName();

    private ImageView mImageView;

    public static PdfFragment newInstance(String title, int indicatorColor, int dividerColor){
        PdfFragment pdfFragment = new PdfFragment();
        pdfFragment.setTitle(title);
        pdfFragment.setIndicatorColor(indicatorColor);
        pdfFragment.setDividerColor(dividerColor);
        return pdfFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.d(TAG, this.getClass().getSimpleName() + " is created");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_imgview, container, false);
        mImageView = (ImageView) v.findViewById(R.id.img_view);
        return v;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, this.getClass().getSimpleName() + " is destroyed!!!");
    }
}

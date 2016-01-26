package com.hp.handleoffice.ui.fragments;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.LinkedList;

/**
 * Created by zhangjuh on 2016/1/22.
 */
public class TabViewPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = TabViewPagerAdapter.class.getSimpleName();
    private LinkedList<BaseFragment> mFragments = null;

    public TabViewPagerAdapter(FragmentManager fm, LinkedList<BaseFragment> fragments){
        super(fm);
        if (fragments == null){
            mFragments = new LinkedList<>();
        } else {
            mFragments = fragments;
        }
    }

    @Override
    public BaseFragment getItem(int pos){
        return mFragments.get(pos);
    }

    @Override
    public int getCount(){
        return mFragments.size();
    }

//    @Override
//    public int getItemPosition(Object object){
//        return POSITION_NONE;
//    }

    @Override
    public CharSequence getPageTitle(int pos){
        return mFragments.get(pos).getTitle();
    }

/*  更新ViewPager的办法
    public void setPagerItems(LinkedList<BaseFragment> fragments){
        mFragments = fragments;
    }

    public interface OnReloadListener{
        void onReload();
    }

    public void setOnReloadListener(OnReloadListener listener){
        this.mListener = listener;
    }

    public void reload(){
        if(mListener != null){
            mListener.onReload();
        }
        this.notifyDataSetChanged();
    }*/

}

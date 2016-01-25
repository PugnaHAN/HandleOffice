package com.hp.handleoffice.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hp.handleoffice.R;
import com.hp.handleoffice.ui.view.SlidingTabLayout;

import java.util.LinkedList;

/**
 * Created by zhangjuh on 2016/1/22.
 */
public class TabFragment extends Fragment {

    private SlidingTabLayout tabLayout;
    private ViewPager pager;
    private FragmentPagerAdapter adapter;

    public static TabFragment newInstance(){
        TabFragment tf = new TabFragment();
        return tf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstance){
        return inflater.inflate(R.layout.fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        final LinkedList<BaseFragment> fragments = getFragments();
        adapter = new TabViewPagerAdapter(getFragmentManager(), fragments);

        pager = (ViewPager) view.findViewById(R.id.content_pager);
        pager.setAdapter(adapter);

        tabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        tabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return fragments.get(position).getIndicatorColor();
            }

            @Override
            public int getDividerColor(int position) {
                return fragments.get(position).getDividerColor();
            }
        });
        tabLayout.setBackgroundResource(R.color.colorPrimary);
        tabLayout.setViewPager(pager);
    }

    private LinkedList<BaseFragment> getFragments(){
        int indicatorColor = Color.parseColor(this.getResources().getString(R.color.colorAccent));
        int dividerColor = Color.TRANSPARENT;

        LinkedList<BaseFragment> fragments = new LinkedList<>();
        fragments.add(WebFragment.newInstance("View Web", indicatorColor, dividerColor));
        fragments.add(CodeFragment.newInstance("View code", indicatorColor, dividerColor));
        fragments.add(PdfFragment.newInstance("View pdf", indicatorColor, dividerColor));

        return fragments;
    }
}

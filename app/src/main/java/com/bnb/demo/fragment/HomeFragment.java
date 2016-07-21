package com.bnb.demo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bnb.demo.R;
import com.bnb.library.BaseMainFragment;

/**
 * Created by yanzm on 2016/7/21.
 */
public class HomeFragment extends BaseMainFragment {

    private static final String TAG = HomeFragment.class.getSimpleName();

    public static HomeFragment newInstance() {
        HomeFragment mFragment = new HomeFragment();
        return mFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_home, container, false);
        return mView;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

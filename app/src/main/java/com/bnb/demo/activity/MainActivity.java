package com.bnb.demo.activity;

import android.graphics.drawable.StateListDrawable;
import android.renderscript.Matrix2f;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bnb.demo.R;
import com.bnb.demo.fragment.DiscoverFragment;
import com.bnb.demo.fragment.HomeFragment;
import com.bnb.demo.fragment.rFragment;
import com.bnb.library.BaseMainActivity;
import com.bnb.library.BaseMainFragment;
import com.bnb.library.NavigationBarManager;
import com.bnb.library.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseMainActivity {

    private NavigationBarView mNavigationBarView;

    private NavigationBarManager mManager;

    private List<BaseMainFragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNavigationBarView = (NavigationBarView) findViewById(R.id.id_nbv);
        mFragments = new ArrayList<BaseMainFragment>();
        mFragments.add(HomeFragment.newInstance());
        mFragments.add(DiscoverFragment.newInstance());
        mFragments.add(rFragment.newInstance());
        mManager = new NavigationBarManager(this, mFragments, mNavigationBarView);
    }

    /**
     * 重写该方法，退出主Activity
     */
    @Override
    public void onBackPressedSupport() {
        finish();
    }
}

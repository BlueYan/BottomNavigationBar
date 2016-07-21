package com.bnb.library;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.LayoutInflaterCompat;
import android.view.View;
import android.widget.RadioButton;

import java.util.List;

import me.yokeyword.fragmentation.Fragmentation;
import me.yokeyword.fragmentation.SupportFragment;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * Created by yanzm on 2016/7/21.
 * 管理器
 * 管理所有的fragment切换和底部RadioButton的对应改变
 */
public class NavigationBarManager {

    private static final String TAG = NavigationBarManager.class.getSimpleName();

    private NavigationBarView mNavigationBarView;

    private List<? extends BaseMainFragment> mFragments; //存储所有的Fragments 最多是五个 Fragments必须继承与BaseMainFragment

    private BaseMainActivity mActivity; //上下文本

    public NavigationBarManager(BaseMainActivity mActivity, List<? extends BaseMainFragment> mFragments, NavigationBarView mView) {
        this.mActivity = mActivity;
        this.mFragments = mFragments;
        this.mNavigationBarView = mView;
        init();
    }

    private void init() {
        if ( mFragments.size() > 5 ) {
            throw new RuntimeException("The fragment can not be more than five");
        }
        if ( mFragments.size() != mNavigationBarView.getmRadioButtons().size() ) {
            throw new RuntimeException("The number of buttons and fragment is not consistent");
        }
        initLoadRootFragment();
        List<RadioButton> mRadioButtons = mNavigationBarView.getmRadioButtons();
        for ( int i = 0; i < mRadioButtons.size(); i++ ) {
            final SupportFragment mFragment = mFragments.get(i);  //向上转型
            RadioButton mRb = mRadioButtons.get(i);
            if ( i == 0 ) { //第一个fragment
                mRb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clearRadioButtonState(v);
                        mActivity.start(mFragment, SupportFragment.SINGLETASK);
                    }
                });

            } else {
                mRb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clearRadioButtonState(v);
                        if ( mFragment == null ) {
                            mActivity.popTo(((SupportFragment) mFragments.get(0)).getClass(), false, new Runnable() {
                                @Override
                                public void run() {
                                    mActivity.start(mFragment);
                                }
                            });
                        } else {
                            mActivity.start(mFragment, SupportFragment.SINGLETASK);
                        }
                    }
                });
            }
        }
    }

    private void initLoadRootFragment() {
        mActivity.loadRootFragment(mNavigationBarView.getmFlContainerId(), mFragments.get(0));
    }

    private void clearRadioButtonState(View v) {
        mNavigationBarView.clearAllClickState(v);
    }

}

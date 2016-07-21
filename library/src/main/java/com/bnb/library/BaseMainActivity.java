package com.bnb.library;

import android.os.Bundle;
import android.support.annotation.Nullable;

import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.SupportFragment;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * Created by yanzm on 2016/7/21.
 * 承载Fragment的基础类
 * 继承框架中的SupportActivity
 */
public class BaseMainActivity extends SupportActivity {

    private static final String TAG = BaseMainActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setLoadRootFragment(int mFlContainerId, SupportFragment mTopFragment) {
        loadRootFragment(mFlContainerId, mTopFragment);
    }

    /**
     * 重写Fragment转场动画
     * @return
     */
    @Override
    protected FragmentAnimator onCreateFragmentAnimator() {
        return super.onCreateFragmentAnimator();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

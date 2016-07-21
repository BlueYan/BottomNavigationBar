package com.bnb.library;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.graphics.ComposePathEffect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.bnb.entity.Menu;
import com.bnb.util.MenuInflater;
import com.bnb.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.XMLFormatter;

public class NavigationBarView extends RelativeLayout {

    private static final String TAG = NavigationBarView.class.getSimpleName();

    private FrameLayout mFlContainer; //放置Fragment

    private RadioGroup mRadioGroup; //底部导航栏

    private int resId;

    private Context mContext;

    private AttributeSet mAttrs;

    private MenuInflater mInflater; //底部菜单menuxml解析实例类

    private List<RadioButton> mRadioButtons;

    private int normalColor; //未点击前的文本颜色

    private int clickColor; //点击后的文本颜色

    private Drawable mBackgroundDrawable;

    public NavigationBarView(Context context) {
        super(context);
    }

    public NavigationBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView(context, attrs);
    }

    public NavigationBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        this.mAttrs = attrs;
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray mArray = context.obtainStyledAttributes(attrs, R.styleable.NavigationBarView);
        resId = mArray.getResourceId(R.styleable.NavigationBarView_nbv_menu, R.menu.menu);
        normalColor = mArray.getColor(R.styleable.NavigationBarView_nbv_text_color, Color.parseColor("#000000"));
        clickColor = mArray.getColor(R.styleable.NavigationBarView_nbv_text_click_color, Color.parseColor("#FF0000"));
        mBackgroundDrawable = mArray.getDrawable(R.styleable.NavigationBarView_nbv_rg_background);
        View mView = LayoutInflater.from(context).inflate(R.layout.view_navigationbar, this);
        mRadioGroup = (RadioGroup) mView.findViewById(R.id.id_rg_bottom_bar);
        mFlContainer = (FrameLayout) mView.findViewById(R.id.id_fl_container);
        if ( isInEditMode() ) {
            return;
        }
        mArray.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if ( isInEditMode() ) {
            return;
        }
        if ( mBackgroundDrawable != null ) {
            mRadioGroup.setBackgroundDrawable(mBackgroundDrawable);
        }
        mInflater = new MenuInflater(mContext);
        mInflater.inflater(resId); //解析xml并且获取数据
        List<Menu> menus = mInflater.getMenus();
        if ( menus.size() > 5 ) {
            throw new RuntimeException("The bottom menu can not be more than five");
        }
        mRadioButtons = new ArrayList<RadioButton>();
        dynamicallyAddRadioButton(menus);
    }

    private void dynamicallyAddRadioButton(List<Menu> menus) {
        for ( int i = 0; i < menus.size(); i+=2 ) {
            final RadioButton mRb = new RadioButton(mContext, mAttrs, R.style.CustomRadioButton);
            mRadioGroup.addView(mRb);
            mRb.setText(menus.get(i).getName()); //设置名字
            addSelectorToRadioButton(mRb, menus.get(i), menus.get(i+1));
            mRb.setLayoutParams(new RadioGroup.LayoutParams(0, RadioGroup.LayoutParams.MATCH_PARENT, 1)); //设置权重
            mRb.setGravity(Gravity.CENTER);
            mRb.setPadding(0, Util.dp2px(mContext, 5), 0, Util.dp2px(mContext, 3)); //设置上下边距
            mRb.setTextSize(10); //设置文本大小
            createColorStateList(mRb, normalColor, clickColor);
            if ( i == 0 ) {
                mRb.setChecked(true);
            }
            mRadioButtons.add(mRb);
        }
    }

    public void clearAllClickState(View v) {
        if ( mRadioGroup != null ) {
            mRadioGroup.clearCheck();
            ((RadioButton)v).setChecked(true);
        }
    }

    private void addSelectorToRadioButton(RadioButton mRb, Menu normalState, Menu selectState) {
        StateListDrawable mSelectorDrawable = new StateListDrawable();
        mSelectorDrawable.addState(new int[]{android.R.attr.state_checked}, getDrawable(selectState.getRedId()));  //添加android:state_checked=true时候的点击图片
        mSelectorDrawable.addState(new int[]{}, getDrawable(normalState.getRedId())); //添加android:state_checked=false时候的点击图片
        //mRb.setBackgroundDrawable(mSelectorDrawable); radiobutton不能这样设置selector资源文件，否则会填充满整个背景
        //mRb.setButtonDrawable(mSelectorDrawable);
        //mRb.setCompoundDrawables(null, mSelectorDrawable, null, null); //设置图片位置 这样设置即使重新设置图片大小也是显示不出来
        mRb.setCompoundDrawablesWithIntrinsicBounds(null, mSelectorDrawable, null, null); //这样设置，图片太小 需要改变图片的大小
    }

    private void createColorStateList(RadioButton mRb, int checkedColorId, int normalColorId) {
        int[] colors = new int[]{checkedColorId, normalColorId};
        int[][] states = new int[2][];
        states[0] = new int[]{android.R.attr.state_checked};
        states[1] = new int[]{};
        ColorStateList mColorStateList = new ColorStateList(states, colors);
        mRb.setTextColor(mColorStateList);
    }

    private ColorDrawable getColorDrawable(String colorCode) {
        ColorDrawable mCheckedColorDrawable = new ColorDrawable(Color.parseColor(colorCode));
        return mCheckedColorDrawable;
    }

    private Drawable getDrawable(int drawableId) {
        Drawable mDrawable = mContext.getResources().getDrawable(drawableId);
        //mDrawable.setBounds(0, 0, Util.dp2px(mContext, 200), Util.dp2px(mContext, 200)); //重新设置图片大小 在这里无效
        return mDrawable;
    }

    public int getmFlContainerId() {
        return R.id.id_fl_container;
    }

    public RadioGroup getmRadioGroup() {
        return mRadioGroup;
    }

    public List<RadioButton> getmRadioButtons() {
        return mRadioButtons;
    }
}

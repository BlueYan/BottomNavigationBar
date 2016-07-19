package com.bnb.util;

import android.content.Context;

/**
 * 创建人：Administrator
 * 创建时间： 2016/7/18 0018 17:45
 * 功能概述:
 * 修改人：
 * 修改时间：
 */
public class Util {

    public static int dp2px(Context context, float value) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (value * scale + 0.5f);
    }


}

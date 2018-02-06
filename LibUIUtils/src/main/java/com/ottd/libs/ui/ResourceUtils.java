package com.ottd.libs.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * Created by hardyshi on 2018/1/24.
 */

public class ResourceUtils {

    public static Drawable getUsefulDrawable(Context context, int id){
        Drawable drawable = context.getResources().getDrawable(id);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());
        return drawable;
    }
}

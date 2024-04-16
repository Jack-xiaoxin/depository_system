package com.example.depository_system.constants;

import android.content.res.Resources;
import android.graphics.Color;

import com.example.depository_system.R;
import com.example.depository_system.app.AccountApplication;

// 颜色资源色值
public class ColorParms {
    private static Resources resource = AccountApplication.getInstance().getResources();
    public static int COLOR_CALENDAR_SELECT = Color.parseColor("#24c789");
    public static int COLOR_CALENDAR_COUNT_MINUS = Color.parseColor("#ff5363");
    public static int COLOR_EDITTEXT_DELETE_NORMAL = Color.parseColor("#837f8c");
    public static int COLOR_EDITTEXT_DELETE_WRONG = Color.parseColor("#fa6446");
    public static int COLOR_EDITTEXT_DELETE_HINT = resource.getColor(R.color.colorTextHint);
}

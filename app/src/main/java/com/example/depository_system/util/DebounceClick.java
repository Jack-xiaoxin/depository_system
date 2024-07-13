package com.example.depository_system.util;

import android.util.Log;

public class DebounceClick {
    private long lastClickTime;
    private static final int CLICK_INTERVAL = 2000; // 毫秒

    public boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        lastClickTime = time;
        Log.d("kevin", "timeD = " + timeD);
        return 0 < timeD && timeD < CLICK_INTERVAL;
    }
}

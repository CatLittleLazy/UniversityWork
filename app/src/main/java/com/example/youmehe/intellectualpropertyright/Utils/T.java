package com.example.youmehe.intellectualpropertyright.Utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast工具
 * Created by Administrator on 2017/3/21.
 */

public class T {
    private static Toast mToast = null;

    /**
     * 长时间Toast
     *
     * @param context
     * @param text
     */
    public static void longToast(Context context, String text) {
        if (mToast == null) {
            mToast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_LONG);
        }

        mToast.show();
    }

    /**
     * 段时间Toast
     *
     * @param context
     * @param text
     */
    public static void shortToast(Context context, String text) {
        if (mToast == null) {
            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }

        mToast.show();
    }
}

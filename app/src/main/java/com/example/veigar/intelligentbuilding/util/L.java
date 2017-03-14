package com.example.veigar.intelligentbuilding.util;

import android.util.Log;

/**
 * Created by veigar on 2017/3/6.
 */

public class L {
    //标签
    private static final String TAG = "main";
    public static void e(String str){
        if(AppConst.IS_DEBUG){
            Log.e(TAG,str);
        }
    }

}

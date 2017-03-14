package com.example.veigar.intelligentbuilding.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import com.example.veigar.intelligentbuilding.ui.LoginActivity;
import com.example.veigar.intelligentbuilding.ui.MainActivity;

/**
 * Created by veigar on 2017/3/6.
 */

public class UiHelper {
    public static void forwardMainActivity(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }
    public static void forwardLoginActivity(Context context){
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public static void forwardSetNet(Activity activity){
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        activity.startActivityForResult(intent, 0);
    }
}

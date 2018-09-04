package com.curonsys.android_java.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;

/**
 * Created by ijin-yeong on 2018. 5. 21..
 */
public class ContentUriProvider {

    private static final String HUAWEI_MANUFACTURER = "Huawei";

    public static Uri getUriForFile(@NonNull Context context, @NonNull String authority, @NonNull File file) {
        if (HUAWEI_MANUFACTURER.equalsIgnoreCase(Build.MANUFACTURER) && Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Log.w(ContentUriProvider.class.getSimpleName(), "Using a Huawei device on pre-N. Increased likelihood of failure...");
            try {
                return FileProvider.getUriForFile(context, authority, file);
            } catch (IllegalArgumentException e) {
                Log.w(ContentUriProvider.class.getSimpleName(), "Returning Uri.fromFile to avoid Huawei 'external-files-path' bug", e);
                return Uri.fromFile(file);
            }
        } else {
            return FileProvider.getUriForFile(context, authority, file);
        }
    }
}
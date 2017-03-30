package com.example.mydi;

import android.content.Context;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Field;

/**
 * Created by kangning on 17/3/30.
 */

public class DIUtil {
    private static String TAG = DIUtil.class.getSimpleName();

    public static <T extends Context> void bind(T context, View view) {
        Class cl = context.getClass();
        for (Field field : cl.getDeclaredFields()) {
            InjectView injectView = field.getAnnotation(InjectView.class);
            if (injectView != null) {
                field.setAccessible(true);
                int viewId = injectView.value();
                String type = field.getGenericType().toString();
                Log.d(TAG, type + " " + field.getName());
                try {
                    field.set(context, view.findViewById(viewId));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}

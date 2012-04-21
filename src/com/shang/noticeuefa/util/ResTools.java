package com.shang.noticeuefa.util;

import android.content.Context;

public class ResTools {
    public static String getString(String name, Context context) {
        return context.getResources().getString(context.getResources().getIdentifier(name, "string", context.getPackageName()));
    }

}

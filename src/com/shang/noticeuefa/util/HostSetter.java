package com.shang.noticeuefa.util;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.mobclick.android.MobclickAgent;
import com.shang.noticeuefa.R;

public class HostSetter {
    private final Activity activity;
    private static String host;

    public HostSetter(Activity activity) {
        this.activity = activity;
    }

    public void setHost() {
        MobclickAgent.updateOnlineConfig(activity);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        String host = MobclickAgent.getConfigParams(activity, activity.getString(R.string.umeng_host_key));
        MobclickAgent.updateOnlineConfig(activity,"host");
        if (host != null && host.length() > 0) {
            preferences.edit().putString(activity.getString(R.string.key_host), host).commit();
            this.host = host;
        }else {
            this.host = preferences.getString(activity.getString(R.string.key_host), "");
        }
    }

    public synchronized String getHost() {
        if (host == null)
            setHost();
        return host;
    }
}
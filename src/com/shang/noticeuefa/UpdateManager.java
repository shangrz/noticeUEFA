package com.shang.noticeuefa;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.mobclick.android.MobclickAgent;

/**
 * Created with IntelliJ IDEA.
 * User: jleo
 * Date: 12-5-5
 * Time: 下午2:54
 * To change this template use File | Settings | File Templates.
 */
public class UpdateManager {
    private Activity activity;

    public UpdateManager(Activity activity) {
        this.activity = activity;
    }

    public void checkUpdate() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MobclickAgent.updateOnlineConfig(activity);
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
                String host = MobclickAgent.getConfigParams(activity, activity.getString(R.string.umeng_host_key));
                if (host != null && host.length() > 0) {
                    preferences.edit().putString(activity.getString(R.string.key_tika_host), host).commit();
                } else {
                    host = preferences.getString(activity.getString(R.string.key_tika_host), Constants.TIKA_HOST);
                }
                Constants.TIKA_HOST = host;//
            }
        }).start();
    }
}

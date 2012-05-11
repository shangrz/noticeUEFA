package com.shang.noticeuefa.weibo;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.weibo.net.AccessToken;
import com.weibo.net.DialogError;
import com.weibo.net.Weibo;
import com.weibo.net.WeiboDialogListener;
import com.weibo.net.WeiboException;

public class AuthDialogListener implements WeiboDialogListener {
    private Handler handler = null;
    private Context context = null;
    private String consumer_secret =null;

    public AuthDialogListener(Handler handler,Context context ,String consumer_secret) {
        this.setHandler(handler);
        this.context = context;
        this.consumer_secret =consumer_secret;
    }

    @Override
    public void onComplete(Bundle values) {
        String token = values.getString("access_token");
        String expires_in = values.getString("expires_in");
     
        AccessToken accessToken = new AccessToken(token, consumer_secret);
        accessToken.setExpiresIn(expires_in);
        Weibo.getInstance().setAccessToken(accessToken);
        if (getHandler() != null)
            getHandler().sendEmptyMessage(1); 
    }

    @Override
    public void onError(DialogError e) {
        Toast.makeText(context,
                "Auth error : " + e.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCancel() {
        Toast.makeText(context, "Auth cancel",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onWeiboException(WeiboException e) {
        Toast.makeText(context,
                "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG)
                .show();
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public Handler getHandler() {
        return handler;
    }

}
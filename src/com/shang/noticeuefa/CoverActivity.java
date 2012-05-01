package com.shang.noticeuefa;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.shang.noticeuefa.view.OptionMenuCreator;
import com.srz.androidtools.util.PreferenceUtil;

/**
 * Created with IntelliJ IDEA.
 * User: jleo
 * Date: 12-4-28
 * Time: 上午1:09
 * To change this template use File | Settings | File Templates.
 */
public class CoverActivity extends SherlockActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return new OptionMenuCreator().onCreateOptionsMenu(menu, new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent();
                startActivity(intent);
                return true;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Sherlock); //Used for theme switching in samples
        super.onCreate(savedInstanceState);
        if (PreferenceUtil.isFirstTimeBoot(this)) {

            setContentView(R.layout.cover);
            PreferenceUtil.setFirstTimeBoot(this, false);
        } else {
            //goto time table
        }
    }

}

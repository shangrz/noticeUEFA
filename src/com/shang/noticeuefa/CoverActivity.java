package com.shang.noticeuefa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.shang.noticeuefa.view.OptionMenuCreator;
import com.srz.androidtools.database.DBInitializedListener;
import com.srz.androidtools.database.DBManager;
import com.srz.androidtools.util.AlarmSender;
import com.srz.androidtools.util.PreferenceUtil;

/**
 * Created with IntelliJ IDEA.
 * User: jleo
 * Date: 12-4-28
 * Time: 上午1:09
 * To change this template use File | Settings | File Templates.
 */
public class CoverActivity extends SherlockActivity {
    private ProgressDialog dialog;
    public static final String DATABASE_NAME = "notice";

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

    private static final int PROMPT_INPROGRESS = 1;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_MyStyle); //Used for theme switching in samples
        super.onCreate(savedInstanceState);

        if (!DBManager.initialized(this, DATABASE_NAME))

        {
            showDialog(PROMPT_INPROGRESS);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    DBManager.initialize(new DBInitializedListener() {

                        public void onInitialized() {
                            dismissDialog();
                        }

                        private void dismissDialog() {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (dialog != null)
                                        dialog.dismiss();
                                }
                            });
                        }

                        public void onFailedToInitialzed() {
                            dismissDialog();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    AlarmSender.sendInstantMessage(R.string.initializefail, CoverActivity.this);
                                    CoverActivity.this.finish();
                                }
                            });

                        }
                    }, CoverActivity.this,R.raw.notice,DATABASE_NAME);
                }
            }).start();
        }

        if (PreferenceUtil.isFirstTimeBoot(this)) {
            setContentView(R.layout.cover);
            PreferenceUtil.setFirstTimeBoot(this, false);
        } else {
            //goto time table
        }
    }

}

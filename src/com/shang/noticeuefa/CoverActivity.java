package com.shang.noticeuefa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.shang.noticeuefa.database.DatabaseHelper;
import com.shang.noticeuefa.model2.Group;
import com.shang.noticeuefa.model2.Team;
import com.shang.noticeuefa.model2.TeamGroup;
import com.shang.noticeuefa.view.OptionMenuCreator;
import com.srz.androidtools.util.PreferenceUtil;

import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: jleo
 * Date: 12-4-28
 * Time: 上午1:09
 * To change this template use File | Settings | File Templates.
 */
public class CoverActivity extends SherlockActivity {
    private ProgressDialog dialog;
    public static final String DATABASE_NAME = "notice.sqlite";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return new OptionMenuCreator().onCreateOptionsMenu(menu, new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(CoverActivity.this, FollowActivity.class);
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

//        if (!DBManager.initialized(this, DATABASE_NAME))
//
//        {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            showDialog(PROMPT_INPROGRESS);
//                        }
//                    });
//                    DBManager.initialize(new DBInitializedListener() {
//
//                        public void onInitialized() {
//                            dismissDialog();
//                        }
//
//                        private void dismissDialog() {
//                            handler.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    if (dialog != null)
//                                        dialog.dismiss();
//                                }
//                            });
//                        }
//
//                        public void onFailedToInitialzed() {
//                            dismissDialog();
//                            handler.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    AlarmSender.sendInstantMessage(R.string.initializefail, CoverActivity.this);
//                                    CoverActivity.this.finish();
//                                }
//                            });
//
//                        }
//                    }, CoverActivity.this, R.raw.notice, DATABASE_NAME);
//                }
//            }).start();
//        }

        if (PreferenceUtil.isFirstTimeBoot(this)) {
            setContentView(R.layout.cover);
            databaseHelper = getHelper();
            Team team = new Team();
            team.setTeamName("英格兰");
            team.setTeamShortName("eng");
            team.setFollowed(false);

            Group group = new Group();
            group.setName("英超");

            TeamGroup tg = new TeamGroup();
            tg.setGroup(group);
            tg.setTeam(team);

            try {
                databaseHelper.getTeamDao().create(team);
                databaseHelper.getDao(Group.class).create(group);
                databaseHelper.getDao(TeamGroup.class).create(tg);

                PreferenceUtil.setFirstTimeBoot(this, false);
            } catch (SQLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        } else {
            //goto time table
        }
    }
    private DatabaseHelper databaseHelper = null;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper =
                    OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }
}

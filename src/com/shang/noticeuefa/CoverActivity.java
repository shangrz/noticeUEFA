package com.shang.noticeuefa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
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
            init();
            setContentView(R.layout.cover);
        } else {
            //goto time table
        }
    }

    private void init() {
        databaseHelper = getHelper();
        try {
            Dao dao = databaseHelper.getDao(TeamGroup.class);
            dao.delete(dao.queryForAll());

            Dao dao1 = databaseHelper.getDao(Team.class);
            dao1.delete(dao1.queryForAll());

            Dao dao2 = databaseHelper.getDao(Group.class);
            dao2.delete(dao2.queryForAll());

            Team team = new Team();
            team.setTeamName("英格兰");
            team.setTeamShortName("eng");
            team.setFollowed(false);

            Team team2 = new Team();
            team2.setTeamName("德国");
            team2.setTeamShortName("ger");
            team2.setFollowed(false);

            Team team3 = new Team();
            team3.setTeamName("法国");
            team3.setTeamShortName("fra");
            team3.setFollowed(false);

            Team team4 = new Team();
            team4.setTeamName("意大利");
            team4.setTeamShortName("ita");
            team4.setFollowed(false);

            Team team5 = new Team();
            team5.setTeamName("意大利");
            team5.setTeamShortName("ita");
            team5.setFollowed(false);

            Team team6 = new Team();
            team6.setTeamName("意大利");
            team6.setTeamShortName("ita");
            team6.setFollowed(false);

            Group group = new Group();
            group.setName("英超");

            Group group2 = new Group();
            group2.setName("西甲");

            TeamGroup tg = new TeamGroup();
            tg.setGroup(group);
            tg.setTeam(team);

            TeamGroup tg2 = new TeamGroup();
            tg2.setGroup(group);
            tg2.setTeam(team2);

            TeamGroup tg3 = new TeamGroup();
            tg3.setGroup(group);
            tg3.setTeam(team3);

            TeamGroup tg4 = new TeamGroup();
            tg4.setGroup(group);
            tg4.setTeam(team4);

            TeamGroup tg5 = new TeamGroup();
            tg5.setGroup(group2);
            tg5.setTeam(team5);

            TeamGroup tg6 = new TeamGroup();
            tg6.setGroup(group2);
            tg6.setTeam(team6);

            databaseHelper.getTeamDao().create(team);
            databaseHelper.getTeamDao().create(team2);
            databaseHelper.getTeamDao().create(team3);
            databaseHelper.getTeamDao().create(team4);
            databaseHelper.getTeamDao().create(team5);
            databaseHelper.getTeamDao().create(team6);

            databaseHelper.getDao(Group.class).create(group);
            databaseHelper.getDao(Group.class).create(group2);
            dao.create(tg);
            dao.create(tg2);
            dao.create(tg3);
            dao.create(tg4);
            dao.create(tg5);
            dao.create(tg6);

            PreferenceUtil.setFirstTimeBoot(this, false);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
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

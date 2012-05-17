package com.shang.noticeuefa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.androidquery.AQuery;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;
import com.mobclick.android.MobclickAgent;
import com.shang.noticeuefa.database.DatabaseHelper;
import com.shang.noticeuefa.model2.Group;
import com.shang.noticeuefa.model2.Team;
import com.shang.noticeuefa.model2.TeamGroup;
import com.shang.noticeuefa.view.OptionMenuCreator;
import com.shang.noticeuefa.view.TeamFollowedListener;
import com.shang.noticeuefa.view.TeamGridAdapter;
import com.shang.noticeuefa.view.TeamPagerViewAdapter;
import com.srz.androidtools.util.AlarmSender;
import com.srz.androidtools.util.PreferenceUtil;
import com.srz.androidtools.viewpagertitle.ViewPagerTabs;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jleo
 * Date: 12-4-28
 * Time: 下午4:36
 * To change this template use File | Settings | File Templates.
 */
public class FollowActivity extends SherlockActivity implements TeamFollowedListener {
    private int numberOfSelected;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return new OptionMenuCreator().onCreateOptionsMenu(menu, new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(numberOfSelected == 0){
                    AlarmSender.sendInstantMessage(R.string.atleast, FollowActivity.this);
                    return true;
                }
                Intent intent = new Intent(FollowActivity.this, MatchActivity.class);
                startActivity(intent);
                PreferenceUtil.setFirstTimeBoot(FollowActivity.this, false);
                return true;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_MyStyle); //Used for theme switching in samples

        super.onCreate(savedInstanceState);

        setContentView(R.layout.follow);


        List<GridView> gridViews = new ArrayList<GridView>();
        List<String> titles = new ArrayList<String>();
        try {
            DatabaseHelper helper = getHelper();
            int followedCount = helper.getTeamDao().queryForEq(Team.FOLLOWED, true).size();
            
            setTitle(numberOfSelected = followedCount);

            Dao<Group, Integer> groupDao = helper.getDao(Group.class);
            List<Group> groups = groupDao.queryForAll();
            for (Group group : groups) {
                String s = group.getName();
                titles.add(s);

                ForeignCollection<TeamGroup> teamGroups = group.getTeamGroups();
                List<Team> teams = new ArrayList<Team>();
                for (TeamGroup tg : teamGroups) {
                    teams.add(tg.getTeam());
                }

                GridView gridView = (GridView) LayoutInflater.from(this).inflate(R.layout.teamgridview, null);
                final TeamGridAdapter tga = new TeamGridAdapter(this, R.layout.team, teams);
                tga.setTeamFollowListener(this);
                new AQuery(gridView).find(R.id.grid).adapter(tga);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        tga.onCheck(i);
                    }
                });
                gridViews.add(gridView);
            }

        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        ViewPager groupPager = (ViewPager) findViewById(R.id.teampager);
        groupPager.setAdapter((new TeamPagerViewAdapter(gridViews, titles)));
        ((ViewPagerTabs) findViewById(R.id.tabs)).setViewPager(groupPager);


    }

    public void setTitle(int numberOfSelected) {
        String prefix = getString(R.string.plsselect);
        String suffix = getString(R.string.nselect);
        suffix = suffix.replace("#", "" + numberOfSelected);
        getSupportActionBar().setTitle(prefix + suffix);
    }

    public void onTeamFollowed(Team team) {
        numberOfSelected++;
        setTitle(numberOfSelected );
        try {
            databaseHelper.getTeamDao().update(team);
        } catch (SQLException e) {

        }

    }

    public void onTeamUnfollowed(Team team) {
        numberOfSelected--;
        setTitle(numberOfSelected );
        try {
            databaseHelper.getTeamDao().update(team);
        } catch (SQLException e) {

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

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}

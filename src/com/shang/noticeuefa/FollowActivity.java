package com.shang.noticeuefa;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.shang.noticeuefa.database.DatabaseHelper;
import com.shang.noticeuefa.model2.Team;
import com.shang.noticeuefa.model2.Tour;
import com.shang.noticeuefa.view.OptionMenuCreator;
import com.shang.noticeuefa.view.TeamFollowedListener;

import java.sql.SQLException;
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
    private List<String> titles;

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
        setTheme(R.style.Theme_MyStyle); //Used for theme switching in samples
        DatabaseHelper helper = getHelper();


        try {
            Dao<Tour, Integer> dao = helper.getTourDao();
            Dao<Team, Integer> dao2 = helper.getTeamDao();
            List<Team> teams = dao2.queryForAll();
            System.out.println(teams.size());
            long millis = System.currentTimeMillis();

            Team a = new Team();
//            a.setId(111);
//            a.setFollowed(true);
//            a.setLastModified(new Date());
            a.setTeamName("申花队");
            a.setTeamShortName("FC-shenhua");

            dao2.create(a);
            // create some entries in the onCreate
//            Tour simple = new Tour();
//            simple.setName("假A");
//            simple.setShortName("A");
//            dao.create(simple);


            Log.i(DatabaseHelper.class.getName(), "created new entries in onCreate: " + millis);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        super.onCreate(savedInstanceState);
        setTitle(numberOfSelected);
        setContentView(R.layout.follow);


//        ViewPager mViewPager = (ViewPager) findViewById(R.id.teampager);
//
//
//        final PagerAdapter mPagerAdapter = new TeamPagerAdapter(titles);
//
//        mViewPager.setAdapter(mPagerAdapter);
//        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int i, float v, int i1) {
//                //To change body of implemented methods use File | Settings | File Templates.
//            }
//
//            @Override
//            public void onPageSelected(int i) {
////                animateTitleTo(mPagerAdapter.getPageTitle(i).toString());
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int i) {
//                //To change body of implemented methods use File | Settings | File Templates.
//            }
//        });

    }

    public void setTitle(int numberOfSelected) {
        String prefix = getString(R.string.plsselect);
        String suffix = getString(R.string.nselect);
        suffix = suffix.replace("#", ""+numberOfSelected);
        getSupportActionBar().setTitle(prefix + suffix);
    }

    public void onTeamFollowed(Team team) {
        numberOfSelected++;
    }

    public void onTeamUnfollowed(Team team) {
        numberOfSelected--;
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

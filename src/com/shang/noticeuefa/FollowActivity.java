package com.shang.noticeuefa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.shang.noticeuefa.model2.Team;
import com.shang.noticeuefa.view.OptionMenuCreator;
import com.shang.noticeuefa.view.TeamFollowedListener;
import com.shang.noticeuefa.view.TeamPagerAdapter;

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
        setTheme(R.style.Theme_Sherlock); //Used for theme switching in samples
        super.onCreate(savedInstanceState);
        setTitle(numberOfSelected);
        setContentView(R.layout.follow);


        ViewPager mViewPager = (ViewPager) findViewById(R.id.teampager);


        final PagerAdapter mPagerAdapter = new TeamPagerAdapter(titles);

        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onPageSelected(int i) {
//                animateTitleTo(mPagerAdapter.getPageTitle(i).toString());
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });

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


}

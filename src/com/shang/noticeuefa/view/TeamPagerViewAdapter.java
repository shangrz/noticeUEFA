package com.shang.noticeuefa.view;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import com.srz.androidtools.viewpagertitle.ViewPagerTabProvider;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jleo
 * Date: 12-3-12
 * Time: 下午9:34
 * To change this template use File | Settings | File Templates.
 */
public class TeamPagerViewAdapter extends PagerAdapter implements ViewPagerTabProvider {
    private List<GridView> gridViews;
    private List<String> titles;

    public TeamPagerViewAdapter(List<GridView> gridViews, List<String> titles) {
        this.gridViews = gridViews;
        this.titles = titles;
    }

    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    public int getCount() {
        return gridViews.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ((ViewPager) container).addView(gridViews.get(position));
        return gridViews.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView(gridViews.get(position));
    }

    @Override
    public String getTitle(int position) {
        return titles.get(position);  //To change body of implemented methods use File | Settings | File Templates.
    }
}

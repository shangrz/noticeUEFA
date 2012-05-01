package com.shang.noticeuefa.view;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jleo
 * Date: 12-3-12
 * Time: 下午9:34
 * To change this template use File | Settings | File Templates.
 */
public class TeamPagerAdapter extends PagerAdapter{
    private List<String> titles;

    public TeamPagerAdapter( List<String> titles) {
        this.titles = titles;
    }

    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    public int getCount() {
        return titles.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
//        ((ViewPager) container).addView(ptrs.get(position));
//        return ptrs.get(position);
        return null;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        ((ViewPager) container).removeView(ptrs.get(position));
    }



    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }



}

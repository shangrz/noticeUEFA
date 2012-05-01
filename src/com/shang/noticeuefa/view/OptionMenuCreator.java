package com.shang.noticeuefa.view;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.shang.noticeuefa.R;

/**
 * Created with IntelliJ IDEA.
 * User: jleo
 * Date: 12-4-28
 * Time: 下午4:38
 * To change this template use File | Settings | File Templates.
 */
public class OptionMenuCreator {

    public boolean onCreateOptionsMenu(Menu menu, MenuItem.OnMenuItemClickListener listener) {

        menu.add(R.string.empty)
                .setEnabled(false).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(R.string.empty)
                .setEnabled(false).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        MenuItem nextItem = menu.add(R.string.next)
                .setIcon(R.drawable.navigation_forward);
        nextItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        nextItem.setOnMenuItemClickListener(listener);


        return true;
    }

}

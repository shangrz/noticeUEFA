package com.shang.noticeuefa.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import com.androidquery.AQuery;
import com.shang.noticeuefa.R;
import com.shang.noticeuefa.model2.Team;
import com.srz.androidtools.util.ResTools;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jleo
 * Date: 12-5-4
 * Time: 上午3:23
 * To change this template use File | Settings | File Templates.
 */
public class TeamGridAdapter extends ArrayAdapter<Team> {

    private int resId;
    private final LayoutInflater inflater;
    private boolean[] checked;
    private boolean[] changed;
    private TeamFollowedListener teamFollowListener;

    public TeamGridAdapter(Context context, int textViewResourceId, List<Team> teams) {
        super(context, textViewResourceId, teams);
        resId = textViewResourceId;
        inflater = LayoutInflater.from(this.getContext());
        checked = new boolean[teams.size()];
        int i = 0;

        for (Team t : teams) {
            checked[i] = t.isFollowed();
            i++;
        }
        changed = new boolean[teams.size()];
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView != null) {
            view = convertView;
        } else {
            view = inflater.inflate(resId, null);
        }
        Team item = getItem(position);
        new AQuery(view).find(R.id.name).text(item.getTeamName());
        int logoDrawable = ResTools.getDrawable(item.getTeamShortName(), getContext());
        Drawable layeredLogo = makeBmp(logoDrawable, checked[position]);
        new AQuery(view).find(R.id.teamlogo).getImageView().setImageDrawable(layeredLogo);
        return view;
    }

    private LayerDrawable makeBmp(int id, boolean isChosen) {
        Context context = this.getContext();
        Bitmap mainBmp = ((BitmapDrawable) context.getResources().getDrawable(id)).getBitmap();

        Bitmap seletedBmp;
        if (isChosen == true)
            seletedBmp = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.selected);
        else
            seletedBmp = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.unselected);

        // 叠加
        Drawable[] array = new Drawable[2];
        array[0] = new BitmapDrawable(mainBmp);
        array[1] = new BitmapDrawable(seletedBmp);
        LayerDrawable la = new LayerDrawable(array);
        la.setLayerInset(0, 0, 0, 0, 0);
        la.setLayerInset(1, 75, 75, 0, 0);

        return la;  //返回叠加后的图
    }

    public void onCheck(int i) {
        changed[i] = !changed[i];
        checked[i] = !checked[i];
        Team item = getItem(i);
        item.setFollowed(checked[i]);
        if(checked[i])
            teamFollowListener.onTeamFollowed(item);
        else
            teamFollowListener.onTeamUnfollowed(item);
        notifyDataSetChanged();
    }

    public void setTeamFollowListener(TeamFollowedListener teamFollowListener) {
        this.teamFollowListener = teamFollowListener;
    }
}

package com.shang.noticeuefa.view;

import android.content.Context;
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

    public TeamGridAdapter(Context context, int textViewResourceId, List<Team> objects) {
        super(context, textViewResourceId, objects);
        resId = textViewResourceId;
        inflater = LayoutInflater.from(this.getContext());
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView != null){
            view = convertView;
        }else{
            view = inflater.inflate(resId,null);
        }
        new AQuery(view).find(R.id.name).text(getItem(position).getTeamName());
        new AQuery(view).find(R.id.teamlogo).getImageView().setImageResource(ResTools.getDrawable(getItem(position).getTeamShortName(),getContext()));
        return view;
    }


}

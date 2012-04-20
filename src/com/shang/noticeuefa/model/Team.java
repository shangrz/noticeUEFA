package com.shang.noticeuefa.model;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;

public class Team {
    protected int teamId;

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    protected String teamName;

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    protected int teamFlagResId;

    public int getTeamFlagResId() {
        return teamFlagResId;
    }

    public void setTeamFlagResId(int teamFlagResId) {
        this.teamFlagResId = teamFlagResId;
    }
    public static Team creatFromTeamTagName(String tagname, Context context) {
        return creatFromResId(context.getResources().getIdentifier(tagname, "array", context.getPackageName()),context);
        
    }

    public static Team creatFromResId(int resId, Context context) {
        TypedArray teamarray = context.getResources().obtainTypedArray(resId);
        int id = teamarray.getInt(0, 0);
        String name = teamarray.getString(1);
        int teamFlagResId =   context.getResources().getIdentifier(teamarray.getString(2), "drawable", context.getPackageName());
        teamarray.recycle();
        return new Team(id, name, teamFlagResId);

    }

    private Team(int teamId, String teamName, int teamFlagResId) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.teamFlagResId = teamFlagResId;

    }

}

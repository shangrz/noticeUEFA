package com.shang.noticeuefa.model;

import com.srz.androidtools.util.ResTools;

import android.content.Context;


public class Team {
    protected String teamId;

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
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
    
    
    public static Team creatFromTeamId(String teamId, Context context) {
       
        String teamName = ResTools.getString(teamId, context);
        int teamFlagResId = context.getResources().getIdentifier(teamId, "drawable", context.getPackageName());
        return new Team(teamId, teamName, teamFlagResId);
        
    }

/*    public static Team creatFromResId(int resId, Context context) {
        TypedArray teamarray = context.getResources().obtainTypedArray(resId);
        String id = teamarray.getString(0);
        String name = teamarray.getString(1);
        int teamFlagResId =   context.getResources().getIdentifier(teamarray.getString(2), "drawable", context.getPackageName());
        teamarray.recycle();
        return new Team(id, name, teamFlagResId);

    }*/

    private Team(String teamId, String teamName, int teamFlagResId) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.teamFlagResId = teamFlagResId;

    }

}

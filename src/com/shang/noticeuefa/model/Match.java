package com.shang.noticeuefa.model;

import com.shang.noticeuefa.util.TimeTools;

import android.content.Context;
import android.content.res.TypedArray;

public class Match {
     
    public Team teamA;
    public Team teamB;
    public int matchId;
    public int matchType;
    public String matchIntro;
    protected String matchDatetime;
    
    public String getMatchDatetimeLocal() {
        return TimeTools.handleTimeWithTimezone(matchDatetime);
    }

    public void setMatchDatetime(String matchDatetime) {
        this.matchDatetime = matchDatetime;
    }
    
    public static Match creatFromMatchTagName(String tagname, Context context) {
        return creatFromResId(context.getResources().getIdentifier(tagname, "array", context.getPackageName()),context);
        
    }

    public static Match creatFromResId(int resId, Context context) {
        TypedArray array = context.getResources().obtainTypedArray(resId);
        int matchId = array.getInt(0, 0);
        String matchIntro = array.getString(1);
        Team teamA = Team.creatFromTeamTagName(array.getString(2), context);
        Team teamB = Team.creatFromTeamTagName(array.getString(3), context);
        int matchType = array.getInt(4, 0);
        String matchDatetime = array.getString(5);
        array.recycle();
        return new Match(  matchId,matchType,   teamA,   teamB,      matchIntro,   matchDatetime);

    }

    private Match(int matchId,int matchType, Team teamA, Team teamB,  String matchIntro, String matchDatetime) {
        this.matchId = matchId;
        this.teamA  = teamA;
        this.teamB = teamB;
        this.matchDatetime =matchDatetime;
        this.matchType = matchType;
        this.matchIntro =matchIntro;
    }
    
}

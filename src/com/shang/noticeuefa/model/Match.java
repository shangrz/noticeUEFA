package com.shang.noticeuefa.model;

import com.shang.noticeuefa.util.TimeTools;

import android.content.Context;
import android.content.res.TypedArray;

public class Match {
     
    public Team teamA;
    public Team teamB;
    public int matchId;
    public String matchType;
    public String matchIntro;
    protected String matchDatetime;
    
    public String getMatchDatetimeLocal() {
        return TimeTools.handleTimeWithTimezone(matchDatetime);
    }

    public void setMatchDatetimeGMT8(String matchDatetime) {
        this.matchDatetime = matchDatetime;
    }
    
    public static Match creatFromMatchTagName(String tagname, Context context) {
        return creatFromResId(context.getResources().getIdentifier(tagname, "array", context.getPackageName()),context);
        
    }

    public static Match creatFromResId(int resId, Context context) {
        TypedArray array = context.getResources().obtainTypedArray(resId);
        int matchId = array.getInt(0, 0);
        String matchIntro = array.getString(1);
        Team teamA = Team.creatFromTeamId(array.getString(2), context);
        Team teamB = Team.creatFromTeamId(array.getString(3), context);
        String matchType = array.getString(4);
        String matchDatetime = array.getString(5);
        array.recycle();
        return new Match(  matchId,matchType,   teamA,   teamB,      matchIntro,   matchDatetime);

    }

    private Match(int matchId,String matchType, Team teamA, Team teamB,  String matchIntro, String matchDatetime) {
        this.matchId = matchId;
        this.teamA  = teamA;
        this.teamB = teamB;
        this.matchDatetime =matchDatetime;
        this.matchType = matchType;
        this.matchIntro =matchIntro;
    }
    
}

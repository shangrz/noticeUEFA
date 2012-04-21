package com.shang.noticeuefa.model;

import com.shang.noticeuefa.util.Constants;
import com.shang.noticeuefa.util.TimeTools;

import android.content.Context;
 
import android.content.res.TypedArray;
import android.graphics.Bitmap.Config;

public class Match {
    
    private static Context context ;
     
    public Team teamA;
    public Team teamB;
    public String matchId;
    public String matchType;
    public String matchIntro;
    protected String matchDatetime;
    protected boolean isNotice;
    
    public boolean isNotice() {
        return isNotice;
    }

    public void setNotice(boolean isNotice) {
        this.isNotice = isNotice;
        context.getSharedPreferences(Constants.SHAREDPREFERENCESNAME, 0).edit().putBoolean(this.matchId, this.isNotice).commit();
    }
    
    public String getMatchDatetimeLocal() {
        return TimeTools.handleTimeWithTimezone(matchDatetime);
    }

    public void setMatchDatetimeGMT8(String matchDatetime) {
        this.matchDatetime = matchDatetime;
    }
    
    public static Match creatFromTagName(String tagname, Context context) {
        Match.context =context;
        return creatFromResId(  tagname,context.getResources().getIdentifier(tagname, "array", context.getPackageName()),context);
        
    }

    public static Match creatFromResId(String tagname,int resId, Context context) {
         
        TypedArray array = context.getResources().obtainTypedArray(resId);
        String  matchId = tagname;
        String matchIntro = array.getString(0);
        Team teamA = Team.creatFromTeamId(array.getString(1), context);
        Team teamB = Team.creatFromTeamId(array.getString(2), context);
        String matchType = array.getString(3);
        String matchDatetime = array.getString(4);
        boolean isNotice = context.getSharedPreferences(Constants.SHAREDPREFERENCESNAME, 0).getBoolean(tagname, false);
        array.recycle();
        return new Match(  matchId,matchType,   teamA,   teamB,      matchIntro,   matchDatetime,isNotice);

    }

    
    
    private Match(String matchId,String matchType, Team teamA, Team teamB,  String matchIntro, String matchDatetime,boolean isNotice) {
        this.matchId = matchId;
        this.teamA  = teamA;
        this.teamB = teamB;
        this.matchDatetime =matchDatetime;
        this.matchType = matchType;
        this.matchIntro =matchIntro;
        this.isNotice =isNotice;
    }
    
}

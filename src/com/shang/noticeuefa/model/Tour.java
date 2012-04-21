package com.shang.noticeuefa.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import android.content.Context;
import android.content.res.TypedArray;

public class Tour extends  ArrayList<Match>{
  
     
    private static final long serialVersionUID = 3962278490220710135L;
    public static Tour creatFromTagName(String tagname, Context context) {
        return creatFromResId(context.getResources().getIdentifier(tagname, "array", context.getPackageName()),context);
        
    }
    public static Tour creatFromResId(int resId, Context context) {
        Tour tour = new Tour();
        TypedArray array = context.getResources().obtainTypedArray(resId);
        for(int i=0; i<array.length(); i++) {
            System.out.println(array.getString(i));
            tour.add(Match.creatFromTagName(array.getString(i), context));
            //tour.matchMap.put(array.getString(i),  Match.creatFromTagName(array.getString(i), context));
        }
        array.recycle();
        return tour;

    }
}

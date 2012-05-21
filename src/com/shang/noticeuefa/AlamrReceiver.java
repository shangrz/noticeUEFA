package com.shang.noticeuefa;

 
import java.sql.SQLException;

import com.androidquery.AQuery;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.shang.noticeuefa.database.DatabaseHelper;
import com.shang.noticeuefa.model2.Match;
import com.shang.noticeuefa.test.testNoticeActivity;
import com.shang.noticeuefa.util.Constants;
import com.shang.noticeuefa.weibo.SinaTrendActivity;
import com.srz.androidtools.util.TimeTools;

import android.R.integer;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;  
import android.content.Context;  
import android.content.Intent;  
import android.net.Uri;
import android.os.Bundle;
import android.util.Printer;
import android.view.View;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.Toast;  
  
 
public class AlamrReceiver extends BroadcastReceiver {  
    NotificationManager nm;
    
    @Override  
    public void onReceive(Context context, Intent intent) {  
        if(intent.getStringExtra("delnotice")!=null) {
            System.out.println(intent.getStringExtra("delnotice"));
            return;    
        }
        aQuery = new AQuery(context);
        nm=(NotificationManager)context.getSystemService("notification");
        System.out.println("闹钟时间到");
        Toast.makeText(context, "闹钟时间到", Toast.LENGTH_LONG).show();  
        
        Notification notification;
        
        notification=new Notification(R.drawable.icon,"比赛即将开始",System.currentTimeMillis());
        
        int theid =0;
        if (intent.getExtras()!=null) {
            System.out.println(intent.getExtras());
            System.out.println("xxx:"+intent.getExtras().getInt("match_id", 0));
            theid = intent.getExtras().getInt("match_id", 0);
        }
        else {
            System.out.println("no bundle");
        }
        notification.contentView = adapterNoticeView(context,theid);
      
        Bundle bundle = new Bundle();
       // bundle.putInt("theid", 100);
      //  Bundle bundle = new Bundle();
      
        
        
      Intent notificationIntent = new Intent(context, SinaTrendActivity.class).setFlags(Intent. FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK)
           .putExtras(bundle) ;
      Match match;
    try {
        System.out.println("########"+theid+"***");
        match = getHelper(context).getMatchDao().queryForId(theid);
        System.out.println("########"+theid+"***"+match.getMatchTime().getTime());
        bundle.putInt("MATCH_ID", theid);
        
        bundle.putLong("MATCH_TIME", match.getMatchTime().getTime());
        bundle.putString("TITLE", match.getTeamA().getTeamName()+ " Vs " + match.getTeamB().getTeamName());
        bundle.putString("THEKEYWORD", match.getTeamA().getTeamName()+ " " + match.getTeamB().getTeamName() + " 足球");
        notificationIntent.putExtras(bundle);
    } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
       
      
       PendingIntent contentIntent = PendingIntent.getActivity(context, theid, notificationIntent,   0);  //request match_id
       notification.contentIntent = contentIntent;  
         
        notification.flags |= Notification.FLAG_ONGOING_EVENT; 
      //  notification.defaults = Notification.DEFAULT_SOUND; 
       
        notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "/" +R.raw.customsound); 
      //  notification.vibrate = new long[] { 1000, 1000, 1000, 1000, 1000 }; 
        nm.notify(theid, notification);  
         
    }
    DatabaseHelper helper;
    
    private DatabaseHelper getHelper(Context context) {
        if (helper == null) {
            helper =
                    OpenHelperManager.getHelper(context, DatabaseHelper.class);
        }
        return helper;
    }
    public RemoteViews adapterNoticeView(Context context,Integer match_id) {
        
      
        Match match;
        try {
            match = getHelper(context).getMatchDao().queryForId(match_id);
            if (match !=null) {
                
                RemoteViews contentView = new RemoteViews(context.getPackageName(),R.layout.noticeitem);  
                setHolderImageview(context,contentView,R.id.teamA_flag_imageView ,match.getTeamA().getTeamShortName());
                setHolderImageview(context,contentView,R.id.teamB_flag_imageView ,match.getTeamB().getTeamShortName()); 
                contentView.setTextViewText(R.id.teamA_name_textView, match.getTeamA().getTeamName());
                contentView.setTextViewText(R.id.teamB_name_textView, match.getTeamB().getTeamName());
                contentView.setTextViewText(R.id.match_datetime_textView, TimeTools.handleTimeWithTimezone(match.getMatchTime()) ) ;
                
            return contentView;
            }else
                return null;
        } catch (SQLException e) { 
            e.printStackTrace();
            return null;
        }
 
        
    }
    AQuery aQuery;
    private void setHolderImageview(Context context,RemoteViews contentView, int viewid,String shortName) {
        
        int teamFlagResId =context.getResources().getIdentifier(shortName, "drawable",context.getPackageName());
        if(teamFlagResId ==0){
            if(aQuery!=null)
                contentView.setImageViewBitmap(viewid, aQuery.getCachedImage(Constants.MATCHPICURLHEADER+shortName)); 
        }else
            contentView.setImageViewResource(viewid,teamFlagResId);
           
    }
  
}  
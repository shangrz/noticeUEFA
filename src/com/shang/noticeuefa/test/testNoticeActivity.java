package com.shang.noticeuefa.test;
 
import java.util.Calendar;

import com.shang.noticeuefa.AlamrReceiver;
import com.shang.noticeuefa.R;
import com.shang.noticeuefa.model.Match;
 
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

public class testNoticeActivity extends Activity {
    @Override
    protected void onDestroy() {
        System.out.println("onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        System.out.println("onPause");
        super.onPause();
    }

    @Override
    protected void onRestart() {
        System.out.println("onRestart");
        super.onRestart();
    }

    @Override
    protected void onStart() {
        System.out.println("onStart");
        super.onStart();
    }

    @Override
    protected void onStop() {
        System.out.println("onStop"); 
        super.onStop();
    }


    protected boolean isss = false;
    @Override
    protected void onResume() {
        System.out.println("onResume");
        
        super.onResume();
         
        Bundle bundle =  getIntent().getExtras();
        if (bundle != null) {
            int id =bundle.getInt("theid");
        if (id != 0 ) {
            Toast.makeText(getApplicationContext(), ""+id, 1000).show();
            isss =false;
            
        }
        }
        
    }

 
    NotificationManager nm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("oncreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testnotice);
        nm=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    }
    
    public void TestClearButtonOnClick(View v) {
        nm.cancelAll();
        
        
    }
    public void TestNoticeButtonOnClick(View v) {
        Button button =(Button)v;
        
      
        Notification notification;
        
        notification=new Notification(R.drawable.cro,"比赛即将开始",System.currentTimeMillis());
        int theid=100;
        notification.contentView = adapterNoticeView();
        Bundle bundle = new Bundle();
        bundle.putInt("theid", 100);
        Intent notificationIntent = new Intent(this, testNoticeActivity.class).setFlags(Intent. FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK)
            .putExtras(bundle) ;
        PendingIntent contentIntent = PendingIntent.getActivity(testNoticeActivity.this, 0, notificationIntent,   0);  
        notification.contentIntent = contentIntent;  
        notification.flags = Notification.FLAG_AUTO_CANCEL;   
        
      //  notification.defaults = Notification.DEFAULT_SOUND; 
        //notification.sound = Uri.parse("android.resource://" + getPackageName() + "/" +R.raw.customsound); 
        nm.notify(theid, notification);  
        isss =true;
        
        
    }
    public RemoteViews adapterNoticeView() {
        //Match match = Match.creatFromResId(R.array.e1,  getApplicationContext());
        Match match = Match.creatFromTagName("e1", getApplicationContext());
        RemoteViews contentView = new RemoteViews(getPackageName(),R.layout.noticeitem);  
        contentView.setImageViewResource(R.id.teamA_flag_imageView, match.teamA.getTeamFlagResId());
        contentView.setImageViewResource(R.id.teamB_flag_imageView, match.teamB.getTeamFlagResId());
        contentView.setTextViewText(R.id.teamA_name_textView, match.teamA.getTeamName());
        contentView.setTextViewText(R.id.teamB_name_textView, match.teamB.getTeamName());
        contentView.setTextViewText(R.id.match_datetime_textView, match.getMatchDatetimeLocal()) ;
        return contentView;
        
    }
    Calendar c ; 
    public void TestAlarmClockButtonOnClick(View v) {
          
        c = Calendar.getInstance();  
        c.setTimeInMillis(System.currentTimeMillis()); 
        System.out.println(c.get(Calendar.SECOND));
        c.set(Calendar.SECOND,c.get(Calendar.SECOND)+2);
        
        Intent intent = new Intent(testNoticeActivity.this,AlamrReceiver.class);  
         
        intent.putExtra("match_id", 2);
      
        PendingIntent pi = PendingIntent.getBroadcast(testNoticeActivity.this, 2, intent, 0);  
        AlarmManager am = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);  
        am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);//设置闹钟  
     //   am.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), (5*1000), pi);//重复设置  
        
    }
    
    public void TestCancelClockButtonOnClick(View v) {
        Intent intent = new Intent(testNoticeActivity.this,AlamrReceiver.class);  
        PendingIntent pi = PendingIntent.getBroadcast(testNoticeActivity.this, 2, intent, 0);  
        AlarmManager am = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);  
        am.cancel(pi);  
        Toast.makeText(testNoticeActivity.this, "闹钟取消", Toast.LENGTH_LONG).show();  
         
    }
    
    @Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  
    {  
        if(requestCode ==100) 
           Toast.makeText(this, "123", 1000).show();
  
    }   
 
}

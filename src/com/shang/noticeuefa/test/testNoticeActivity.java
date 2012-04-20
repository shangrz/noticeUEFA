package com.shang.noticeuefa.test;

  
import com.shang.noticeuefa.R;

import android.R.anim;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
 
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
        // TODO Auto-generated method stub
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
        notification.defaults = Notification.DEFAULT_SOUND;
  //      getResources().getDrawable(android.R.drawable.status_bar_item_background)
       
        nm.notify(theid, notification);  
        isss =true;
        
        
    }
    public RemoteViews adapterNoticeView() {
        RemoteViews contentView = new RemoteViews(getPackageName(),R.layout.noticeitem);  
        contentView.setImageViewResource(R.id.teamA_flag_imageView, R.drawable.cro);
        contentView.setImageViewResource(R.id.teamB_flag_imageView, R.drawable.den);
        contentView.setTextViewText(R.id.teamA_name_textView, "cro");
        contentView.setTextViewText(R.id.teamB_name_textView, "den");
         
        return contentView;
        
    }
    
    @Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  
    {  
        if(requestCode ==100) 
           Toast.makeText(this, "123", 1000).show();
  
    }   

}

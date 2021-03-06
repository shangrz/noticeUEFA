package com.shang.noticeuefa;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.zip.Inflater;

import android.R.array;
import android.R.integer;
import android.R.xml;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
 
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.format.Time;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.*;
 

import com.actionbarsherlock.R;
import com.actionbarsherlock.R.string;
import com.actionbarsherlock.app.SherlockActivity;
import com.androidquery.AQuery;
 
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;
import com.mobclick.android.MobclickAgent;
import com.shang.noticeuefa.database.DatabaseHelper;
import com.shang.noticeuefa.model2.Match;
import com.shang.noticeuefa.model2.Notification;
 
import com.shang.noticeuefa.test.testNoticeActivity;
import com.shang.noticeuefa.util.Constants;
import com.shang.noticeuefa.util.MyGestureListener;
import com.shang.noticeuefa.weibo.SinaTrendActivity;
import com.srz.androidtools.util.AlarmSender;
import com.srz.androidtools.util.TimeTools;

class MatchListViewAdapter   extends ArrayAdapter< Match> {
    public static String UMENG_EVENT_TAG = "alarm_which_match";
    interface IQuery{
        void doQuery();
    }
    protected IQuery mQuery;
    
    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public Match getItem(int position) {
        
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
         
        return super.getItemId(position);
    }

    @Override
    public int getPosition(Match item) {
        
        return super.getPosition(item);
    }
    
    private Vector<Boolean> m_selects = new Vector<Boolean>(); 
    public Vector<Boolean> getM_selects() {
        return m_selects;
    }

    public void setM_selects(Vector<Boolean> m_selects) {
        this.m_selects = m_selects;
    }

    public boolean changeState(int position,boolean  multiChoose){ 
        // 多选时 
        
        //改变模式为单选，这里只是为了改了方便，内部其实还是多选，只是 有选第2个时候把前一个去掉
        for (int i=0; i<m_selects.size();i++) {
            if(position != i)
                m_selects.setElementAt(false, i); 
        }
        
        
        if(multiChoose == true){     
            
            m_selects.setElementAt(!m_selects.elementAt(position), position);   //直接取反即可     
        } 
        MULTI_MODE = multiChoose;
        
        notifyDataSetChanged();     //通知适配器进行更新 
        return m_selects.get(position);
    } 
    
    public void endActionMode(){ 
        
        
        MULTI_MODE = false;
        for(int i=0; i<this.getCount(); i++) 
            m_selects.setElementAt(false,i);
       
       clearAllAnim();
        notifyDataSetChanged();     //通知适配器进行更新 
    } 
    
    public int getSelectedCount() {
        int i = 0;
        for (boolean isSelected:m_selects) {
            if(isSelected) i+=1;
        }
       
        return i;
    }

 
   public static boolean MULTI_MODE = false;

   public boolean NEEDQUERYFOLLOW = false;

   
    private LayoutInflater mInflater;
    
    
    private int listItemViewResourceId = R.layout.match_listitem;
 
    Context context ;
    DatabaseHelper helper;
    AQuery aQuery;
    Activity activity;
//    public MatchListViewAdapter(Context context,  List<Match> matchs,DatabaseHelper helper) {
//      
//        super(context, 0,matchs );
//        this.mInflater = LayoutInflater.from(context);
//        aQuery = new AQuery(context);
//        this.matchs =matchs;
//        this.context= context;
//        this.helper = helper;
//        for(int i=0; i<matchs.size(); i++) 
//            m_selects.add(false); 
//     
//    }
    
    
    public MatchListViewAdapter(Context context,Activity activity,DatabaseHelper helper) {
        super(context, 0 );
        this.mInflater = LayoutInflater.from(context);
        aQuery = new AQuery(activity);
        this.activity =activity;
        this.helper = helper;
        this.context= context;
    }

    class MyOnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {
        ViewHolder holder;
        int position;
        Match match;
        public MyOnCheckedChangeListener(ViewHolder holder,int position,Match match){
            this.holder = holder;
            this.position = position;
            this.match = match;
        }
        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                boolean isChecked) {
            
              
            
            
/*            if(match.getNotifications() != null) {
                match.getNotifications().setAlarm(isChecked);
                try {
                    helper.getDao(Notification.class).update(match.getNotifications());
                } catch (SQLException e) {
                    
                    e.printStackTrace();
                }
            }else {
                Notification n1 = new Notification();
                n1.setAlarm(isChecked);
                n1.setFollow(true);
                try {
                 helper.getDao(Notification.class).create(n1);
                 match.setNotifications(n1);
                 helper.getMatchDao().update( match);
                    } catch (SQLException e) {
                 e.printStackTrace();
             } 
                
            }*/
        }
    };
    public boolean islistviewitemTouch = false;
    public View getView(final int position, View convertView,
            ViewGroup parent) {
        
         
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(listItemViewResourceId, null);

            holder.teamA_ImageView = (ImageView) convertView.findViewById(R.id.teamA_flag_imageView);
            holder.teamB_ImageView = (ImageView) convertView.findViewById(R.id.teamB_flag_imageView);
            holder.teamA_Name_TextView = (TextView)convertView.findViewById(R.id.teamA_name_textView);
            holder.teamB_Name_TextView = (TextView)convertView.findViewById(R.id.teamB_name_textView);
            holder.matchdatetimeTextView = (TextView)convertView.findViewById(R.id.match_datetime_textView); 
            holder.checkBox = (CheckBox)convertView.findViewById(R.id.notice_checkBox);
            
            holder.checkBox.setClickable(false);
            holder.highlightView = convertView.findViewById(R.id.tag_linearLayout);
       
            convertView.setTag(holder);
 

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        //Match match = matchs.get(position); 
        final Match match = this.getItem(position);
        
        
      //  convertView.findViewById(R.id.animImageView).setVisibility(4);
     
 
        
        holder.teamA_Name_TextView.setText(match.getTeamA().getTeamName());
        holder.teamB_Name_TextView.setText(match.getTeamB().getTeamName());
        
        
        setHolderImageview(convertView,holder.teamA_ImageView,match.getTeamA().getTeamShortName(),R.id.progress1);
        setHolderImageview(convertView,holder.teamB_ImageView,match.getTeamB().getTeamShortName(),R.id.progress2);
      
        
       // 时间输入时均为GMT+8的时间，输出时按手机local时区
         
        holder.matchdatetimeTextView.setText(TimeTools.handleTimeWithTimezone(match.getMatchTime()) );
        
        holder.checkBox.setOnCheckedChangeListener( new MyOnCheckedChangeListener(holder, position,match));
        
        
        
         holder.checkBox.setChecked(match.getNotifications() == null?false:match.getNotifications().isAlarm()); 
         
        if (MULTI_MODE)
            if(m_selects.get(position)){
                convertView.setBackgroundResource(R.color.red2);
           // convertView.setSelected(m_selects.get(position));
            }
            else {
                convertView.setBackgroundResource(R.color.black);
            }
        else {
            convertView.setBackgroundResource(R.color.black);
        }
          
/*        if( convertView.findViewById(R.id.animImageView).getAnimation() != null) 
            convertView.findViewById(R.id.animImageView).getAnimation().cancel();*/
        if(m_selects.get(position)) {
            
            startAnim(convertView, position);
        }
        final MyGestureListener myGestureListener = new MyGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                islistviewitemTouch = false;
                return false;
            }
            
            @Override
            public void runWhenToRight() {
                if(!islistviewitemTouch) {
                     
                    return;
                }
                islistviewitemTouch = false;
                
                    final Intent nextIntent = new Intent();
                  if(match != null) {
                     Bundle bundle = new Bundle();
                     bundle.putString("TITLE", match.getTeamA().getTeamName()+ " Vs " + match.getTeamB().getTeamName());
                     bundle.putString("THEKEYWORD",  match.getTeamA().getTeamName()+" "+ match.getTeamB().getTeamName()+" 足球");
                     nextIntent.putExtras(bundle);
                    }
                    nextIntent.setClass(context,  SinaTrendActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.runOnUiThread(new Runnable() {
                        
                        @Override
                        public void run() {
                            
                            PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("SHOWGOTOWEIBOANIM", false).commit();
                            clearAllAnim();
                            context.startActivity(nextIntent);   
                            
                        }
                    });
                   // context.startActivity(nextIntent);   
                     
            }

            @Override
            public void runWhenToLeft() {
                islistviewitemTouch = false;
            }

            @Override
            public void doSomething() {
                islistviewitemTouch = false;
            }
            
             
            
        };
        myGestureListener.set(activity,100,0);  
        
        convertView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                System.out.println("convertView onTouch");
                islistviewitemTouch = true;
                if(PreferenceManager.getDefaultSharedPreferences(context).getBoolean("SHOWGOTOWEIBOANIM", true)) {
                final View target = v.findViewById(R.id.animImageView);
                
                if(animMap.containsKey(position)) {
                    animMap.get(position).cancel();
                    animMap.remove(position);
          
                }
                    Animation a = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 
                            0, Animation.RELATIVE_TO_SELF, 0 );
                    a.setAnimationListener(new AnimationListener() {
                        
                        @Override
                        public void onAnimationStart(Animation animation) {
                            target.setVisibility(View.VISIBLE);
                        }
                        
                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                        
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            target.setVisibility(View.INVISIBLE);
                            System.out.println("onAnimationEndxxx");
                            
                        }
                    });
                    a.setDuration(1000);
                    a.setStartOffset(0);
     
                     a.setRepeatCount(1);
                     a.setInterpolator(AnimationUtils.loadInterpolator(v.getContext(),
                            android.R.anim.accelerate_interpolator));
                    target.startAnimation(a);
                    
                    if(!animMap.containsKey(position))
                        animMap.put(position, a);
                }
                 
                gestureDetector = new GestureDetector(myGestureListener);
                return  gestureDetector.onTouchEvent(event); 
               // return false;
            }
        });
    //    holder.highlightView.setBackgroundColor(getContext().getResources().getColor(holder.checkBox.isChecked()?R.color.red:R.color.taggray));
        return convertView;
    }
    public GestureDetector gestureDetector;
    private void setHolderImageview( View convertView,ImageView v, String shortName,int progressid) {
         
        int teamFlagResId =context.getResources().getIdentifier(shortName, "drawable",context.getPackageName());
        if(teamFlagResId ==0){
          
            aQuery.recycle(convertView).id(v).image(Constants.MATCHPICURLHEADER+shortName,true,true,0,android.R.drawable.stat_notify_sync_noanim);
           
           
          //   aQuery.id(v).image(Constants.MATCHPICURLHEADER+shortName,true,true,0,android.R.drawable.stat_notify_sync_noanim);
             
        }else
            v.setImageResource(teamFlagResId);
        
    }
 
    public static class ViewHolder  {
        public final static String TEAM_A_NAME ="TEAM_A_NAME";
        public final static String TEAM_B_NAME ="TEAM_B_NAME";
        public final static String CHECKED ="CHECKED";
        public final static String MATCHDATETIME ="MATCHDATETIME";
        public ImageView teamA_ImageView;
        public ImageView teamB_ImageView;
        public TextView teamA_Name_TextView;
        public TextView teamB_Name_TextView; 
        public TextView matchdatetimeTextView;
        public CheckBox checkBox;
        public View highlightView;
    }
    
    
    Dialog dialog;
    private Dialog getDialog() {
        if(dialog == null) {
            AlertDialog dialogProgress = new ProgressDialog(this.activity);
            dialogProgress.setMessage("处理中");
            dialogProgress.setCancelable(true);
            ((ProgressDialog) dialogProgress).setIndeterminate(true);
            this.dialog =dialogProgress;
            return this.dialog;
        }
        else {
            return dialog;
        }
    }
    
    
    private void setAlarm(boolean isNotice,int i) {
        AlarmSender.layoutResid = R.layout.toast;
        int _match_id = getItem(i).getId(); 
        
        System.out.println("_match_id");
        System.out.println("_match_id"+_match_id);
        Intent intent = new Intent(context,AlamrReceiver.class); 
        System.out.println("match9d:"+i+" time "+new Date(getItem(i).getMatchTime().getTime() - 15*60*1000));
       
        Bundle bundle =new Bundle();
         
        bundle.putInt("match_id", _match_id);
        intent.putExtras(bundle);
        if(isNotice ) { 
            if(getItem(i).getMatchTime().getTime() > System.currentTimeMillis()) {
                MobclickAgent.onEvent(this.getContext(), UMENG_EVENT_TAG,String.valueOf(_match_id));
                PendingIntent pi = PendingIntent.getBroadcast(context, _match_id, intent, 0);  
                AlarmManager am = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);  
                am.set(AlarmManager.RTC_WAKEUP, getItem(i).getMatchTime().getTime() - 15*60*1000, pi);
                AlarmSender.sendInstantMessage(com.shang.noticeuefa.R.string.noticematchbefore15m, context);
              
            }
        }else {
            intent.putExtra("delnotice", "delnotice");
            PendingIntent pi = PendingIntent.getBroadcast(context, _match_id, intent, 0);  
            AlarmManager am = (AlarmManager)context.getSystemService(Activity.ALARM_SERVICE);  
            am.cancel(pi);  
            AlarmSender.sendInstantMessage(com.shang.noticeuefa.R.string.cancelalarm, context);
            
        }
    }
    
    public void setAllSelectedNotice(final boolean isNotice) {
       final Vector<Boolean> _selects =  (Vector<Boolean>) m_selects.clone() ;
       getDialog().show();
      
        new Handler().postDelayed( new Thread(new Runnable() {
            @Override
            public void run() { 
                
                
                
                List<Integer> ids = new ArrayList<Integer>();
                
                for(int i = 0;i<_selects.size();i++) {
                    
                    if(_selects.get(i) ) {
                        
                       // matchs.get(i).setNotice(isNotice);
                         if (getItem(i).getNotifications() != null) {
                             
                             if (getItem(i).getNotifications().isAlarm() != isNotice) {
                                 getItem(i).getNotifications().setAlarm(isNotice);
                                 
                                 setAlarm( isNotice, i);
                                  
                    
                                 ids.add( getItem(i).getNotifications().getId());
//                                  try {
//                                      helper.getDao(Notification.class).update(
//                                              this.getItem(i).getNotifications());
//                                      
//                                  } catch (SQLException e) {
//                                      e.printStackTrace();
//                                  }
                             }
                         }
                        else {
                            Notification n1 = new Notification();
                            n1.setAlarm(isNotice);
                            n1.setFollow(true);
                            
                            
                            try {
                             helper.getDao(Notification.class).create(n1);
                             getItem(i).setNotifications(n1);
                              helper.getMatchDao().update(getItem(i));
                              
                              setAlarm( isNotice, i);
                                } catch (SQLException e) {
                             e.printStackTrace();
                         }
                            
                        }
                            
                    }
                }
             try {
                 UpdateBuilder<Notification, ?> updateBuilder = helper.getDao(
                         Notification.class).updateBuilder();
                 updateBuilder.updateColumnValue("alarm", isNotice);
                 updateBuilder.where().in("id", ids);
                 helper.getDao(Notification.class).update(updateBuilder.prepare());
             } catch (SQLException e) {
                 e.printStackTrace();
             }
                 activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                        getDialog().dismiss();
                    }
                });
                 
                
                
             }
         }), 10);
 
        
        
    }

    public void selectAll(boolean b) {
        if(b) {
            MULTI_MODE = b;
            for(int i=0; i< this.getCount(); i++) 
                m_selects.setElementAt(true,i);
            notifyDataSetChanged();   
        }
        
    }
    
    private void doWhenChange(List<Match> matchs) {
        this.clear();
        m_selects.clear();
        clearAllAnim();
        for(Match match :matchs) {
            this.add(match);
        }
       // this.addAll(matchs);
        for(int i=0; i<this.getCount(); i++) 
            m_selects.add(false); 
     
        notifyDataSetChanged();
        noticeNoMatch();
         
    }
    
    private void noticeNoMatch() {
        if(this.getCount() == 0) {
           // Toast.makeText(activity.getApplicationContext(), com.shang.noticeuefa.R.string.nomatch, 1000).show();
            aQuery.id(R.id.nomatch_imageview).visible();
        }
        else {
            aQuery.id(R.id.nomatch_imageview).invisible();
        }
    }
 
    
    public void toAllMatch() {
        mQuery = new IQuery() {
            @Override
            public void doQuery() {
                try {
                    if(NEEDQUERYFOLLOW)
                         doWhenChange(helper.getMatchDao().query(getQuery().prepare()));
                    else {
                        Calendar  cal = Calendar.getInstance();
                        cal.set(Calendar.MINUTE, 0);
                        cal.set(Calendar.SECOND, 0);
                        cal.set(Calendar.HOUR_OF_DAY, 0); 
                        Date d1 = cal.getTime();
                        
                        
                        doWhenChange(helper.getMatchDao().query(getQuery().gt("matchTime", getTodayDate()).prepare() ));
                         
                    }
                       // doWhenChange( helper.getMatchDao().queryForAll());
                       
                } catch (SQLException e) {
                    e.printStackTrace();
                    
                }
                
            }
        };
        mQuery.doQuery(); 
        
        
        
        
    }
    
    private Where<Match, Integer> getQuery() {
        QueryBuilder<Match, Integer> queryBuilder;
        try {
            queryBuilder = helper.getMatchDao().queryBuilder();
            if(NEEDQUERYFOLLOW) {
                return queryBuilder.where().in("teamA", helper.getTeamDao().queryForEq("followed", true))
                .or().in("teamB", helper.getTeamDao().queryForEq("followed", true)).and();
                
                //return queryBuilder.where().in("notifications", helper.getDao(Notification.class).queryForEq("follow", true)).and() ;
                
            }
            else
                return queryBuilder.where();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } 
         
    }
    public void toTodayMatch() {
        
        mQuery = new IQuery() {
            @Override
            public void doQuery() {
                Calendar  cal = Calendar.getInstance(); 
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                Date d1 = cal.getTime();
                 
                cal.add(Calendar.HOUR_OF_DAY, 30);
                Date d2 = cal.getTime();
                doWhenChange( getMatchWhen(d1,d2));
                
            }
        };
        mQuery.doQuery(); 
        
         
       
    }
    
    public List<Match> getMatchWhen(Date d1, Date d2) {
        try {
            return helper.getMatchDao().query(getQuery().between("matchTime", d1, d2).prepare());
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public void toThisWeekMatch() {
        mQuery = new IQuery() {
            @Override
            public void doQuery() {
                Date[] ds  = makeDates(0);
 
                doWhenChange( getMatchWhen(getTodayDate(),ds[1]));
            }
        };
        mQuery.doQuery(); 
    }
    
    public void toNextWeekMatch() {
        mQuery = new IQuery() {
            @Override
            public void doQuery() {
                Date[] ds =makeDates(7);
                doWhenChange( getMatchWhen(ds[0],ds[1]));
                
            }
        };
        mQuery.doQuery(); 
        
         
    }
    
    public void toAfter2DaysMatch() {
        mQuery = new IQuery() {
            @Override
            public void doQuery() {
                Calendar  cal = Calendar.getInstance();
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.add(Calendar.DATE, 1);
                Date d1 = cal.getTime();
                cal.add(Calendar.HOUR_OF_DAY, 54);
                Date d2 = cal.getTime();
                doWhenChange( getMatchWhen(d1,d2 ));
                
            }
        };
        mQuery.doQuery(); 
         
    }
    
    private Date[] makeDates(int days) {
        Calendar  cal = Calendar.getInstance();
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        int day_of_week = cal.get(Calendar.DAY_OF_WEEK) - 2;
        cal.add(Calendar.DATE, days-day_of_week);
        Date d1 = cal.getTime();
        cal.add(Calendar.HOUR_OF_DAY, 174);
        Date d2 = cal.getTime();
        return   new Date[] { d1,d2};
       
    }
    
    public Map<Integer , Animation> animMap = new HashMap<Integer , Animation>();
    public void startAnim(View view , int i) {
       
            
            
        
        
        if(animMap.containsKey(i)) {
            animMap.get(i).cancel();
            animMap.remove(i);
  
        }
        
/*        final View target = view.findViewById(R.id.animImageView);
        final View targetParent = (View) target.getParent();
        System.out.println(targetParent.getWidth()+":::"+targetParent.getPaddingRight());
//        Animation a = new TranslateAnimation( 
//                targetParent.getWidth()/1.5f -targetParent.getPaddingRight(), targetParent.getWidth()/2,0.0f, 0.0f);
        Animation a = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 
                0, Animation.RELATIVE_TO_SELF, 0 );
                
        a.setAnimationListener(new AnimationListener() {
            
            @Override
            public void onAnimationStart(Animation animation) {
                target.setVisibility(View.VISIBLE);
            }
            
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
            
            @Override
            public void onAnimationEnd(Animation animation) {
                target.setVisibility(View.INVISIBLE);
                System.out.println("onAnimationEnd");
                
            }
        });
        a.setDuration(3000);
        a.setStartOffset(0);
       
//         a.setRepeatMode(Animation.RESTART);
//         a.setRepeatCount(Animation.INFINITE);
       //  a.setRepeatMode(Animation.REVERSE);
         a.setRepeatCount(2);
         a.setInterpolator(AnimationUtils.loadInterpolator(view.getContext(),
                android.R.anim.accelerate_interpolator));
        target.startAnimation(a);
        
        if(!animMap.containsKey(i))
            animMap.put(i, a);*/
    }
    
    public void stopAnim(  int i ) {
        if(animMap.containsKey(i)) {
            animMap.get(i).cancel();
            animMap.remove(i);
        }
    }
    
    public void clearAllAnim() {
        for(Integer i:animMap.keySet()) {
            animMap.get(i).cancel();
        }
        animMap.clear();
    }
    
    public void doWhenChangeIsFollow(){
        if(mQuery!=null)
            mQuery.doQuery();
    }
    
    private Date getTodayDate() {
        Calendar  cal = Calendar.getInstance();
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0); 
        return cal.getTime();
    }
    
 
     
    
}
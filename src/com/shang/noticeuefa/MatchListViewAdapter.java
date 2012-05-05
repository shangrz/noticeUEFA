package com.shang.noticeuefa;

import java.util.Vector;

import android.R.integer;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.shang.noticeuefa.model2.Match;
import com.shang.noticeuefa.model2.Tour;
import com.srz.androidtools.util.TimeTools;

class MatchListViewAdapter   extends ArrayAdapter< Match> {
    
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
    public void changeState(int position,boolean  multiChoose){ 
        // 多选时 
        if(multiChoose == true){     
            m_selects.setElementAt(!m_selects.elementAt(position), position);   //直接取反即可     
        } 
        MULTI_MODE = multiChoose;
        
        notifyDataSetChanged();     //通知适配器进行更新 
    } 
    
    public void endActionMode(){ 
        
        
        MULTI_MODE = false;
        for(int i=0; i<tour.size(); i++) 
            m_selects.setElementAt(false,i);
        
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

 

    private ListView listView;
    private LayoutInflater mInflater;
    Tour tour ;
    private int listItemViewResourceId = R.layout.match_listitem;
 
    Activity activity ;
    public MatchListViewAdapter(Activity activity, Tour tour) {
          
        super(activity, 0,tour );
        this.mInflater = LayoutInflater.from(activity.getBaseContext());
        
        this.tour =tour;
        this.activity= activity;
        
        for(int i=0; i<tour.size(); i++) 
            m_selects.add(false); 
     
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
         //   holder.highlightView.setBackgroundColor(getContext().getResources().getColor(isChecked?R.color.red:R.color.taggray));
          //  listItem.get(position).put(ViewHolder.CHECKED, isChecked);
            match.setNotice(isChecked);
        }
    };
    
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
        
        Match match = tour.get(position); 
        holder.teamA_Name_TextView.setText(match.getTeamA().getTeamName());
        holder.teamB_Name_TextView.setText(match.getTeamB().getTeamName());
        
        int teamFlagResId =this.activity.getResources().getIdentifier(match.getTeamA().getTeamShortName(), "drawable",this.activity.getPackageName());
        holder.teamA_ImageView.setImageResource(teamFlagResId);
        
        teamFlagResId =this.activity.getResources().getIdentifier(match.getTeamB().getTeamShortName(), "drawable",this.activity.getPackageName());
        holder.teamB_ImageView.setImageResource(teamFlagResId);
        
        
       // 时间输入时均为GMT+8的时间，输出时按手机local时区
         
        holder.matchdatetimeTextView.setText(match.getMatchTime().toString());
        
        holder.checkBox.setOnCheckedChangeListener( new MyOnCheckedChangeListener(holder, position,match));
        
        holder.checkBox.setChecked(match.getNotifications()); 
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
          
        
    //    holder.highlightView.setBackgroundColor(getContext().getResources().getColor(holder.checkBox.isChecked()?R.color.red:R.color.taggray));
        return convertView;
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

    public void setAllSelectedNotice(boolean isNotice) {
       for(int i = 0;i<m_selects.size();i++) {
           if(m_selects.get(i) ) {
               tour.get(i).setNotice(isNotice);
           }
       }
    }

    public void selectAll(boolean b) {
        if(b) {
            MULTI_MODE = b;
            for(int i=0; i<tour.size(); i++) 
                m_selects.setElementAt(true,i);
            notifyDataSetChanged();   
        }
        
    }
}
package com.shang.noticeuefa;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.shang.noticeuefa.model.Match;
import com.shang.noticeuefa.model.Tour;

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
 
 

 

    private ListView listView;
    private LayoutInflater mInflater;
    Tour tour ;
    private int listItemViewResourceId = R.layout.match_listitem;
 
   
    public MatchListViewAdapter(Activity activity, Tour tour) {
          
        super(activity, 0,tour );
        this.mInflater = LayoutInflater.from(activity.getBaseContext());
        
        this.tour =tour;
     
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
            holder.highlightView = convertView.findViewById(R.id.tag_linearLayout);
       
            convertView.setTag(holder);
 

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        Match match = tour.get(position); 
        holder.teamA_Name_TextView.setText(match.teamA.getTeamName());
        holder.teamB_Name_TextView.setText(match.teamB.getTeamName());
        holder.teamA_ImageView.setImageResource(match.teamA.getTeamFlagResId());
        holder.teamB_ImageView.setImageResource(match.teamB.getTeamFlagResId());
        
        
       // 时间输入时均为GMT+8的时间，输出时按手机local时区
        holder.matchdatetimeTextView.setText(match.getMatchDatetimeLocal());
        
        holder.checkBox.setOnCheckedChangeListener( new MyOnCheckedChangeListener(holder, position,match));
        
        holder.checkBox.setChecked(match.isNotice()); 
        
        
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
}
package com.shang.noticeuefa;
 
import java.security.PublicKey;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.CopyOnWriteArrayList;

 
import com.shang.noticeuefa.MatchListViewAdapter.ViewHolder;
import com.shang.noticeuefa.util.TimeTools;
 
import greendroid.app.GDActivity;
import greendroid.widget.ActionBarItem;
 
import greendroid.widget.NormalActionBarItem;
 
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton; 
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import greendroid.widget.ActionBarItem.Type;
public class MainActivity extends     GDActivity {
    private final int ACTION_BAR_ALARM_MAIN = 0;
    private final int ACTION_BAR_ALARM_SETTING = 1;
    private boolean ISMUTE = false;
    private ListView listView;
    List<Map<String, Object>> listItems = new CopyOnWriteArrayList<Map<String, Object>>();
     
//    public MainActivity() {
//        super(greendroid.widget.ActionBar.Type.Empty);
//        
//    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarContentView(R.layout.main);
        setHomwButton();
     
        addActionBarItem(getActionBar().newActionBarItem(NormalActionBarItem.class).setDrawable(R.drawable.sound).setContentDescription(R.string.gd_export),
                ACTION_BAR_ALARM_SETTING);
        this.setTitle(R.string.matchname);
        this.getActionBar().setBackgroundColor(getResources().getColor(R.color.barblue));
         
        
        testload();
        listView = (ListView) findViewById(R.id.listView1); 
         
        MatchListViewAdapter adapter = new MatchListViewAdapter(MainActivity.this, listItems); 
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
             //   ((ViewHolder)view.getTag()).checkBox.setChecked( !((ViewHolder)view.getTag()).checkBox.isChecked());
                ((ViewHolder)view.getTag()).checkBox.toggle();
            }
        });
        
         
    }
    
    
    protected void setHomwButton() {
        ((ImageButton)getActionBar().getChildAt(0)).setImageResource(R.drawable.eurologo);
        ((ImageButton)getActionBar().getChildAt(0)).setOnClickListener(new ImageButton.OnClickListener() {

            @Override
            public void onClick(View v) {
                 
            }
        });
    }
    
    private void testload( ) {
        for (int i= 0; i<10;i++) {
        final Map<String, Object> map = new HashMap<String, Object>(); 
        map.put(ViewHolder.TEAM_A_NAME,"teamA"+String.valueOf(i));
        map.put(ViewHolder.TEAM_B_NAME,"teamB"+String.valueOf(i));
        map.put(ViewHolder.CHECKED,i%2 ==0?true:false);
        map.put(ViewHolder.MATCHDATETIME,"2012 06-19 00:00");
        listItems.add(map);
        }
    }
    
    @Override
    public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
        switch (item.getItemId()) {
         

        case ACTION_BAR_ALARM_SETTING:
            ISMUTE = !ISMUTE;
            if(ISMUTE) {
                item.setDrawable(R.drawable.mute); 
                
            }else {
                item.setDrawable(R.drawable.sound);
                
            }
            break;
        default:
            return super.onHandleActionBarItemClick(item, position);
    }
        return true;
    }
}

class MatchListViewAdapter   extends ArrayAdapter< Map<String, Object> > {
    @Override
    public int getCount() {
        
        return super.getCount();
    }

    @Override
    public Map<String, Object> getItem(int position) {
        
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
         
        return super.getItemId(position);
    }

    @Override
    public int getPosition(Map<String, Object> item) {
        
        return super.getPosition(item);
    }

    private ListView listView;
    private LayoutInflater mInflater;
    List<Map<String, Object>> listItem ;
    private int listItemViewResourceId = R.layout.match_listitem;
    public MatchListViewAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        // TODO Auto-generated constructor stub
    }
    
    public MatchListViewAdapter(Activity activity, List<Map<String, Object>> listItem ) {
        super(activity, 0,listItem);
        this.mInflater = LayoutInflater.from(activity.getBaseContext());
        this.listItem =listItem;
        
  
     
    }

    class MyOnCheckedChangeListener implements OnCheckedChangeListener  {
        ViewHolder holder;
        int position;
        public MyOnCheckedChangeListener(ViewHolder holder,int position){
            this.holder = holder;
            this.position = position;
        }
        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                boolean isChecked) {
            holder.highlightView.setBackgroundColor(getContext().getResources().getColor(isChecked?R.color.tagblue:R.color.taggray));
            listItem.get(position).put(ViewHolder.CHECKED, isChecked);
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
       
        holder.teamA_Name_TextView.setText((String) listItem.get(position).get(ViewHolder.TEAM_A_NAME));
        holder.teamB_Name_TextView.setText((String) listItem.get(position).get(ViewHolder.TEAM_B_NAME));
        
        
       // 时间输入时均为GMT+8的时间，输出时按手机local时区
        holder.matchdatetimeTextView.setText(TimeTools.handleTimeWithTimezone((String) listItem.get(position).get(ViewHolder.MATCHDATETIME)));
        holder.checkBox.setOnCheckedChangeListener( new MyOnCheckedChangeListener(holder, position));
        holder.checkBox.setChecked((Boolean) listItem.get(position).get(ViewHolder.CHECKED)); 
        holder.highlightView.setBackgroundColor(getContext().getResources().getColor(holder.checkBox.isChecked()?R.color.tagblue:R.color.taggray));
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
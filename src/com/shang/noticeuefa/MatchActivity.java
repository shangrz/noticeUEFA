package com.shang.noticeuefa;
 

import java.lang.ref.SoftReference;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import com.actionbarsherlock.R;
import com.actionbarsherlock.app.SherlockActivity;
   
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.actionbarsherlock.view.Window;
import com.androidquery.AQuery;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.shang.noticeuefa.database.DatabaseHelper;
import com.shang.noticeuefa.model2.Group;
import com.shang.noticeuefa.model2.Match;
import com.shang.noticeuefa.model2.Notification;
import com.shang.noticeuefa.model2.Tour;
import com.srz.androidtools.util.ResTools;
import com.srz.androidtools.util.TimeTools;
 
 
 
import android.R.bool;
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.format.DateUtils;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
 

 
 

public class MatchActivity extends SherlockActivity   {
    
    
    ActionMode mMode;
    GestureDetector mGestureDetector;    
    Context mContext;
    private ViewPager pager;
    private Handler handler;
    private MatchGalleryAdapter galleryadapter;
    private int index = 0;
    private ListView listView;
    public static boolean MODE_NOW =false;
    Tour tour ;
    private MatchListViewAdapter listAdapter;
    
    HashMap<String, SoftReference<Drawable>> imageCache; 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
 
         menu.add(0,1,0,"SelectAll")
         .setIcon(  R.drawable.content_select_all)
         .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT); 
         
         
         SubMenu subMenu1 = menu.addSubMenu("Collection");
         subMenu1.add(1,6,1,R.string.today);
          
         subMenu1.add(1,3,2,R.string.todayandafter);
          
         subMenu1.add(1,4,3,R.string.thisweek);
         subMenu1.add(1,5,4,R.string.nextweek);
         subMenu1.add(1,2,5,R.string.all);
          
         MenuItem subMenu1Item = subMenu1.getItem();
         subMenu1Item.setIcon(R.drawable.collections_go_to_today);
         subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
      
         /*menu.add("Collection")
           .setIcon(R.drawable.collections_go_to_today)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM); */
      /*  menu.add("Search")
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT); 
*/
         

        return true;
    }
     
        
    
    private DatabaseHelper databaseHelper = null;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }
    
    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper =
                    OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }
   
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_MyStyle); //Used for theme switching in samples
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match);
       // this.getWindow().setBackgroundDrawableResource(R.color.red);
        getSupportActionBar().setTitle(R.string.today_push_match);
        handler = new Handler();
        imageCache = new  HashMap<String, SoftReference<Drawable>>(); 
        this.setTitle(R.string.matchname);
        listView = (ListView) findViewById(R.id.listView1); 
    //    tour =Tour.creatFromTagName("euro", getApplicationContext());
        
         
      //  listView.setAdapter(new MatchListViewAdapter(MatchActivity.this,getTodayMatch(),getHelper()));
        listAdapter = new MatchListViewAdapter(getApplicationContext(),this,getHelper());
        listAdapter.toTodayMatch();
        
        listView.setAdapter(listAdapter );
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(MODE_NOW == false)  {
                   mMode = startActionMode(new AnActionModeOfEpicProportions(MatchActivity.this));
                    
                           View closeButton =  findViewById(R.id.abs__action_mode_close_button);
                           if (closeButton != null) {
                            //   closeButton.setEnabled(false);
                               closeButton.setVisibility(View.GONE);
                           }
                           
                   
              
                }
                
                ((MatchListViewAdapter) listView.getAdapter()).changeState(i, true);
                if(((MatchListViewAdapter) listView.getAdapter()).getSelectedCount() == 0)
                    mMode.finish();
                mMode.setTitle(((MatchListViewAdapter) listView.getAdapter()).getSelectedCount()+" 已选择");
                 
                 
                //((mMode.getCustomView()).findViewById(R.layout.abs__action_mode_close_item)).
              //  findViewById(R.id.abs__action_mode_close_button).setVisibility(View.INVISIBLE);
                
                
              //     view.setBackgroundResource(R.color.red2);
             //   ((ViewHolder)view.getTag()).checkBox.setChecked( !((ViewHolder)view.getTag()).checkBox.isChecked());
             //   ((ViewHolder)view.getTag()).checkBox.toggle();
            }
        });
        
        pager = (ViewPager) findViewById(R.id.image_gallery);
        galleryadapter = new MatchGalleryAdapter(this,listAdapter ,imageCache);
        pager.setAdapter(galleryadapter);
        pager.setCurrentItem(0);
        
        pager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                  //  startNewSchedule();
                }
                return false;
            }
        });

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onPageSelected(int i) {
                index = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        
//       mGestureDetector = new GestureDetector(new GestureListener());  
//       listView.setOnTouchListener(new TouhListener());  
        listView.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                    int visibleItemCount, int totalItemCount) {
               // galleryadapter.ISLOADPIC = false;
                pager.setCurrentItem(firstVisibleItem, true);
                
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int first = view.getFirstVisiblePosition();
              
               // galleryadapter.ISLOADPIC = true;
                
                if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
                    //pager.setCurrentItem(first, true);
                   // galleryadapter.notifyDataSetChanged();
                 
                }
                
            }});
        
        
         
    }
   
  
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
         
        switch(item.getItemId()) {
        case 1:
            
            mMode = startActionMode(new AnActionModeOfEpicProportions(this));
            handler.postDelayed(new Runnable() {
                
                @Override
                public void run() {
                    View closeButton = findViewById(R.id.abs__action_mode_close_button);
                    if (closeButton != null) {
                     //   closeButton.setEnabled(false);
                        closeButton.setVisibility(View.GONE);
                    }
                    
                }
            } , 100);
            
            ((MatchListViewAdapter) listView.getAdapter()).selectAll( true);
            if( ((MatchListViewAdapter) listView.getAdapter()).getSelectedCount() == 0)
                mMode.finish();
            mMode.setTitle( ((MatchListViewAdapter) listView.getAdapter()).getSelectedCount()+" 已选择");
            break;
        case 2:
            listAdapter.toAllMatch();
            break;
        case 3:
            listAdapter.toAfter2DaysMatch();
             break;
        case 4:
            listAdapter.toThisWeekMatch();
             break;
        case 5:
            listAdapter.toNextWeekMatch();
             break;
        case 6:
            listAdapter.toTodayMatch();
            break;
       
        
        }
        if(item.getGroupId() == 1) {
            getSupportActionBar().setTitle(item.getTitle()+getString(R.string.push_match) );
            galleryadapter.notifyDataSetChanged();
            pager.setCurrentItem(0, false);
        }
        
        return false;
        
    }
    
  
    
 
 
    class TouhListener implements OnTouchListener{  
        @Override  
        public boolean onTouch(View v, MotionEvent event) {  
            return mGestureDetector.onTouchEvent(event);  
        }  
          
    }
    class GestureListener implements OnGestureListener{
        private int verticalMinDistance = 60;
        private int minVelocity         = 0;
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                float velocityY) {
            if (e1.getX() - e2.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity) {
//                Intent nextIntent = new Intent();
//                nextIntent.setClass(getApplicationContext(),  FollowActivity.class);
//                startActivity(nextIntent);    
 
//                overridePendingTransition(  R.anim.infromright,R.anim.out2left);  
            }
            else if (e2.getX() - e1.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity) {
                Intent nextIntent = new Intent();
                nextIntent.setClass(getApplicationContext(),  FollowActivity.class);
                startActivity(nextIntent);   
             //   overridePendingTransition(  R.anim.infromright,R.anim.out2left);  
                overridePendingTransition(R.anim.infromleft,R.anim.out2right); 
 
            }
            return false;
             
        }

        @Override
        public void onLongPress(MotionEvent e) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                float distanceX, float distanceY) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            // TODO Auto-generated method stub
            return false;
        }  
        
    }
    
    private final class AnActionModeOfEpicProportions implements ActionMode.Callback {
        
        private MatchActivity activity;

        AnActionModeOfEpicProportions(MatchActivity  activity) {
            this.activity = activity;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MODE_NOW = true;

//            menu.add(0,1,0,"Follow")
//            .setIcon(R.drawable.rating_important)
//            .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
           
           
   
            menu.add(0,2,0,"Alarm")
            .setIcon(R.drawable.device_access_add_alarm)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
             
             menu.add(0,3,0,"Del")
             .setIcon(R.drawable.content_discard)
             .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
          

         

            return true;
        }
     

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
             
             
             
            switch(item.getItemId()) {
                case 1:
                    break;
                case 2:
                    ((MatchListViewAdapter) listView.getAdapter()).setAllSelectedNotice(true);
                     
                    break;
                case 3:
                    ((MatchListViewAdapter) listView.getAdapter()).setAllSelectedNotice(false);
                    
                    break;
                
            }
            
            mode.finish();
            
            
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            
                      
           
            MODE_NOW = false;
            ((MatchListViewAdapter) listView.getAdapter()).endActionMode();
        }
    }    
    
     
}

class MatchGalleryAdapter extends PagerAdapter {
    public static boolean ISLOADPIC = true; 
    HashMap<String, SoftReference<Drawable>> imageCache;
    private final LayoutInflater inflater;
    public static int[] homeAd = new int[]{R.drawable.match_0,R.drawable.match_0 };
    MatchListViewAdapter listAdapter;
    Context context;
    public MatchGalleryAdapter(Context context,MatchListViewAdapter listAdapter, HashMap<String, SoftReference<Drawable>> imageCache){
        inflater = LayoutInflater.from(context);
        this.listAdapter = listAdapter;
        this.context = context;
        this.imageCache = imageCache;
    }
    
    public int getCount() {
        return Integer.MAX_VALUE;   
    }

    public boolean isViewFromObject(View view, Object o) {
        return view==o;   
    }
    
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v = inflater.inflate(R.layout.rolling_image,null);
        ImageView imageView = (ImageView) v.findViewById(R.id.imageid);
         
        TextView textView = (TextView)v.findViewById(R.id.textView1);
        if(listAdapter.getCount()!=0) {
        int  _position =position%listAdapter.getCount() ;
        
           
        Match match = listAdapter.getItem(_position);    
        
        //imageView.setImageResource(homeAd[position%homeAd.length]);
        if(ISLOADPIC) {
        if(this.imageCache.get("match_"+match.getId()) != null) {
            if (this.imageCache.get("match_"+match.getId()).get() != null)
                imageView.setImageDrawable(this.imageCache.get("match_"+match.getId()).get());
            else
                setImage(imageView, match);
        }else {
            setImage(imageView, match);
          /*  if(ResTools.getDrawable("match_"+match.getId(),context) != 0) {
                imageView.setImageResource(ResTools.getDrawable("match_"+match.getId(),context));
                imageCache.put("match_"+match.getId(), new  SoftReference<Drawable>(imageView.getDrawable()) );
            }
            else
                imageView.setImageResource(R.drawable.match_0);*/
        }
        }
        if(match.getDesc() != null)
            textView.setText(match.getDesc());
        else if( ResTools.getString("desc_" +match.getId(), context) != null) {
            textView.setText(ResTools.getString("desc_" +match.getId(), context));
        }else
            textView.setText(match.getTeamA().getTeamName()+ " VS "+ match.getTeamB().getTeamName());
        }
        else {
            textView.setText(R.string.app_name);
            imageView.setImageResource(R.drawable.match_0);
        }
        container.addView(v,0);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        (container).removeView((View) object);

    }
    
    private void setImage(ImageView imageView, Match match) {
        if(ResTools.getDrawable("match_"+match.getId(),context) != 0) {
            imageView.setImageResource(ResTools.getDrawable("match_"+match.getId(),context));
            imageCache.put("match_"+match.getId(), new  SoftReference<Drawable>(imageView.getDrawable()) );
        }
        else
            imageView.setImageResource(R.drawable.match_0);
    }
    
  
     
   
}

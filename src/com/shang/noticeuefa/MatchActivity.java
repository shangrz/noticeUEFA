package com.shang.noticeuefa;
 

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.actionbarsherlock.app.SherlockActivity;
   
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
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
import com.srz.androidtools.util.TimeTools;
 
 
 
import android.R.bool;
import android.R.integer;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
 

 
 

public class MatchActivity extends SherlockActivity   {
    
    
    ActionMode mMode;
    GestureDetector mGestureDetector;    
    Context mContext;
    private ViewPager pager;
    private Handler handler = new Handler();
    private MatchGalleryAdapter galleryadapter;
    private int index = 0;
    private ListView listView;
    public static boolean MODE_NOW =false;
    Tour tour ;
    private MatchListViewAdapter listAdapter;
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
        
         
        
        pager = (ViewPager) findViewById(R.id.image_gallery);
        galleryadapter = new MatchGalleryAdapter(this);
        pager.setAdapter(galleryadapter);
        pager.setCurrentItem(0);
       
        this.setTitle(R.string.matchname);
        listView = (ListView) findViewById(R.id.listView1); 
    //    tour =Tour.creatFromTagName("euro", getApplicationContext());
        
         
      //  listView.setAdapter(new MatchListViewAdapter(MatchActivity.this,getTodayMatch(),getHelper()));
        listAdapter = new MatchListViewAdapter(getApplicationContext(),getHelper());
        listAdapter.toTodayMatch();
        listView.setAdapter(listAdapter );
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(MODE_NOW == false)  
                   mMode = startActionMode(new AnActionModeOfEpicProportions());
                   
                ((MatchListViewAdapter) listView.getAdapter()).changeState(i, true);
                if(((MatchListViewAdapter) listView.getAdapter()).getSelectedCount() == 0)
                    mMode.finish();
                mMode.setTitle(((MatchListViewAdapter) listView.getAdapter()).getSelectedCount()+" 已选择");
                
                
              //     view.setBackgroundResource(R.color.red2);
             //   ((ViewHolder)view.getTag()).checkBox.setChecked( !((ViewHolder)view.getTag()).checkBox.isChecked());
             //   ((ViewHolder)view.getTag()).checkBox.toggle();
            }
        });
        
        
        
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
   
        

    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getGroupId() == 1)
            getSupportActionBar().setTitle(item.getTitle()+getString(R.string.push_match) );
        switch(item.getItemId()) {
        case 1:
            
            mMode = startActionMode(new AnActionModeOfEpicProportions());
            
            ((MatchListViewAdapter) listView.getAdapter()).selectAll( true);
            if( ((MatchListViewAdapter) listView.getAdapter()).getSelectedCount() == 0)
                mMode.finish();
            mMode.setTitle( ((MatchListViewAdapter) listView.getAdapter()).getSelectedCount()+" 已选择");
            break;
        case 2:
            listAdapter.toAllMatch();
          //  listView.setAdapter(new MatchListViewAdapter(getApplicationContext(), getAllMatch(),getHelper()));
            break;
        case 3:
         //   listView.setAdapter(new MatchListViewAdapter(getApplicationContext(), getAfter2DaysMatch(),getHelper())); 
            listAdapter.toAfter2DaysMatch();
             break;
        case 4:
          //   listView.setAdapter(new MatchListViewAdapter(getApplicationContext(), getThisWeekMatch(),getHelper())); 
            listAdapter.toThisWeekMatch();
             break;
        case 5:
          //   listView.setAdapter(new MatchListViewAdapter(getApplicationContext(), getNextWeekMatch(),getHelper())); 
            listAdapter.toNextWeekMatch();
             break;
        case 6:
            listAdapter.toTodayMatch();
         //   listView.setAdapter(new MatchListViewAdapter(getApplicationContext(), getTodayMatch(),getHelper())); 
            break;
       
        
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
            Toast.makeText(getApplicationContext(), "" +  item.getItemId() , Toast.LENGTH_SHORT).show();
            
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

    private final LayoutInflater inflater;
    public static int[] homeAd = new int[]{R.drawable.match_0,R.drawable.match_0 };

    public MatchGalleryAdapter(Context context){
        inflater = LayoutInflater.from(context);
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
        imageView.setImageResource(homeAd[position%homeAd.length]);
        container.addView(v,0);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        (container).removeView((View) object);

    }
    
  
     
    
   
}

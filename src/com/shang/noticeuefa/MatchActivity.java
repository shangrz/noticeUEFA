package com.shang.noticeuefa;
 
/**
 * @author shang
 *
 */
import java.lang.ref.SoftReference;
import java.util.HashMap;


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
import com.shang.noticeuefa.util.MyGestureListener;
import com.shang.noticeuefa.weibo.SinaTrendActivity;
import com.srz.androidtools.util.PreferenceUtil;
import com.srz.androidtools.util.ResTools;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
 

 
 

 
public class MatchActivity extends SherlockActivity   {
    
    private int listview_click_position;
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
        
 
         menu.add(0,1,0,"isFollow")
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
    boolean listInScorll = false;
    boolean pageInScorll = false;
   
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_MyStyle); //Used for theme switching in samples
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match);
        PreferenceUtil.setFirstTimeBoot(getApplicationContext(), false);
       // this.getWindow().setBackgroundDrawableResource(R.color.red);
        getSupportActionBar().setTitle(R.string.today_push_match);
        handler = new Handler();
        imageCache = new  HashMap<String, SoftReference<Drawable>>(); 
        this.setTitle(R.string.matchname);
        listView = (ListView) findViewById(R.id.listView1); 
    //    tour =Tour.creatFromTagName("euro", getApplicationContext());
         
         
      //  listView.setAdapter(new MatchListViewAdapter(MatchActivity.this,getTodayMatch(),getHelper()));
        listAdapter = new MatchListViewAdapter(getApplicationContext(),this,getHelper());
        listAdapter.toAllMatch(); 
        listAdapter.NEEDQUERYFOLLOW =false;
        getSupportActionBar().setTitle( getString(listAdapter.NEEDQUERYFOLLOW?R.string.push_match:R.string.all_match) );
     //   lastOptionsItemSelected =6;
        listView.setAdapter(listAdapter );
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(MODE_NOW == false)  {
                   mMode = startActionMode(new AnActionModeOfEpicProportions(MatchActivity.this));
                }
                
                
                listview_click_position = i;
                pager.setCurrentItem(i, true);
                boolean  startOrStop= ((MatchListViewAdapter) listView.getAdapter()).changeState(i, true);
                if(startOrStop) {
                     
                        
                         
                        listAdapter.startAnim(view,i);
                }else {
                    listAdapter.stopAnim(i);
                }
                 
                if(((MatchListViewAdapter) listView.getAdapter()).getSelectedCount() == 0) {
                    mMode.finish();
                } 
                if(listView.getAdapter()!=null)
                    mMode.setTitle(((MatchListViewAdapter) listView.getAdapter()).getSelectedCount()+" 已选择");
                
                 
                //((mMode.getCustomView()).findViewById(R.layout.abs__action_mode_close_item)).
              //  findViewById(R.id.abs__action_mode_close_button).setVisibility(View.INVISIBLE);
                
                
              //     view.setBackgroundResource(R.color.red2);
             //   ((ViewHolder)view.getTag()).checkBox.setChecked( !((ViewHolder)view.getTag()).checkBox.isChecked());
             //   ((ViewHolder)view.getTag()).checkBox.toggle();
            }
        });
        
        pager = (ViewPager) findViewById(R.id.image_gallery);
        galleryadapter = new MatchGalleryAdapter(this,listAdapter ,imageCache,handler);
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
                pageInScorll = true;
                 
            }

            @Override
            public void onPageSelected(int i) {
                index = i;
                System.out.println(":: "+i);   
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                System.out.println("::xx  "+i);
                if(i==0 && listInScorll == false) {
                    pageInScorll = true; 
                    listView.smoothScrollToPosition(index);
                     
                }
                if(i==0)
                    listInScorll = false;
                //pageInScorll = false;
            }
        });
 
        listView.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                    int visibleItemCount, int totalItemCount) {
             //   galleryadapter.setISLOADPIC(false);
            //    pager.setCurrentItem(firstVisibleItem, true);
                  listInScorll = true;
                
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int first = view.getFirstVisiblePosition();
                
              
                
                if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
                    galleryadapter.setISLOADPIC(true) ;
                    
                    if(!pageInScorll ) {
                        listInScorll = true;
                        pager.setCurrentItem(first, true);
                    }
                    
                    
                    pageInScorll = false;
                 //   pager.findViewById(R.id.imageid).setVisibility(View.VISIBLE); 
                   // galleryadapter.notifyDataSetChanged();
                 
                }
                
            }});
    }
   
    //private int lastOptionsItemSelected =0;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
        case 1:
            /*
            mMode = startActionMode(new AnActionModeOfEpicProportions(this));  
            ((MatchListViewAdapter) listView.getAdapter()).selectAll( true);
            if( ((MatchListViewAdapter) listView.getAdapter()).getSelectedCount() == 0)
                mMode.finish();
            mMode.setTitle( ((MatchListViewAdapter) listView.getAdapter()).getSelectedCount()+" 已选择");*/
            //listAdapter.NEEDQUERYFOLLOW = !listAdapter.NEEDQUERYFOLLOW;
            //listAdapter.doWhenChangeIsFollow();
/*            if(lastOptionsItemSelected == 2) 
                listAdapter.toAllMatch();
            else if(lastOptionsItemSelected==3)
                listAdapter.toAfter2DaysMatch();
            else if(lastOptionsItemSelected==4)
                listAdapter.toThisWeekMatch();
            else if(lastOptionsItemSelected==5)
                listAdapter.toNextWeekMatch();
            else if(lastOptionsItemSelected==6)
                listAdapter.toTodayMatch();*/
            
            
            
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
            getSupportActionBar().setTitle(item.getTitle()+getString(listAdapter.NEEDQUERYFOLLOW?R.string.push_match:R.string.all_match) );
        }
        if(item.getItemId()==1) {
            String s= getSupportActionBar().getTitle().subSequence(0, getSupportActionBar().getTitle().length()-getString(R.string.push_match).length()).toString();
            getSupportActionBar().setTitle(s+getString(listAdapter.NEEDQUERYFOLLOW?R.string.push_match:R.string.all_match));
            }
        String _tString =getSupportActionBar().getTitle().toString();
        if((_tString.startsWith(getString(R.string.all)+getString(R.string.all))))
            getSupportActionBar().setTitle(_tString.substring(getString(R.string.all).length()));
        
        galleryadapter.notifyDataSetChanged();
        pager.setCurrentItem(0, false);
        return false;
        
    }
    
 
/*    class TouhListener implements OnTouchListener{  
        @Override  
        public boolean onTouch(View v, MotionEvent event) {  
            return mGestureDetector.onTouchEvent(event);  
        }  
          
    }*/
   
    @Override  
    public boolean dispatchTouchEvent(MotionEvent event) {  
        System.out.println("note!!!12");
        if (listAdapter != null) {
            if (listAdapter.gestureDetector != null) { 
               
                    if (listAdapter.gestureDetector.onTouchEvent(event)) { 
                        event.setAction(MotionEvent.ACTION_CANCEL); 
                    }
                  
            }
     
        }  
         
        return super.dispatchTouchEvent(event);  
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


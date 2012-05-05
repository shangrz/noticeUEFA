package com.shang.noticeuefa;
 

import com.actionbarsherlock.app.SherlockActivity;
   
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.shang.noticeuefa.model.Tour;
 
 
 
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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
    private MatchGalleryAdapter adapter;
    private int index = 0;
    private ListView listView;
    public static boolean MODE_NOW =false;
    Tour tour ;
    MatchListViewAdapter adapter2;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
 
         menu.add(0,1,0,"SelectAll")
         .setIcon(  R.drawable.content_select_all)
         .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT); 
         
         
         SubMenu subMenu1 = menu.addSubMenu("Collection");
         subMenu1.add(R.string.todayandafter);
         subMenu1.add(R.string.thisweek);
         subMenu1.add(R.string.nextweek);
          
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
    
    
 
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_MyStyle); //Used for theme switching in samples
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match);
       // this.getWindow().setBackgroundDrawableResource(R.color.red);
        getSupportActionBar().setTitle(R.string.today_push_match);
        
        
        
        pager = (ViewPager) findViewById(R.id.image_gallery);
        adapter = new MatchGalleryAdapter(this);
        pager.setAdapter(adapter);
        pager.setCurrentItem(0);
       
        this.setTitle(R.string.matchname);
        listView = (ListView) findViewById(R.id.listView1); 
        tour =Tour.creatFromTagName("euro", getApplicationContext());
        adapter2 = new MatchListViewAdapter(MatchActivity.this, tour); 
        listView.setAdapter(adapter2);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(MODE_NOW == false)  
                   mMode = startActionMode(new AnActionModeOfEpicProportions());
                   
                adapter2.changeState(i, true);
                if(adapter2.getSelectedCount() == 0)
                    mMode.finish();
                mMode.setTitle(adapter2.getSelectedCount()+" 已选择");
                
                
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
        
       mGestureDetector = new GestureDetector(new GestureListener());  
       listView.setOnTouchListener(new TouhListener());  
   
        

    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
        switch(item.getItemId()) {
        case 1:
            
            mMode = startActionMode(new AnActionModeOfEpicProportions());
            
            adapter2.selectAll( true);
            if(adapter2.getSelectedCount() == 0)
                mMode.finish();
            mMode.setTitle(adapter2.getSelectedCount()+" 已选择");
            break;
        case 2:
           
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
                    adapter2.setAllSelectedNotice(true);
                    break;
                case 3:
                    adapter2.setAllSelectedNotice(false);
                    break;
                
            }
            
            mode.finish();
            
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            MODE_NOW = false;
            adapter2.endActionMode();
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

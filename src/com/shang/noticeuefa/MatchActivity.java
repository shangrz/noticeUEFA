package com.shang.noticeuefa;
 

import com.actionbarsherlock.app.SherlockActivity;
  
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.shang.noticeuefa.model.Tour;
 
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

 
 

public class MatchActivity extends SherlockActivity{
    private ViewPager pager;
    private Handler handler = new Handler();
    private MatchGalleryAdapter adapter;
    private int index = 0;
    private ListView listView;
    
    Tour tour ;
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
 
         menu.add("SelectAll")
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
        MatchListViewAdapter adapter2 = new MatchListViewAdapter(MatchActivity.this, tour); 
        listView.setAdapter(adapter2);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
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

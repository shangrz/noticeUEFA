package com.shang.noticeuefa.weibo;

 
import java.io.IOException;
 
import java.net.MalformedURLException;
 

 
import java.util.HashMap;
import java.util.List;

import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 
   

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.mobclick.android.MobclickAgent;
 
 
 
import com.shang.noticeuefa.FollowActivity;
import com.shang.noticeuefa.R;
 
import com.shang.noticeuefa.util.MyGestureListener;
import com.shang.noticeuefa.weibo.SinaTrendAdapter.ViewHolder;
import com.weibo.net.Weibo;
 
import com.weibo.net.ShareActivity;
import com.weibo.net.WeiboException;
import com.weibo.net.WeiboParameters;

 
 
 
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
 
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
 
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
 
import android.os.Bundle;
import android.os.Handler;
import android.os.Message; 
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log; 
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View; 
import android.view.View.OnTouchListener;
import android.widget.AbsListView; 
import android.widget.AdapterView;
 
import android.widget.Button;
import android.widget.ListView; 
import android.widget.Toast; 
import android.widget.AbsListView.OnScrollListener;

/**
 * @author shang
 *
 */
public class SinaTrendActivity extends SherlockActivity {
    
    private int match_id = 0;
    private long match_time = 0;
    private   static int PAGECOUNT = 20;
    private   static boolean notLoading = true;
    SharedPreferences sp;
    public static final String ID = "id";
    private boolean isBack = false; 
    private String thekeyword = "欧锦赛 足球";
    private String title = "欧锦赛";
    protected static final String REDUIRECT_URL = "http://www.sina.com";  
    protected static final String CONSUMER_KEY = "3616744610"; 
    protected static final String CONSUMER_SECRET = "e083f8556e3b1f080cb56166ec83fdbb"; 

    Weibo weibo = Weibo.getInstance(); 
    private int total_number; 
    private ListView list; 
    SinaTrendAdapter adapter; 
    private Handler messageHandler = new Handler();
    GestureDetector mGestureDetector;  

    static final int DIALOG_PROGRESS_BAR = 1; 
    static final int DIALOG_NETWORK_NOT_EXIST = 2;
    List<Map<String, Object>> listItems = new CopyOnWriteArrayList<Map<String, Object>>();

    protected int page = 1;

    public static boolean isNetworkAvailable(Activity mActivity) {
        Context context = mActivity.getApplicationContext();
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null)
            return false;
        else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                } 
        }
        return false;
    } 
 
    private boolean isNewKey = true;
    private boolean isLoaded = false;
    public  boolean getIsNewKey() {
        return isNewKey;
    }
    public void  setIsNewKey(boolean isNewKey) {
        this.isNewKey =isNewKey;
    }
    
    private boolean islogin = false;
    class AuthHandler extends Handler{
        public void handleMessage(Message msg) {  
            if (msg.what == 1 && isLoaded == false) {
              //  sp.edit().putBoolean(ConfigActivity.WEIBOBINDOFF, true).commit(); 
                findViews();
                isLoaded = true;
                setIsNewKey(isLoaded); 
            }
            
            islogin = true;
            invalidateOptionsMenu();
           
 
 
        }
    }
   
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        return super.onPrepareOptionsMenu(menu);
    }
 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
         if(!islogin)
             menu.add(0,1,0,"登陆") .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
         else
             menu.add(0,2,0,"发布") .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
         
         /*SubMenu subMenu1 = menu.addSubMenu("Collection");
         subMenu1.add(1,6,1,R.string.today);
          
         subMenu1.add(1,3,2,R.string.todayandafter);
          
         subMenu1.add(1,4,3,R.string.thisweek);
         subMenu1.add(1,5,4,R.string.nextweek);
         subMenu1.add(1,2,5,R.string.all);
          
         MenuItem subMenu1Item = subMenu1.getItem();
         subMenu1Item.setIcon(R.drawable.collections_go_to_today);
         subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);*/
      
   

        return true;
    }
    
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
         
        switch(item.getItemId()) {
        case 1:
            weibo.authorize(SinaTrendActivity.this, new AuthDialogListener(new AuthHandler(), getBaseContext(), CONSUMER_SECRET));
            break;
        case 2:
            try {
                weibo.share2weibo(SinaTrendActivity.this,  weibo.getAccessToken().getToken(), weibo
                        .getAccessToken().getSecret(), "#"+thekeyword+"#", null);
            } catch (WeiboException e1) {
                Toast.makeText(getApplicationContext(), "登陆错误", Toast.LENGTH_SHORT);
                e1.printStackTrace();
            }catch (Exception e2){
                e2.printStackTrace();
            }
            break;
       
        
        }
        
        
        return false;
        
    }

    public void onCreate(Bundle savedInstanceState) {
     
        
        setTheme(R.style.Theme_MyStyle); 
        super.onCreate(savedInstanceState);
        MobclickAgent.onError(this);
        setContentView(R.layout.sinatrend);
 
        
        
        
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
             
                match_id = bundle.getInt("MATCH_ID");
                match_time = bundle.getLong("MATCH_TIME");
                title = bundle.getString("TITLE");
                thekeyword = bundle.getString("THEKEYWORD");
                
        }
     
        Log.d("sinatenderactivity", "creat over!!!!!!!");
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        
        if(match_id != 0) {
            if(match_time!=0) {
                 if(System.currentTimeMillis()>match_time+1000*120*60) {
                    NotificationManager nm = (NotificationManager)getSystemService("notification");
                    nm.cancel(match_id);
                 }
            }
            
        }
        
        
        getSupportActionBar().setTitle(title); 
        
        if(isBack) 
            return; 
        if (!isNetworkAvailable(this)) {
            showDialog(DIALOG_NETWORK_NOT_EXIST); 
            finish();
        } else {
            weibo.setupConsumerConfig(CONSUMER_KEY, CONSUMER_SECRET);
            weibo.setRedirectUrl(REDUIRECT_URL);
             
//            bar.setButton(R.string.login, NavigationBar.Type.TEXT, NavigationBar.Position.RIGHT, new View.OnClickListener() {
//                @Override
//                public void onClick(View view) { 
//                    weibo.authorize(SinaTrendActivity.this, new AuthDialogListener(new AuthHandler(), getBaseContext(), CONSUMER_SECRET));
//                }
//            });  
             
            weibo.authorize(SinaTrendActivity.this, new AuthDialogListener(new AuthHandler(), getBaseContext(), CONSUMER_SECRET));
        }
    }
 

    private void findViews() {  
         
        
        list = (ListView) findViewById(R.id.ListView_trends);
        MyGestureListener myGestureListener = new MyGestureListener() {
            @Override
            public void runWhenToRight() {
            }

            @Override
            public void runWhenToLeft() {
                    finish();
            }

            @Override
            public void doSomething() {
                // TODO Auto-generated method stub
                
            }
            
        };
        myGestureListener.set(SinaTrendActivity.this,150,0);
        mGestureDetector = new GestureDetector(myGestureListener);  
        list.setOnTouchListener(new TouhListener());  
        //listItem.clear();
        page = 1; 
        adapter = new SinaTrendAdapter(SinaTrendActivity.this, listItems, list ); 
        list.setAdapter(adapter);
        Log.d("tag", "will load ");
        showDialog(DIALOG_PROGRESS_BAR);
        load(thekeyword, page, PAGECOUNT); 
        list.setOnScrollListener(new OnScrollListener() {

            boolean fireLoad = false;
     
            public void onScroll(AbsListView view, int firstVisibleItem,
                    int visibleItemCount, int totalItemCount) {
                
                if (firstVisibleItem + visibleItemCount >= totalItemCount
                        && totalItemCount < total_number && totalItemCount != 0) {
                    fireLoad = true;
                } else {
                    fireLoad = false;
                } 
              
                if (firstVisibleItem + visibleItemCount >= (totalItemCount - PAGECOUNT/2)
                        && totalItemCount < total_number && totalItemCount != 0) {
                    if (notLoading) {
                        notLoading = false;
                        page += 1;
                        Log.d("load in onScroll",thekeyword +" page :"+ String.valueOf(page) + " PAGECOUNT"+String.valueOf(PAGECOUNT) );
                        load(thekeyword, page,PAGECOUNT); 
 
                    }
                }
            }
            
  
            
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Log.d("scrollState",String.valueOf(scrollState));
                int first = view.getFirstVisiblePosition();
                int count = view.getChildCount(); 
                if (scrollState == OnScrollListener.SCROLL_STATE_IDLE ||(first + count >= adapter.getCount())) {
                    if (fireLoad && notLoading) {
                        notLoading = false;
                        page += 1;
                        Log.d("load in onScrollStateChanged SCROLL_STATE_IDLE",thekeyword +" page :"+ String.valueOf(page) + " PAGECOUNT"+String.valueOf(PAGECOUNT) );
                        load(thekeyword, page,PAGECOUNT); 
//                        messageHandler.post(new Runnable() {
//                            
//                            public void run() {
//                                load(thekeyword, page); 
//                              notLoading = true;  
//                              fireLoad = false;
//                            }
//                        });
                    }
                }
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                    long id) {
                long weiboId =(Long) listItems.get(position).get("id") ;
                try {
                    weibo.repost2weibo(SinaTrendActivity.this, Weibo
                            .getInstance().getAccessToken().getToken(),
                            Weibo.getInstance().getAccessToken()
                                    .getSecret(), "#" + thekeyword + "#",
                            weiboId);
                } catch (WeiboException e) {
                
                    e.printStackTrace();
                }
               
                
            }
            
        });
    }
    
    private AlertDialog dialog;
    protected Dialog onCreateDialog(int id) {

         if (id == DIALOG_PROGRESS_BAR) {
            dialog = new ProgressDialog(this);
            dialog.setMessage(this.getString(R.string.loading));
            ((ProgressDialog) dialog).setIndeterminate(true);
            dialog.setCancelable(true);
            return dialog;
        } 
         
         if (id == DIALOG_NETWORK_NOT_EXIST) {
             Toast.makeText(this, "没有联网", 2000) .show();  
         }
        return super.onCreateDialog(id);
        }
    
    public void load(final String keyword, final int page, final int pagecount ) { 
        
         Thread loadThread = new Thread( ) {
             public void run(){ 
                 
                notLoading = false;
                int count = pagecount;
                // int page = 1;
                String trendString;
                try {
                    Log.d("keyword", keyword);
                    trendString = getTrendsTimeline(weibo, keyword, count, page);
                   // trendString = getTopicsTimeline(weibo, keyword, count, page); 
  //                  JSONObject json = new JSONObject(trendString); 
  //                  JSONArray trendsaArray = json.getJSONArray("statuses");
                    Log.d("json",trendString);
                    JSONArray trendsaArray = new JSONArray(trendString) ;
                    //total_number = json.getInt("total_number");
                    total_number = 500;
                    Log.d("total_number", String.valueOf(total_number)); 
                    if(trendsaArray.length() == 0)
                        total_number = 0;
                    for (int i = 0; i < trendsaArray.length(); i++) {
                        final Map<String, Object> map = new HashMap<String, Object>(); 
                        map.put("text",
                                trendsaArray.getJSONObject(i).get("text"));
                        map.put("screen_name", trendsaArray.getJSONObject(i)
                                .getJSONObject("user").getString("screen_name"));
                        map.put("created_at", trendsaArray.getJSONObject(i)
                                .get("created_at"));
                        map.put("profile_image_url", trendsaArray
                                .getJSONObject(i).getJSONObject("user")
                                .getString("profile_image_url"));
                        map.put("id", trendsaArray.getJSONObject(i).getLong("id"));
                        if (trendsaArray.getJSONObject(i).has("reposts_count"))
                            map.put("reposts_count", trendsaArray.getJSONObject(i).getInt("reposts_count"));
                        else
                            map.put("reposts_count",0);
                        if (trendsaArray.getJSONObject(i).has("comments_count"))
                            map.put("comments_count", trendsaArray.getJSONObject(i).getInt("comments_count"));
                        else
                            map.put("comments_count",0);
                         
                        if (trendsaArray.getJSONObject(i).has("bmiddle_pic") )
                            map.put("bmiddle_pic", trendsaArray.getJSONObject(i).getString("bmiddle_pic"));
                        listItems.add(map); 
                                          //   adapter.notifyDataSetChanged(); 
                        //handler.sendEmptyMessage(1); 
                    }
                    //handler.sendEmptyMessage(1); 
                } catch (MalformedURLException e) {
                  
                    e.printStackTrace();
                } catch (IOException e) {
                    
                    e.printStackTrace();
                } catch (WeiboException e) {
                    
                    e.printStackTrace();
                } catch (JSONException e) {
                    
                    e.printStackTrace();
                }finally {
                  //  adapter.notifyDataSetChanged();
                    runOnUiThread(new Runnable() { 
                        @Override
                        public void run() { 
                            
                            notLoading = true;
                            adapter.notifyDataSetChanged();
                            if(dialog != null)
                                dialog.dismiss();  
                        }
                    });
                 
                } 
             }
        
             
         };
         loadThread.start();
         
       

    }
    
   
 
    private   String PROVINCES = "1000";
    
    private String getTrendsTimeline2(Weibo weibo, String trend, int count,
            int page) throws MalformedURLException, IOException, WeiboException {
        String url = Weibo.SERVER + "trends/statuses.json"; 
        WeiboParameters bundle = new WeiboParameters(); 
        
        //bundle.add("source", CONSUMER_KEY);
        //bundle.add("access_token", weibo.getAccessToken().getToken());
        
      //  bundle.add("provinces", PROVINCES);
        bundle.add("trend", trend);
        bundle.add("count", String.valueOf(count));
        bundle.add("page", String.valueOf(page));

        String rlt = weibo.request(this, url, bundle, "GET",
                weibo.getAccessToken());
        return rlt;
    }
    
    private String getTrendsTimeline(Weibo weibo, String trend, int count,
            int page) throws MalformedURLException, IOException, WeiboException {
         
        String url = "https://api.t.sina.com.cn/trends/statuses.json"; 
        WeiboParameters bundle = new WeiboParameters();
        
        bundle.add("source", CONSUMER_KEY); 
        bundle.add("trend_name", trend);
        bundle.add("count", String.valueOf(count));
        bundle.add("page", String.valueOf(page));

        String rlt = weibo.request(this, url, bundle, "GET",
                weibo.getAccessToken());
        return rlt;
    }
    
    private String getTopicsTimeline(Weibo weibo, String trend, int count,
            int page) throws MalformedURLException, IOException, WeiboException {
        String url = Weibo.SERVER + "search/topics.json"; 
        WeiboParameters bundle = new WeiboParameters();
 
        bundle.add("q", trend);
        bundle.add("count", String.valueOf(count));
        bundle.add("page", String.valueOf(page));

        String rlt = weibo.request(this, url, bundle, "POST",
                weibo.getAccessToken());
        return rlt;
    }
    
    
    
    
    
    
 
  
    
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
//        if(SinaShareActivity.RESPONSE != null) {
//            Log.d("RESPONSE",SinaShareActivity.RESPONSE);
//            try {
//                JSONObject json =new JSONObject(SinaShareActivity.RESPONSE);
//                Map<String, Object> map = makeMapfromJson(json); 
//                listItems.add(0, map );  
//                adapter.notifyDataSetChanged();
//                 
//            } catch (JSONException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            SinaShareActivity.RESPONSE =null;
//            }
        }
    
    private Map<String, Object> makeMapfromJson(JSONObject json) throws JSONException    {
        Map<String, Object> map = new HashMap<String, Object>(); 
        map.put("text", json.get("text"));
        map.put("screen_name", json.getJSONObject("user").getString("screen_name"));
        map.put("created_at", json
                .get("created_at"));
        map.put("profile_image_url", json.getJSONObject("user")
                .getString("profile_image_url"));
        map.put("id", json.getLong("id"));
        if (json.has("reposts_count"))
            map.put("reposts_count", json.getInt("reposts_count"));
        else
            map.put("reposts_count",0);
        if (json.has("comments_count"))
            map.put("comments_count", json.getInt("comments_count"));
        else
            map.put("comments_count",0);
        if (json.has("bmiddle_pic") )
            map.put("bmiddle_pic", json.getString("bmiddle_pic"));
        return map;
    }
    
    
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        }
    
    @Override
    protected void onDestroy() {
        System.out.println("onDestroy");
        if (adapter != null)
            adapter.clearImageCache();
        super.onDestroy();
    }
  
    class TouhListener implements OnTouchListener{  
        @Override  
        public boolean onTouch(View v, MotionEvent event) {  
            return mGestureDetector.onTouchEvent(event);  
        }  
    }
    
    
 
}

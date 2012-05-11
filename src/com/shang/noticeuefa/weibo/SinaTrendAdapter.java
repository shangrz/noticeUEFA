package com.shang.noticeuefa.weibo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
 
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shang.noticeuefa.R;
import com.shang.noticeuefa.weibo.AsyncImageLoader.ImageCallback;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
 
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
  


class  AsyncImageLoader {  
    private File cacheDir = null;
  
  //  不使用软引用
  //  private  HashMap<String, SoftReference<Drawable>> imageCache;    
 
    private  HashMap<String,  Drawable > imageCache; 
    
        public  AsyncImageLoader() {    
        //  imageCache = new  HashMap<String, SoftReference<Drawable>>();   //  不使用软引用
          imageCache = new  HashMap<String,  Drawable >();   
          
        } 
         
        public  AsyncImageLoader(File cacheDir) {    
          //  imageCache = new  HashMap<String, SoftReference<Drawable>>();   //  不使用软引用
            imageCache = new  HashMap<String,  Drawable >();   
            this.cacheDir = cacheDir;
        }    
         
        public  Drawable loadDrawable( final  String imageUrl,  final  ImageCallback imageCallback) { 
            Log.d("AsyncImageLoader imageurlkey", imageUrl);
            if  (imageCache.containsKey(imageUrl)) {    
                Log.d("AsyncImageLoader containsKey imageurlkey", imageUrl);
                //SoftReference<Drawable> softReference = imageCache.get(imageUrl);     //  不使用软引用
               // Drawable drawable = softReference.get();     //  不使用软引用
                Drawable  drawable = imageCache.get(imageUrl);  
                if  (drawable !=  null ) {
                    Log.d("AsyncImageLoader containsKey drawableOK", imageUrl);
                    return  drawable;    
                }    
            }
            
            //文件缓存
            else  if(cacheDir != null)
            {  
                 
          
                String cachefilename = cacheDir.getAbsolutePath() + File.separator + imageUrlToFilename(imageUrl);  
                 
//                File[] cacheFiles = cacheDir.listFiles();  
                
                Log.d("#####to load",cachefilename);
 
                if(new File(cachefilename).exists())
                  
 
                {  
                    Log.d("#####load from successed",cachefilename);
                    return Drawable.createFromPath(cachefilename);
                     
                }  
            }  
            
            
            final  Handler handler =  new  Handler() {    
                public   void  handleMessage(Message message) {    
                    imageCallback.imageLoaded((Drawable) message.obj, imageUrl);    
                }    
            };    
            new  Thread() {    
                @Override     
                public   void  run() {    
                    Drawable drawable = loadImageFromUrl(imageUrl);    
                   // imageCache.put(imageUrl, new  SoftReference<Drawable>(drawable));    //  不使用软引用 
                    imageCache.put(imageUrl,  drawable );   
                    Message message = handler.obtainMessage(0 , drawable);    
                    handler.sendMessage(message);    
                    
                //    File dir = new File("/mnt/sdcard/test/");  
                     
                    if (cacheDir == null)
                        return;
                    if(!cacheDir.exists())
                        cacheDir.mkdirs();  
                    File bitmapFile = new File(cacheDir,
                            imageUrlToFilename(imageUrl));  
                    if(!bitmapFile.exists())  
                    {  
                        try  
                        {  
                            Log.d("write to",bitmapFile.getAbsolutePath());
                            bitmapFile.createNewFile();
                            FileOutputStream fos;
                            Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
                            fos = new FileOutputStream(bitmapFile);  
                            bitmap.compress(Bitmap.CompressFormat.JPEG,100, fos);  
                            fos.flush();
                            fos.close();  
                           
                        }  
                        catch (Exception e)  
                        {   
                            e.printStackTrace();  
                        } 
                        
                    }  
   
                }    
            }.start();    
            return   null ;    
        }    
         
       public static  Drawable loadImageFromUrl(String url) {    
           URL m;    
           InputStream i = null ;    
           try  {    
               Log.d("AsyncImageLoader loadImageFromUrl", url);
               m = new  URL(url);    
               i = (InputStream) m.getContent();    
           } catch  (MalformedURLException e1) {   
               Log.d("AsyncImageLoader loadImageFromUrl", "MalformedURLException111");
               e1.printStackTrace();    
           } catch  (IOException e) {    
               Log.d("AsyncImageLoader loadImageFromUrl", "IOException111");
               e.printStackTrace();    
           }    
           Drawable d = Drawable.createFromStream(i, "src" );    
           return  d;    
       }    
         
        public   interface  ImageCallback {    
            public   void  imageLoaded(Drawable imageDrawable, String imageUrl);    
        }
        
        public  void clearImageCache() {    
              if(imageCache != null)
                  imageCache.clear();
          }

       private String imageUrlToFilename(String imageUrl) {
            
          // imageUrl.substring(imageUrl.lastIndexOf("/") + 1)
            
           return URLEncoder.encode(imageUrl);
           
       }
   
}    
 
public class SinaTrendAdapter   extends ArrayAdapter< Map<String, Object> > {
    public static class ViewHolder  {
        public ImageView image; 
        public TextView tvname; 
        public TextView tvcomment; 
        public TextView tvdate;
    }
    
    
    private Handler handler;
    private ListView listView;
    private LayoutInflater mInflater;
    private  AsyncImageLoader asyncImageLoader;    
    List<Map<String, Object>> listItem ;
    private int imgViewSide = 48;
    
    public Handler getHandler() {
        return handler;
    }
    
    
   
    
    
    public void setHandler(Handler handler) {
        this.handler = handler;
    }
    
    public void clearImageCache() {
        if(asyncImageLoader != null)
            asyncImageLoader.clearImageCache();
    }
    
    public SinaTrendAdapter(Activity activity, List<Map<String, Object>> listItem, ListView listView) {
        super(activity, 0,listItem);
        this.mInflater = LayoutInflater.from(activity.getBaseContext());
        this.listItem =listItem;
        this.listView = listView;
        this.imgViewSide = activity.getWindowManager().getDefaultDisplay().getWidth()/10;
     
        asyncImageLoader = new  AsyncImageLoader(activity.getCacheDir());   
        //asyncImageLoader = new  AsyncImageLoader();    
    }
    
 
    
    
    public View getView(final int position, View convertView,
            ViewGroup parent) { 
        SimpleDateFormat outTimeformat = new SimpleDateFormat(
                "MM-dd  HH:mm");

        //final ViewHolder holder;
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.sinatrendview, null);

            holder.image = (ImageView) convertView
                    .findViewById(R.id.ImageView01);
            holder.image.setDrawingCacheEnabled(true);
            holder.tvname = (TextView) convertView
                    .findViewById(R.id.ItemName);
            holder.tvcomment = (TextView) convertView
                    .findViewById(R.id.ItemText);
            holder.tvdate = (TextView) convertView
                    .findViewById(R.id.ItemDate);

            convertView.setTag(holder);
 

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvname.setText((String) listItem.get(position).get(
                "screen_name"));
        holder.tvcomment.setText((String) listItem.get(position)
                .get("text"));

        long time = Date.parse(listItem.get(position).get("created_at")
                .toString());

        holder.tvdate.setText(outTimeformat.format(new Date(time)));
        String imageUrlString =  listItem.get(position).get("profile_image_url").toString() ;
       // URL url = new URL(imageUrlString);
        
        holder.image.setTag(imageUrlString );
        Drawable cachedImage = asyncImageLoader.loadDrawable(imageUrlString, new  ImageCallback() {    
            public   void  imageLoaded(Drawable imageDrawable, String imageUrl) {    
                ImageView imageViewByTag = (ImageView) listView.findViewWithTag(imageUrl);    
                if  (imageViewByTag !=  null ) {    
                    imageViewByTag.setImageDrawable(imageDrawable);    
                }    
            }    
        });    
        LayoutParams para;
        para = holder.image.getLayoutParams();
        para.height = this.imgViewSide;
        para.width = this.imgViewSide;
        holder.image.setLayoutParams(para);
        holder.image.setScaleType(ImageView.ScaleType.FIT_CENTER);
        if  (cachedImage ==  null ) {    
          //  holder.image.setImageResource(R.drawable.person);    
        }else {    
            holder.image.setImageDrawable(cachedImage);    
        }
        
          
        return convertView;
    }
   

   
}
  
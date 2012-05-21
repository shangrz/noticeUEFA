package com.shang.noticeuefa;

import android.content.Context;
import android.graphics.EmbossMaskFilter;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.actionbarsherlock.*;
import com.shang.noticeuefa.model2.Match;
import com.srz.androidtools.util.ResTools;

import java.lang.ref.SoftReference;
import java.util.HashMap;

class MatchGalleryAdapter extends PagerAdapter {
    public static boolean   ISLOADPIC = true;
    public boolean isISLOADPIC() {
        return ISLOADPIC;
    }

    public void setISLOADPIC(boolean iSLOADPIC) {
        ISLOADPIC = iSLOADPIC;
    }

    HashMap<String, SoftReference<Drawable>> imageCache;
    private final LayoutInflater inflater;
    public static int[] homeAd = new int[]{com.actionbarsherlock.R.drawable.match_0, com.actionbarsherlock.R.drawable.match_0 };
    MatchListViewAdapter listAdapter;
    Context context;
    Handler handler;
    public MatchGalleryAdapter(Context context,MatchListViewAdapter listAdapter, HashMap<String, SoftReference<Drawable>> imageCache ,Handler handler){
        inflater = LayoutInflater.from(context);
        this.listAdapter = listAdapter;
        this.context = context;
        this.imageCache = imageCache;
        this.handler = handler;
    }

    public int getCount() {
        return Integer.MAX_VALUE;
    }

    public boolean isViewFromObject(View view, Object o) {
        return view==o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v = inflater.inflate(com.actionbarsherlock.R.layout.rolling_image,null);
        final ImageView imageView = (ImageView) v.findViewById(com.actionbarsherlock.R.id.imageid);

        TextView textView = (TextView)v.findViewById(com.actionbarsherlock.R.id.textView1);
        
        if(listAdapter.getCount()!=0) {
            int  _position =position%listAdapter.getCount() ;


            final Match match = listAdapter.getItem(_position);

            //imageView.setImageResource(homeAd[position%homeAd.length]);

            if (imageCache.get("match_" + match.getId()) != null) {
                if (imageCache.get("match_" + match.getId()).get() != null)

                    imageView.setImageDrawable(imageCache.get(
                            "match_" + match.getId()).get());

                else

                    setImage(imageView, match);

            } else {
                setImage(imageView, match);

            }

            if(match.getDesc() != null)
                textView.setText(match.getDesc());
            else if( ResTools.getString("desc_" +match.getId(), context) != null) {
                textView.setText(ResTools.getString("desc_" +match.getId(), context));
            }else
                textView.setText(match.getTeamA().getTeamName()+ " VS "+ match.getTeamB().getTeamName());
        }
        else {
            textView.setText(com.actionbarsherlock.R.string.app_name);
            imageView.setImageResource(com.actionbarsherlock.R.drawable.match_0);
        }
        
        
        //textSpecial(textView);
        container.addView(v,0);
        return v;
    }
    
    
    private void textSpecial(TextView textView){
        TextPaint tp2 = textView.getPaint(); 
        
        tp2.setFakeBoldText(true);
        //设置光源的方向
        float[] direction = new float[]{ 1, 1, 1 };
        //设置环境光亮度
        float light = 0.4f;
        //选择要应用的反射等级
        float specular = 6;
        //向mask应用一定级别的模糊
        float blur = 3.5f;
        EmbossMaskFilter maskfilter=new EmbossMaskFilter(direction,light,specular,blur);
        tp2.setMaskFilter(maskfilter);
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
            imageView.setImageResource(com.actionbarsherlock.R.drawable.match_0);
    }


}
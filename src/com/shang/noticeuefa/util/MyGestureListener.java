package com.shang.noticeuefa.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MotionEvent;

import com.actionbarsherlock.R;
import com.shang.noticeuefa.weibo.SinaTrendActivity;
import android.view.GestureDetector.OnGestureListener;

 
 
public  abstract class MyGestureListener implements OnGestureListener {
    public int verticalMinDistance = 60;
    public int minVelocity = 0 ;
    public Activity activity;
    
    
    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }
    
    public abstract void runWhenToRight();
    public abstract void runWhenToLeft();
    
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
            float velocityY) {
        if (e1.getX() - e2.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity) {
             
            runWhenToRight();

            if(activity != null)
            activity.overridePendingTransition(  R.anim.infromright,R.anim.out2left);  
//                activity.overridePendingTransition(  R.anim.infromright,R.anim.out2left);   
        }
        else if (e2.getX() - e1.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity) {
            runWhenToLeft();

//         //   overridePendingTransition(  R.anim.infromright,R.anim.out2left);  
             if(activity != null)
                 activity.overridePendingTransition(R.anim.infromleft,R.anim.out2right); 

        }
        return false;
         
    }

    @Override
    public void onLongPress(MotionEvent arg0) {
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

    public void set(Activity activity,int verticalMinDistance, int minVelocity) {
        this.activity = activity;
        this.verticalMinDistance = verticalMinDistance;
        this.minVelocity = minVelocity;
        
    }
}

package com.shang.noticeuefa.util;

import android.app.Activity;
import android.view.MotionEvent;

import com.actionbarsherlock.R;
import android.view.GestureDetector.OnGestureListener;

 
 
public  abstract class MyGestureListener implements OnGestureListener {
    public int verticalMinDistance = 60;
    public int minVelocity = 0 ;
    public Activity activity;
    
 
    
    public abstract void runWhenToRight();
    public abstract void runWhenToLeft();
    public abstract void doSomething();
    
    
    
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
            float velocityY) {
        System.out.println("MyGestureListener onFling");
        if (e1.getX() - e2.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity) {
            System.out.println("will runWhenToRight()");
            runWhenToRight();

            if(activity != null)
            activity.overridePendingTransition(  R.anim.infromright,R.anim.out2left);  
//                activity.overridePendingTransition(  R.anim.infromright,R.anim.out2left);   
        }
        else if (e2.getX() - e1.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity) {
            System.out.println("will runWhenToLeft()");
            runWhenToLeft();

//         //   overridePendingTransition(  R.anim.infromright,R.anim.out2left);  
             if(activity != null)
                 activity.overridePendingTransition(R.anim.infromleft,R.anim.out2right); 

        }
        doSomething();
        return false;
         
    }

    @Override
    public void onLongPress(MotionEvent arg0) {
        System.out.println("MyGestureListener onLongPress");
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2,
            float distanceX, float distanceY) {
        System.out.println("MyGestureListener onScroll");
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        System.out.println("MyGestureListener onShowPress");
        
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        System.out.println("MyGestureListener onSingleTapUp");
        return false;
    }
    
    
    @Override
    public boolean onDown(MotionEvent e) {
        System.out.println("MyGestureListener onDown");
        return false;
    }
    
    public void set(Activity activity,int verticalMinDistance, int minVelocity) {
        this.activity = activity;
        this.verticalMinDistance = verticalMinDistance;
        this.minVelocity = minVelocity;
        
    }
    
    
}

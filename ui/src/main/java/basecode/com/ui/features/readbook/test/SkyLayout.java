package basecode.com.ui.features.readbook.test;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

class SkyLayout extends RelativeLayout implements android.view.GestureDetector.OnGestureListener {
	public Object data;
	public View editControl;
	public View deleteControl;
	private GestureDetector gestureScanner;
	private static final int SWIPE_MIN_DISTANCE = 50;
    private static final int SWIPE_MAX_OFF_PATH = 1024;
    private static final int SWIPE_THRESHOLD_VELOCITY = 50;
    
    private SkyLayoutListener skyLayoutListener = null;
	 
	public SkyLayout(Context context) {
		super(context);		
		gestureScanner = new GestureDetector(this);
	}
	
	public void setSkyLayoutListener(SkyLayoutListener sl) {
		this.skyLayoutListener = sl;
	}
	
	@Override
    public boolean onTouchEvent(MotionEvent me) {
        return gestureScanner.onTouchEvent(me);
    }
 
    public boolean onDown(MotionEvent e) {
        return true;
    }
 
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        try {
            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                return false;
 
            // right to left swipe
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//                Toast.makeText(getContext(), "Left Swipe", Toast.LENGTH_SHORT).show();
                if (this.skyLayoutListener!=null) {
                	skyLayoutListener.onSwipeToLeft(this);
                }
            }
            // left to right swipe
            else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//                Toast.makeText(getContext(), "Right Swipe", Toast.LENGTH_SHORT).show();
                if (this.skyLayoutListener!=null) {
                	skyLayoutListener.onSwipeToRight(this);
                }
            }
            // down to up swipe
            else if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
//                Toast.makeText(getContext(), "Swipe up", Toast.LENGTH_SHORT).show();
            }
            // up to down swipe
            else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
//                Toast.makeText(getContext(), "Swipe down", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
 
        }
        return true;
    }
 
    public void onLongPress(MotionEvent e) {
    	if (this.skyLayoutListener!=null) {
    		this.skyLayoutListener.onLongPress(this,e);
    	}
    }
 
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return true;
    }
 
    public void onShowPress(MotionEvent e) {
    	if (this.skyLayoutListener!=null) {
    		this.skyLayoutListener.onShortPress(this,e);
    	}
    }
 
    public boolean onSingleTapUp(MotionEvent e) {
    	if (this.skyLayoutListener!=null) {
    		this.skyLayoutListener.onSingleTapUp(this,e);
    	}   	
    	return true;
    }
}



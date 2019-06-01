package basecode.com.ui.features.readbook.test;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;


class SkySeekBar extends SeekBar {
	boolean isReversed = false;
	
    public SkySeekBar(Context context) {
        super(context);
    }

    public SkySeekBar(Context context, AttributeSet attrs) {
    	super(context, attrs);
    }

    public SkySeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    public void setReversed(boolean value) {
    	this.isReversed = value;
    }

    @Override
    protected void onDraw(Canvas canvas) {
    	if (this.isReversed) {
    		float px = this.getWidth() / 2.0f;
    		float py = this.getHeight() / 2.0f;
    		canvas.scale(-1, 1, px, py);
    	}
    	super.onDraw(canvas);    		
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	if (this.isReversed) {
    		event.setLocation(this.getWidth() - event.getX(), event.getY());
    	}
        return super.onTouchEvent(event);
    }
}

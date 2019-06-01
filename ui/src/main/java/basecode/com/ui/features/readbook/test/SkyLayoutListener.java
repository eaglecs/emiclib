package basecode.com.ui.features.readbook.test;

import android.view.MotionEvent;

interface SkyLayoutListener {
	void onShortPress(SkyLayout view, MotionEvent e);
	void onLongPress(SkyLayout view, MotionEvent e);
	void onSingleTapUp(SkyLayout view, MotionEvent e);
	void onSwipeToLeft(SkyLayout view);
	void onSwipeToRight(SkyLayout view);
}



package basecode.com.ui.features.readbook.test;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;


class SkyDrawable extends ShapeDrawable {
    private final Paint fillpaint, strokepaint;
    public SkyDrawable(Shape s, int fillColor, int strokeColor, int strokeWidth) {
        super(s);
        fillpaint = new Paint(this.getPaint());
        fillpaint.setColor(fillColor);
        strokepaint = new Paint(fillpaint);
        strokepaint.setStyle(Paint.Style.STROKE);
        strokepaint.setStrokeWidth(strokeWidth);
        strokepaint.setColor(strokeColor);
    }
 
    @Override
    protected void onDraw(Shape shape, Canvas canvas, Paint paint) {
        shape.draw(canvas, fillpaint);
        shape.draw(canvas, strokepaint);
    }
}


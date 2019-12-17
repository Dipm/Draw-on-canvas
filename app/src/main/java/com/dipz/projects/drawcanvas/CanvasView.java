package com.dipsar.drawcanvas;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.RequiresApi;

public class CanvasView extends View {

    Context mContext;
    private Path mPath;
    private Paint mPaint;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private float mX, mY;
    private static final float TOLERANCE = 5;

    // for Paint
    private Paint.Style paintStyle = Paint.Style.STROKE;
    private int paintStrokeColor = Color.BLACK;
    private int paintFillColor;
    private float paintStrokeWidth = 3F;
    private int opacity = 255;
    private float blur = 0F;
    private Paint.Cap lineCap = Paint.Cap.ROUND;
    private PathEffect drawPathEffect = null;


    public CanvasView(Context context) {
        super(context);
        setDesiredPathAttributes(context, null, 0, 0);
    }

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDesiredPathAttributes(context, attrs, 0, 0);
    }

    public CanvasView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setDesiredPathAttributes(context, attrs, defStyleAttr, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CanvasView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setDesiredPathAttributes(context, attrs, defStyleAttr, defStyleRes);
    }

    private void setDesiredPathAttributes(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        if (attrs != null) {
            final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CanvasView, defStyleAttr, 0);

            paintFillColor= getResources().getColor(R.color.colorPrimary);
            paintFillColor = typedArray.getColor(R.styleable.CanvasView_paintFillColor, paintFillColor);
            typedArray.recycle();
        }
        mContext = context;
        mPath = new Path();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(paintFillColor);
        mPaint.setStyle(this.paintStyle);
        mPaint.setStrokeWidth(this.paintStrokeWidth);
        mPaint.setStrokeCap(this.lineCap);
        mPaint.setStrokeJoin(Paint.Join.MITER);  // fixed
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);

        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTouch(x, y);
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                upTouch();
                invalidate();
                break;

            case MotionEvent.ACTION_MOVE:
                moveTouch(x, y);
                invalidate();
                break;

        }

        return true;
    }

    private void startTouch(float x, float y) {
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void moveTouch(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);

        if (dx >= TOLERANCE || dy >= TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    private void upTouch() {
        mPath.lineTo(mX, mY);
    }

    public void clearCanvas() {
        mPath.reset();
        invalidate();
    }

    public void drawEraseCanvasMode(String modeDrawOrErase) {
        if (modeDrawOrErase.equalsIgnoreCase(MainActivity.DRAW_MODE)) {

        } else if (modeDrawOrErase.equalsIgnoreCase(MainActivity.ERASE_MODE)) {
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            mPaint.setARGB(0, 0, 0, 0);
        } else {

        }
        invalidate();
    }

    public int getPaintFillColor() {
        return mPaint.getColor();
    }

    public void setPaintFillColor(int paintFillColor) {
        mPaint.setColor(paintFillColor);
        invalidate();
    }
}

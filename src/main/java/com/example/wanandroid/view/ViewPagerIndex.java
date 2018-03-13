package com.example.wanandroid.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.wanandroid.R;

/**
 * Created by zhangchong on 18-3-13.
 */
public class ViewPagerIndex extends View{
    private int indexLength;
    private int indexDistance;
    private int indexColor;
    private int indexNum;
    private int indexLocate;
    private Paint paint;
    private int startX;
    private int startY;
    private Context context;

    public ViewPagerIndex(Context context) {
        this(context, null);
    }

    public ViewPagerIndex(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPagerIndex(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.index, defStyleAttr, 0);
        indexLength = (int) array.getDimension(array.getIndex(R.styleable.index_index_length), 10);
        indexDistance = (int) array.getDimension(array.getIndex(R.styleable.index_index_distance), 10);
        indexColor = array.getColor(R.styleable.index_index_color, getResources().getColor(R.color.colorMain));
        array.recycle();
        indexNum = 3;
        indexLocate = 0;
        paint = new Paint();
        paint.setColor(indexColor);
        indexLength = dip2pix(context, indexLength);
        indexDistance = dip2pix(context, indexDistance);
        this.context = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        calculateStart();
        for(int i=0;i<indexNum;i++)
        {
            if(i == indexLocate)
            {
                paint.setStyle(Paint.Style.FILL);
            }
            else
            {
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(2);
            }
            canvas.drawCircle(startX+i*indexDistance, startY, indexLength, paint);
        }
    }

    public static int dip2pix(Context context, int dp)
    {
        float density = context.getResources().getDisplayMetrics().density;
        return (int)(density*dp + 0.5);
    }

    public void calculateStart()
    {
        int width = getResources().getDisplayMetrics().widthPixels-dip2pix(context, 10);
        int height = getHeight();
        startX = (width - (indexNum - 1) * indexDistance)/2;
        startY = height/2;
    }

    public void setIndexNum(int indexNum)
    {
        this.indexNum = indexNum;
        invalidate();
    }

    public void setIndexLocate(int indexLocate)
    {
        this.indexLocate = indexLocate;
        invalidate();
    }
}

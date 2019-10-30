package com.example.bigpicview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;

public class BigView extends View implements GestureDetector.OnGestureListener,View.OnTouchListener {
    //加载矩形
    private Rect mRect;
    //图片参数
    private BitmapFactory.Options mOptions;
    //手势
    private GestureDetector mGesture;
    //滚动
    private Scroller mScroller;
    //图片宽高
    private int mImageWidth,mImageHeight;

    private BitmapRegionDecoder mDecoder;
    private int mViewWidth;
    private int mViewHeight;

    private float mScale;
    private Bitmap mBitmap;

    public BigView(Context context) {
        this(context,null);
    }

    public BigView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BigView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRect = new Rect();
        mOptions = new BitmapFactory.Options();
        mGesture = new GestureDetector(context,this);
        mScroller = new Scroller(context);
        setOnTouchListener(this);
    }

    public void setImage(InputStream is){
        mOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is,null,mOptions);
        mImageWidth = mOptions.outWidth;
        mImageHeight = mOptions.outHeight;
        //开启复用
        mOptions.inMutable = true;
        //设置格式  ARGB 透明度+红绿蓝(三原色)
        //ARGB_8888  占用4个字节  4个8位 32位   700*2400*4(字节)
        //ARGB_4444  占用2个字节  4个4位 16位   700*2400*2(字节)
        //RGB_565    占用2个字节  16位          700*2400*2(字节)
        mOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        mOptions.inJustDecodeBounds = false;
        //区域解码器
        try {
            mDecoder = BitmapRegionDecoder.newInstance(is,false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        requestLayout();
    }
    //开始测量
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewWidth = getMeasuredWidth();
        mViewHeight = getMeasuredHeight();
        //确定图片的加载区域
        mRect.left = 0;
        mRect.top = 0;
        mRect.right = mImageWidth;//必须是图片宽度
        //得到图片加载的具体的高度
        //根据图片的宽度,以及view的宽度,计算缩放因子
        mScale = mViewWidth / (float)mImageWidth;
        mRect.bottom = (int)(mViewHeight/mScale);
    }
    private Matrix matrix;
    //开始画图片,画具体的内容
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mDecoder==null){
            return;
        }
        //内存复用(复用的bitmap必须跟即将解码的bitmap尺寸一样)
        mBitmap = mOptions.inBitmap;
        //指定解码区域
        mBitmap = mDecoder.decodeRegion(mRect,mOptions);
        //得到一个矩阵进行缩放,得到view 的大小   ====>缩放因子
        matrix = new Matrix();
        matrix.setScale(mScale,mScale);
        canvas.drawBitmap(mBitmap,matrix,null);
    }
    //第五步、处理点击事件
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //直接将事件交给手势事件
        return mGesture.onTouchEvent(event);
    }
    //第六步、按下
    @Override
    public boolean onDown(MotionEvent e) {
        //如果移动没有停止、强行停止
        if (!mScroller.isFinished()){
            mScroller.forceFinished(true);
        }
        return true;//事件传递
    }
    //第七步、处理滑动事件
    //e1 开始事件 手指按下去获取坐标
    //e2 获取当前事件的坐标
    //distanceX,Y  移动的距离
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        //上下移动时,mRect需要改变现实的区域
        mRect.offset(0,(int)distanceY);
        //移动的时候,处理到达顶部或者底部的情况
        if (mRect.bottom > mImageHeight){
            mRect.bottom = mImageHeight;
            mRect.top= mImageHeight - (int)(mViewHeight/mScale);//图片高-显示高=图片显示起点高
            Log.e("weip",mImageHeight+"======"+(int)(mViewHeight/mScale));
        }
        if (mRect.top<0){
            mRect.top = 0;
            mRect.bottom = (int)(mViewHeight/mScale);
        }
        invalidate();//失效,前面都作废重新再来一次
        return false;
    }
    //第八步、处理惯性问题
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        //velocityY为负   你没吃饭吗？  是的   老外的思维和我们的不一样
        mScroller.fling(0,mRect.top,0,(int)-velocityY,0,0,0,mImageHeight-(int)(mViewHeight/mScale));
        return false;
    }
    //第九步,处理计算结果
    @Override
    public void computeScroll() {
        if (mScroller.isFinished()){
            return;
        }
        //为true  滑动还没有结束
        if (mScroller.computeScrollOffset()){
            mRect.top = mScroller.getCurrY();
            mRect.bottom = mRect.top+(int)(mViewHeight/mScale);
            invalidate();
        }
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }



    @Override
    public void onLongPress(MotionEvent e) {

    }



}

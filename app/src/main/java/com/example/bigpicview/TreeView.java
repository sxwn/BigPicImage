package com.example.bigpicview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Iterator;
import java.util.LinkedList;

public class TreeView extends View {
    //存储
    LinkedList<Branch> growingBranches;
    private Paint paint;
    private Canvas treeCanvas;
    private Bitmap bitmap;

    public TreeView(Context context) {
        this(context,null);
    }
    public TreeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }
    public TreeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //初始化
        paint = new Paint();
        growingBranches = new LinkedList<>();
        //树枝数据()???
        growingBranches.add(getBranches());
    }

    private Branch getBranches() {
        int[][] data = new int[][]{
                {0,-1,217,490,252,60,182,10,30,100},
                {1,0,222,310,137,227,22,210,13,100},
                {2,1,132,245,116,240,76,205,2,40},
                {3,0,232,255,282,166,362,155,12,100},
                {4,3,260,210,330,219,343,236,3,80},
                {5,0,217,91,219,58,216,27,3,40},
                {6,0,228,207,95,57,10,54,9,80},
                {7,6,109,96,65,63,53,15,2,40},
                {8,6,180,155,117,125,77,140,4,60},
                {9,0,228,167,290,62,360,31,6,100},
                {10,9,272,103,328,87,330,81,2,80}
        };
        int n = data.length;
        //循环分组
        Branch[] branches = new Branch[n];
        for (int i =0;i<n;i++){
            branches[i] = new Branch(data[i]);
            // 分组(parentId)
            int parentId = data[i][1];
            if (parentId!= -1){
                branches[parentId].addChild(branches[i]);
            }
        }
        return branches[0];
    }
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//    }
//    @Override
//    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        super.onLayout(changed, left, top, right, bottom);
//    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bitmap = Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888);
        treeCanvas = new Canvas(bitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制
        drawBranches();
        canvas.drawBitmap(bitmap,0,0,paint);
    }

    private void drawBranches() {
        if (!growingBranches.isEmpty()){
            LinkedList<Branch> tempBranches = null;
            Iterator<Branch> iterator = growingBranches.iterator();
            while (iterator.hasNext()){
                Branch branch = iterator.next();
                //生长
                treeCanvas.save();
                treeCanvas.translate(getWidth()/2 - 217*2,getHeight() - 490*2);
                if (!branch.grow(treeCanvas,paint,1)){
                    //绘制完成
                    iterator.remove();
                    //判断是否有分枝
                    if (branch.childList!=null){
                        //有分枝
                        if (tempBranches == null){
                            tempBranches = branch.childList;
                        }else {
                            tempBranches.addAll(branch.childList);
                        }
                    }
                }
                treeCanvas.restore();
            }
            if (tempBranches!=null){
                growingBranches.addAll(tempBranches);
            }
            if (!growingBranches.isEmpty()){
                //继续绘制
                invalidate();
            }
        }
    }
}

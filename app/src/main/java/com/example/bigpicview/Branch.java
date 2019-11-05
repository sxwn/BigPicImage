package com.example.bigpicview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;

import java.util.LinkedList;

public class Branch {
    //形状(3个控制点)
    private PointF[] cp = new PointF[3];
    //粗细
    private float redius;
    //长度
    private int maxLength;
    private int currentLength;
    private float part;//一根树枝每一次绘制的长度
    //颜色
    public static int branchColor = 0xff773322;
    //分枝
    LinkedList<Branch> childList;
    //绘制
    private float growX, growY;

    public Branch(int data[]) {
        //id,parentId,pointF,max radius,length
        //{0, -1,217,490,252,60,182,10,30,100}
        cp[0] = new PointF(data[2], data[3]);
        cp[1] = new PointF(data[4], data[5]);
        cp[2] = new PointF(data[6], data[7]);
        redius = data[8];
        maxLength = data[9];
        part = 1f / maxLength;
    }

    public void addChild(Branch branch) {
        if (childList == null) {
            childList = new LinkedList<>();
        }
        childList.add(branch);
    }

    public boolean grow(Canvas canvas, Paint paint, int scaleFactor) {
        if (currentLength < maxLength) {
            //计算当前绘制点的位置
            bezier(part * currentLength);
            //绘制
            draw(canvas, paint, scaleFactor);
            currentLength++;
            redius *= 0.97f;
            return true;
        } else {
            return false;
        }
    }

    private void bezier(float t) {
        //path
//        Path path = new Path();
//        path.moveTo();
//        path.quadTo();
//        PathMeasure pathMeasure = new PathMeasure(path,false);
//        pathMeasure.getPosTan(t*maxLength,pos,tan)
        float c0 = (1 - t) * (1 - t);
        float c1 = 2 * t * (1 - t);
        float c2 = t * t;
        growX = c0 * cp[0].x + c1 * cp[1].x + c2 * cp[2].x;
        growX = c0 * cp[0].y + c1 * cp[1].y + c2 * cp[2].y;
    }

    private void draw(Canvas canvas, Paint paint, int scaleFactor) {
        paint.setColor(branchColor);
        canvas.save();
        canvas.scale(scaleFactor,scaleFactor);
        canvas.drawCircle(growX,growY,redius,paint);
        canvas.restore();//不会影响后面的内容
    }
}

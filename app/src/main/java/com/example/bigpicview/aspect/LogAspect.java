package com.example.bigpicview.aspect;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.example.bigpicview.annotation.LogRecord;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.concurrent.CopyOnWriteArrayList;

@Aspect
public class LogAspect {

    private final String TAG = LogAspect.class.getSimpleName();

    //切点
    @Pointcut("execution(@com.example.bigpicview.annotation.LogRecord * * (..)) && @annotation(log)")
    public void pointActionMethod(LogRecord log){ }

    //切面
    @Around("pointActionMethod(log)")
    public void aProceedingJoinPoint(final ProceedingJoinPoint proceedingJoinPoint,LogRecord log) throws Throwable{
        //初始化Context
        Context context = null;
        //Aspect 需要依赖 Java环境的 Java1.6 1.5
        final Object thisObject = proceedingJoinPoint.getThis();//这里面有坑 有可能为null
        if (thisObject instanceof Context){
            context = (Context) thisObject;
        }else if(thisObject instanceof Fragment){
            context = ((Fragment)thisObject).getActivity();
        }
        //检测环境
        if (context == null){
            throw new IllegalAccessException("context == null");
        }
        //日志文件的保存到文件中去
        String value = log.value();
        Log.d(TAG,"aProceedingJoinPoint:进行日志的统一埋点 当前执行了"+value);

    }


}

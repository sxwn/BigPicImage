package com.example.bigpicview.aspect;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.bigpicview.annotation.NoNetworkShow;
import com.example.bigpicview.utils.NetworkUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.lang.reflect.Method;

@Aspect
public class NetworkUtilsAspect {

    //进行一个切点  1
    @Pointcut("execution(@com.example.bigpicview.annotation.NetworkCheck * * (..))")
    public void pointActionMethod(){}

    //进行切面的处理  2
    @Around("pointActionMethod()")
    public void aProceedingJoinPoint(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        //拿到环境
        Context context = null; //初始化Context  兼容多种情况
        final Object thisObject = proceedingJoinPoint.getThis();
        //在其它地方(Activity Fragment View)的地方使用了 @NetworkCheck
        if (thisObject instanceof Context){
            context = (Context) thisObject;
        }else if(thisObject instanceof Fragment){
            context = ((Fragment) thisObject).getActivity();
        }
        //检测环境
        if (context == null){
            throw new IllegalAccessException("context == null");
        }
        //是否使用到这个注解了 @NetworkCheck
        //thisObject == MainActivity

        //先判断是否有网络
        if (NetworkUtils.isNetworkAvaliable(context)){
            //网络ok,被@NetworkCheck的方法正常的走下去
            proceedingJoinPoint.proceed();
        }else {
            //第一版
//            Toast.makeText(context,"没有网络",Toast.LENGTH_SHORT).show();
            //第二版
            Class<?> thisObjectClass = thisObject.getClass();
            Method[] methods = thisObjectClass.getDeclaredMethods();
            for(Method method:methods){
                method.setAccessible(true);//官方解释:让虚拟机跳跃不要检测private修饰符
                //谁满足这个注解@NoNerworkShow  就执行它
                boolean annotationPresent = method.isAnnotationPresent(NoNetworkShow.class);
                if (annotationPresent){
                    //此method == NoNetworkShow注解配置的
                    method.invoke(thisObject);
                }
            }
            //第三版
            //跳转到网络的设置界面
//            NetworkUtils.getSettings(context);
        }
    }

}

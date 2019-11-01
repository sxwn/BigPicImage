package com.example.bigpicview.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(METHOD)//作用域在方法之上
@Retention(RUNTIME)//运行时
public @interface LogRecord {
    String value() default "默认需求,用户没有填,我也没有办法";
}

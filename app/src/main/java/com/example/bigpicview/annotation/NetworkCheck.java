package com.example.bigpicview.annotation;


import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

@Target(METHOD)//作用域在方法之上
@Retention(RUNTIME)//运行时
public @interface NetworkCheck {
}

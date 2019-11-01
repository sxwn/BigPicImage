package com.example.bigpicview;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.bigpicview.annotation.LogRecord;
import com.example.bigpicview.annotation.NetworkCheck;
import com.example.bigpicview.annotation.NoNetworkShow;

import java.io.IOException;
import java.io.InputStream;


/**
 * 面向切面编程
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BigView bigView = findViewById(R.id.bigview);
        InputStream is = null;
        try {
            is = getAssets().open("a.jpg");
            bigView.setImage(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        findViewById(R.id.activity_main_bt_action1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAction1();
            }
        });
        findViewById(R.id.activity_main_bt_action2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAction2();
            }
        });
        findViewById(R.id.activity_main_bt_action3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAction3();
            }
        });
        findViewById(R.id.activity_main_bt_action4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLoginEngine();
            }
        });
        findViewById(R.id.activity_main_bt_action5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              doRegisterEngine();
            }
        });
    }
    //进入功能1
    @NetworkCheck
    public void startAction1(){
        //如果有网络才执行下面代码，否则方法不执行
        startActivity(new Intent(this,SimpleActivity.class));
    }
    //进入功能2
    @NetworkCheck
    public void startAction2(){
        //如果有网络才执行下面代码，否则方法不执行
        startActivity(new Intent(this,SimpleActivity.class));
    }
    //进入功能3
    @NetworkCheck
    public void startAction3(){
        //如果有网络才执行下面代码，否则方法不执行
        startActivity(new Intent(this,SimpleActivity.class));
    }

    //没有网络的提示
    @NoNetworkShow
    public void noNetwork(){
        new AlertDialog.Builder(this)
                .setMessage("没有网络了")
                .setNegativeButton("朕知道了",null)
                .show();
    }

    //*******************************
    @LogRecord("在首页 我的 完成的登录操作")
    public void doLoginEngine(){

    }

    @LogRecord("App首次启动时 完成的注册操作")
    public void doRegisterEngine(){

    }
}

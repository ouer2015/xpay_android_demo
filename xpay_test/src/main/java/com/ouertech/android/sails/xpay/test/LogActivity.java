/*
 * ========================================================
 * Copyright(c) 2014 杭州偶尔科技版权所有
 * ========================================================
 * 本软件由杭州偶尔科技所有, 未经书面许可, 任何单位和个人不得以
 * 任何形式复制代码的部分或全部, 并以任何形式传播。
 * 公司网址
 * 
 * 			http://www.kkkd.com/
 * 
 * ========================================================
 */
package com.ouertech.android.sails.xpay.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.ouertech.android.sails.ouer.base.constant.CstBase;
import com.ouertech.android.sails.ouer.base.constant.CstFile;
import com.ouertech.android.sails.ouer.base.utils.UtilFile;
import com.ouertech.android.sails.ouer.base.utils.UtilStorage;
import com.ouertech.android.sails.ouer.base.utils.UtilString;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author : Zhenshui.Xia
 * @since : 2015/10/19.
 * desc : 日志界面
 */
public class LogActivity extends AppCompatActivity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xpay_activity_log);

        //返回
        findViewById(R.id.xpay_id_nav).setOnClickListener(this);

        String fileName = CstBase.PROJECT +"_ANDROID_"
                + new SimpleDateFormat("yyyyMMdd_HH").format(new Date())
                + CstFile.SUFFIX_LOG;
        String filePath = UtilStorage.createFilePath(null, fileName);

        if(UtilString.isNotBlank(filePath)) {
            TextView tvLog = (TextView)findViewById(R.id.xpay_id_log);
            String log = UtilFile.readFileToString(filePath);
            log = log.length()>10000 ? log.substring(log.length() - 10000) : log;
            tvLog.setText(log);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.xpay_id_nav://返回
                finish();
                break;
            default:
                break;
        }
    }
}

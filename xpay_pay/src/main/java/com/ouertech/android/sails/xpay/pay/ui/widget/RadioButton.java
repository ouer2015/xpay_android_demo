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
package com.ouertech.android.sails.xpay.pay.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import com.xiangqu.app.R;


/**
 * @author : Zhenshui.Xia
 * @since : 2015/8/29.
 * desc : 自定义单选按钮
 */
public class RadioButton extends Button {
    private boolean mChecked;

    public RadioButton(Context context) {
        super(context);
        init(context);
    }

    public RadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mChecked = false;
        setBackgroundResource(R.drawable.xpay_ic_radio_unchecked);
    }

    /**
     * 设置单选按钮是否选中
     * @param checked
     */
    public void setChecked(boolean checked) {
        mChecked = checked;
        if(mChecked) {
            setBackgroundResource(R.drawable.xpay_ic_radio_checked);
        } else {
            setBackgroundResource(R.drawable.xpay_ic_radio_unchecked);
        }
    }

    /**
     * 获取单选按钮是否选中
     * @return TODO
     */
    public boolean isChecked() {
        return mChecked;
    }
}

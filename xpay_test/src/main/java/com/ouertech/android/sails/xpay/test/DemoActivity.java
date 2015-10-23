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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.ouertech.android.sails.ouer.base.constant.CstBase;
import com.ouertech.android.sails.ouer.base.constant.CstHttp;
import com.ouertech.android.sails.ouer.base.future.base.OuerFutureData;
import com.ouertech.android.sails.ouer.base.future.base.OuerFutureListener;
import com.ouertech.android.sails.ouer.base.future.core.AgnettyResult;
import com.ouertech.android.sails.ouer.base.future.defaults.OuerHttpDefaultHandler;
import com.ouertech.android.sails.ouer.base.future.http.HttpFuture;
import com.ouertech.android.sails.ouer.base.future.impl.OuerClient;
import com.ouertech.android.sails.xpay.lib.constant.CstXPay;
import com.ouertech.android.sails.xpay.lib.data.bean.Charge;
import com.ouertech.android.sails.xpay.lib.data.bean.PayResult;
import com.ouertech.android.sails.xpay.lib.future.impl.XPay;

/**
 * @author : Zhenshui.Xia
 * @since : 2015/9/10.
 * desc : XPay示例程序，仅供开发者参考。当前demo仅提供使用流程，尚不能演示完整的渠道支付流程，
 * 我们会在后续迭代中完善。
 * 【说明文档】开发前请参考XPay安卓SDK开发者使用指南，地址：http://www.kkkd.com/home/_resources/files/xpay_android.zip
 * 【开发流程】
 * 1）客户端下单和支付可以合并为一个流程也可以分开处理，如果分开处理，下单可以分为交易下单、支付下单，无论哪种方式
 *    支付前都需要先获取到支付凭证。
 * 2）客户端请求服务端获得charge。获取charge的接口需要商户服务端自己开发，相关接口开发规范请参考XPay官方文档，
 *    地址： http://www.kkkd.com/home/_resources/files/xpay_api.pdf
 * 3）收到服务端的charge，调用XPay SDK的支付接口：XPay.pay(activty, charge)。
 * 4）onActivityResult 中获得支付结果，开发者根据支付结果做出相应的处理
 * 5）如果支付成功。服务端会收到XPay的异步通知，支付成功依据服务端异步通知为准。
 */
public class DemoActivity extends AppCompatActivity implements View.OnClickListener{
    //填写开发者申请应用的app ID
    private static final String APP_ID      = "4ab6526f6d19fecfb3be6a07d7e62f1d";
    //下单地址
    private static final String ORDER_URL   = "http://api.kkkd.com/payDemo/chargeSubmit.jsp";

    //调试模式
    private TextInputLayout mTilDebug;
    //appId
    private TextInputLayout mTilAppId;
    //凭证接口URL
    private TextInputLayout mTilUrl;
    //渠道
    private TextInputLayout mTilChannel;
    //订单号
    private TextInputLayout mTilTN;
    //标题
    private TextInputLayout mTilTitle;
    //价格
    private TextInputLayout mTilPrice;
    //支付
    private Button mBtnPay;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xpay_activity_demo);

        //平台初始化，初始化工作必须放在平台服务调用之前，建议初始化工作放在Application里
        OuerClient.init(getApplication(), APP_ID, true, "xpay");
        CstBase.INNER_DEBUG = true;

        //调试模式
        mTilDebug = (TextInputLayout)findViewById(R.id.xpay_id_debug);
        mTilDebug.getEditText().setText("true");
        //appId
        mTilAppId = (TextInputLayout)findViewById(R.id.xpay_id_appId);
        mTilAppId.getEditText().setText(APP_ID);
        //凭证接口URL
        mTilUrl = (TextInputLayout)findViewById(R.id.xpay_id_url);
        mTilUrl.getEditText().setText(ORDER_URL);
        //渠道
        mTilChannel = (TextInputLayout)findViewById(R.id.xpay_id_channel);
        mTilChannel.getEditText().setText(CstXPay.CHANNEL_WXPAY);
        //订单号
        mTilTN = (TextInputLayout)findViewById(R.id.xpay_id_tn);
        mTilTN.getEditText().setText(String.valueOf(System.currentTimeMillis()));
        //标题
        mTilTitle = (TextInputLayout)findViewById(R.id.xpay_id_title);
        mTilTitle.getEditText().setText("XPay测试");
        //价格
        mTilPrice = (TextInputLayout)findViewById(R.id.xpay_id_price);
        mTilPrice.getEditText().setText(String.valueOf(1));

        //log
        findViewById(R.id.xpay_id_log).setOnClickListener(this);
        //支付
       mBtnPay = (Button)findViewById(R.id.xpay_id_pay);
        setPayListener();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.xpay_id_pay:
                CstBase.DEBUG = CstBase.INNER_DEBUG
                        = Boolean.valueOf(mTilDebug.getEditText().getText().toString().trim());
                CstBase.APP_ID = mTilAppId.getEditText().getText().toString().trim();

                //下单支付
                orderPay(mTilChannel.getEditText().getText().toString().trim(),
                         mTilTN.getEditText().getText().toString().trim(),
                         Integer.parseInt(mTilPrice.getEditText().getText().toString().trim()),
                         mTilTitle.getEditText().getText().toString().trim());
                break;
            case R.id.xpay_id_log:
                Intent intent = new Intent(this, LogActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //获取支付结果
        PayResult result = XPay.getPayResult(requestCode, resultCode, data);
        if(result != null) {
            int status = result.getStatus(); //支付状态
            String memo = result.getMemo(); //支付状态说明
            String attach = result.getAttach();//额外信息
            //todo 此处开发者根据支付结果做出相应的处理
            switch (status) {
                case CstXPay.PAY_SUCCESS: //支付成功
                    break;
                case CstXPay.PAY_CANCELED://支付取消
                    break;
                case CstXPay.PAY_FAILED: //支付失败
                    break;
                case CstXPay.PAY_PENDING: //支付结果确认中,最终交易是否成功以服务端异步通知为准
                    break;
                case CstXPay.PAY_INVALID: //支付不合法
                    break;
                default:
                    break;
            }

            Toast.makeText(this, memo, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 下单获取charge ，需要异步处理，此处类似于系统的AsyncTask的HTTP异步任务，开发者根据自己客户端的
     * 异步处理框架自行开发就行
     * @param channel   支付渠道
     * @param partnerTradeNo 交易号
     * @param amount 金额（分）
     * @param title 标题
     */
    private void orderPay(String channel, String partnerTradeNo,
                          int amount, String title) {
        OrderReq req = new OrderReq();
        req.setChannel(channel);
        req.setPartnerTradeNo(partnerTradeNo);
        req.setAmount(amount);
        req.setTitle(title);

        new HttpFuture.Builder(this, CstHttp.GET)
                .setUrl(mTilUrl.getEditText().getText().toString().trim())
                .setHandler(OuerHttpDefaultHandler.class)
                .setData(new OuerFutureData(req, new TypeToken<Charge>() {}.getType()))
                .setListener(new OuerFutureListener(this) {
                    @Override
                    public void onStart(AgnettyResult result) {
                        super.onStart(result);
                        //todo ui线程回调，任务开始
                        clearPayListener();
                    }

                    @Override
                    public void onComplete(AgnettyResult result) {
                        super.onComplete(result);
                        //todo ui线程回调，获取charge成功
                        Charge charge = (Charge) result.getAttach();
                        XPay.pay(DemoActivity.this, charge);
                        setPayListener();
                    }

                    @Override
                    public void onException(AgnettyResult result) {
                        super.onException(result);
                        //todo ui线程回调，获取charge失败
                        setPayListener();
                    }

                    @Override
                    public void onNetUnavaiable(AgnettyResult result) {
                        super.onNetUnavaiable(result);//主要此处有个默认的Toast提示，如果不展示，可以删除父类方法的实现
                        //todo ui线程回调，当前网络不给力
                        setPayListener();
                    }
                })
                .execute();
    }

    /**
     * 设置支付监听器
     */
    private void setPayListener() {
        mBtnPay.setOnClickListener(this);
    }

    /**
     * 取消支付监听器，避免重复提交
     */
    private void clearPayListener() {
        mBtnPay.setOnClickListener(null);
    }
}

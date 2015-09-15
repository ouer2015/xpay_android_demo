/*
 * ========================================================
 * Copyright(c) 2014 杭州偶尔科技-版权所有
 * ========================================================
 * 本软件由杭州龙骞科技所有, 未经书面许可, 任何单位和个人不得以
 * 任何形式复制代码的部分或全部, 并以任何形式传播。
 * 公司网址
 * 
 * 			http://www.kkkd.com/
 * 
 * ========================================================
 */
package com.ouertech.android.sails.xpay.pay.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.ouertech.android.sails.ouer.base.future.base.OuerFutureListener;
import com.ouertech.android.sails.ouer.base.future.core.AgnettyFuture;
import com.ouertech.android.sails.ouer.base.future.core.AgnettyResult;
import com.ouertech.android.sails.ouer.base.utils.UtilList;
import com.ouertech.android.sails.ouer.base.utils.UtilLog;
import com.ouertech.android.sails.ouer.ui.base.BaseTopActivity;
import com.ouertech.android.sails.xpay.lib.constant.CstXPay;
import com.ouertech.android.sails.xpay.lib.data.bean.Charge;
import com.ouertech.android.sails.xpay.lib.data.bean.Payment;
import com.ouertech.android.sails.xpay.pay.constant.CstExPay.WEIXIN;
import com.ouertech.android.sails.xpay.pay.constant.CstExPay.BROADCAST_ACTION;
import com.ouertech.android.sails.xpay.pay.future.impl.ExPay;
import com.ouertech.android.sails.xpay.pay.ui.adapter.PaymentAdapter;
import com.ouertech.android.sails.xpay.pay.utils.SignUtils;
import com.ouertech.android.sails.xpay.pay.utils.UtilCache;
import com.ouertech.android.sails.xpay.pay.utils.UtilXPay;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.xiangqu.app.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * @author : Zhenshui.Xia
 * @date : 2015/8/28.
 * @desc :付款界面
 */
public class PaymentActivity extends BaseTopActivity {
    private long mPaymentTime;

    private LinearLayout mLlPayment;
    private TextView mTvEmpty;
    private PaymentAdapter mAdapter;


    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        registerAction(BROADCAST_ACTION.UPDATE_PAYMENTS_ACTION);
    }

    @Override
    protected void initTop() {
        setNavigation(R.drawable.xpay_ic_arrow_left);
        setTitle(R.string.xpay_string_pay_title);
    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.xpay_activity_payment);
    }

    @Override
    protected void initViews() {
        mLlPayment = (LinearLayout)findViewById(R.id.xpay_id_pay_root);
        mTvEmpty = (TextView)findViewById(R.id.xpay_id_pay_empty);

        ListView lvPayments = (ListView)findViewById(R.id.xpay_id_pay_list);
        mAdapter = new PaymentAdapter(this, null);
        lvPayments.setAdapter(mAdapter);

        findViewById(R.id.xpay_id_pay).setOnClickListener(this);

        //失败重试
        setOnRetryListener(new OnRetryListener() {
            @Override
            public void onRetry() {
                getPayments();
            }
        });

        List<Payment> datas = getCachePayments();
        if(UtilList.isEmpty(datas)) {
            //获取支付方式
            getPayments();
        } else {
            mAdapter.refresh(datas);
            getPaymentsBackground();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if(id == R.id.xpay_id_pay) {//付款
            pay();
        }
    }

    @Override
    protected void onReceive(Intent intent) {
        super.onReceive(intent);
        String action = intent.getAction();
        if(BROADCAST_ACTION.UPDATE_PAYMENTS_ACTION.equals(action)) {
            List<Payment> datas = getCachePayments();
            mAdapter.refresh(datas);
        }
    }


    /**
     * 获取支付方式
     */
    private void getPayments() {
        AgnettyFuture future = ExPay.getPayments(
                new OuerFutureListener(this) {

                    @Override
                    public void onStart(AgnettyResult result) {
                        super.onStart(result);
                        mPaymentTime = System.currentTimeMillis();
                        setLoading(true);
                    }

                    @Override
                    public void onComplete(final AgnettyResult result) {
                        super.onComplete(result);
                        long delay = System.currentTimeMillis() - mPaymentTime;
                        delay = delay < 1500 ? 1500 - delay : delay;

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setLoading(false);

                                List<Payment> datas = (List<Payment>) result.getAttach();
                                if (UtilList.isNotEmpty(datas)) {
                                    mLlPayment.setVisibility(View.VISIBLE);
                                    mTvEmpty.setVisibility(View.GONE);
                                } else {
                                    mLlPayment.setVisibility(View.GONE);
                                    mTvEmpty.setVisibility(View.VISIBLE);
                                }

                                mAdapter.refresh(datas);
                            }
                        }, delay);
                    }

                    @Override
                    public void onException(AgnettyResult result) {
                        super.onException(result);
                        long delay = System.currentTimeMillis() - mPaymentTime;
                        delay = delay < 1500 ? 1500 - delay : delay;

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                UtilXPay.showTip(PaymentActivity.this, R.string.xpay_string_pay_get_payments_failure);
                                setRetry(true);
                            }
                        }, delay);

                    }

                    @Override
                    public void onNetUnavaiable(AgnettyResult result) {
                        UtilXPay.showNetworkUnavaiable(PaymentActivity.this);
                        setRetry(true);
                    }

                });

        attachDestroyFutures(future);
    }

    /**
     * 后台更新支付方式
     */
    private void getPaymentsBackground() {
        AgnettyFuture future = ExPay.getPayments(
                new OuerFutureListener(this) {

                    @Override
                    public void onComplete(final AgnettyResult result) {
                        super.onComplete(result);

                        List<Payment> datas = (List<Payment>) result.getAttach();
                        if (UtilList.isNotEmpty(datas)) {
                            mLlPayment.setVisibility(View.VISIBLE);
                            mTvEmpty.setVisibility(View.GONE);
                        } else {
                            mLlPayment.setVisibility(View.GONE);
                            mTvEmpty.setVisibility(View.VISIBLE);
                        }

                        mAdapter.refresh(datas);
                    }

                    @Override
                    public void onNetUnavaiable(AgnettyResult result) {

                    }
                });

        attachDestroyFutures(future);
    }


    /**
     * 付款
     */
    private void pay(){
        String channel = mAdapter.getChannel();
        if(CstXPay.CHANNEL_BANK.equals(channel)) {//银行卡支付，打开银行卡选择界面
            bankPay();
        } else if(CstXPay.CHANNEL_WX.equals(channel)) {
            wxPay();
        } else if(CstXPay.CHANNEL_ALIPAY.equals(channel)) {
            alipayPay();
        }
    }

    private List<Payment> getCachePayments() {
        List<Payment> datas = UtilCache.getPayments(this);
        Payment payment = UtilCache.getBankPayment(this);

        if(datas == null) {
            datas = new ArrayList<>();
        }

        if(payment != null) {
            datas.add(0, payment);
        }

        return datas;
    }


    final IWXAPI mWxApi = WXAPIFactory.createWXAPI(this, null);

    private void wxPay() {
        mWxApi.registerApp(WEIXIN.APP_ID);

        if(!mWxApi.isWXAppInstalled()) {
            UtilXPay.showTipWithAction(this,
                    R.string.xpay_string_wx_uninstalled,
                    R.string.xpay_string_install,
                    new ActionClickListener() {
                        @Override
                        public void onActionClicked(Snackbar snackbar) {
                            UtilXPay.installWxApp(PaymentActivity.this,
                                "http://dldir1.qq.com/weixin/android/weixin624android600.apk",
                                R.string.xpay_string_notify_wx_ticker,
                                R.string.xpay_string_notify_wx_title,
                                R.string.xpay_string_notify_progress);
                        }
                    });
        }

        if(!mWxApi.isWXAppSupportAPI()) {
            UtilXPay.showTipWithAction(this,
                    R.string.xpay_string_wx_unsupport,
                    R.string.xpay_string_update,
                    new ActionClickListener() {
                        @Override
                        public void onActionClicked(Snackbar snackbar) {
                            UtilXPay.installWxApp(PaymentActivity.this,
                                    "http://dldir1.qq.com/weixin/android/weixin624android600.apk",
                                    R.string.xpay_string_notify_wx_ticker,
                                    R.string.xpay_string_notify_wx_title, 
                                    R.string.xpay_string_notify_progress);
                        }
                    });
        }

        ExPay.order(new OuerFutureListener(this){
            @Override
            public void onComplete(AgnettyResult result) {
                super.onComplete(result);
                Charge charge = (Charge)result.getAttach();
                PayReq req = new PayReq();
                req.appId = charge.getAppId();
                req.partnerId = charge.getPartnerId();
                req.prepayId = charge.getPrepayId();
                req.packageValue = charge.getPackageValue();
                req.nonceStr = charge.getNonceStr();
                req.timeStamp = charge.getTimeStamp();
                req.sign = charge.getSign();
                mWxApi.sendReq(req);
            }
        });
    }







    private void bankPay() {
        Intent intent = new Intent(this, BanksActivity.class);
        startActivity(intent);
    }


    //商户PID
    public static final String PARTNER = "2088511131123172";
    //商户收款账号
    public static final String SELLER = "admin@ixiaopu.com";
    //商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBANs4xf/BqOre1qZB\n" +
            "nK2zNRAIY2d91bVHfbJ4qsnXzD35CxlWY4n9Z7Ybr339l6hqKVTZU/z5YRu49sFj\n" +
            "S83QV8VT/OHLYfaNEHGNR1lOQoGBb/c6ZqsDhG5gTKgCcoueyn9TmujOmgE4Sxg/\n" +
            "/e9FmDmVhMbQXCGTy6fk1OcqoOsdAgMBAAECgYBeqDMGbg6L69BvVWFbt0ewNo8F\n" +
            "ftYqQkZKK8RDB0UPdVuPgzfTu3FutqGeG0PC2qQiGP0k/GEr4T/a3Q88PTvIEatT\n" +
            "06nxD5UKvKhbgmFHONpvH5GKqcSp6v0602mDpuKloKx4P5vcOzVdfj52CWZvDb1Y\n" +
            "5mLQ/NykxCbAI6gz2QJBAPy42xdj9EMZAP9iHgq6lwhobAWL6c4Y6IISDOv7KieI\n" +
            "EXMNZJZhWx6U3oyJJdLPlGqJ/5lW3s6uakXGeGP7oo8CQQDeEK6wZM7Dkdf+2/K9\n" +
            "YZSC0itQD4MjJuqhyZRekICjAYri+fVfm+1p+gg85GapOMhNn9YSYm2ZcND7m3K3\n" +
            "cb2TAkEA6zAlRKlSojbcxM5M4vMupzZ2gNrqZVCs4I9n0xzU6CErDAVEvOYxZE5q\n" +
            "piLkPkHL/zIZ2lwL0hBi+jhvxaxiZQJBAKvVDNfBqZz7hb/YQoT4tQplxs40sqzI\n" +
            "uFZovT5PYrq/vtDTymMpLRUMPMOhmS7omVY3kFa5g0VwXsXTC3mXsbECQQDw6B/z\n" +
            "ytTz8a7EfaLFhCv75RMus44gEedgW6Leq9cRMhjFjJkE0aA04L3liyG91r4pLrcc\n" +
            "LPMMdkRifBCovsnt\n";
    //支付宝公钥
    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDbOMX/wajq3tamQZytszUQCGNnfdW1R32yeKrJ18w9+QsZVmOJ/We2G699/ZeoailU2VP8+WEbuPbBY0vN0FfFU/zhy2H2jRBxjUdZTkKBgW/3OmarA4RuYEyoAnKLnsp/U5rozpoBOEsYP/3vRZg5lYTG0Fwhk8un5NTnKqDrHQIDAQAB";

    private void alipayPay() {
        // 订单
        String orderInfo = getOrderInfo("测试的商品", "该测试商品的详细描述", "0.01");

        // 对订单做RSA 签名
        String sign = sign(orderInfo);
        UtilLog.d("---sing:"+sign);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

       // sign = "partner=\"2088611036448823\"&out_trade_no=\"P150826120411236395\"&subject=\"P150826120411236395\"&body=\"P150826120411236395\"&total_fee=\"0.01\"&notify_url=\"http://kkkd.xiangqu.com/pay/alipay/notify_app\"&service=\"mobile.securitypay.pay\"&_input_charset=\"UTF-8\"&payment_type=\"1\"&seller_id=\"xiangqu@ixiaopu.com\"&it_b_pay=\"3d\"&sign_type=\"RSA\"&sign=\"ixnS5x53zDrGsyhs6JOlEbu68Xs1bCQoToWtw3CmDmam2je9bc7%2BP1qeSE1fKUKcRwvUx1HXhwhDw0nMt6RMbQrvpaQ%2BiyNnFCcCOPgI%2FkbdM0dkpWjdIZDZ12AfGITdUgjbSB6TEVwy62xegve9THSjJ9NPdQlhJXfyev3Errg%3D";

        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(PaymentActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo);
                UtilLog.d("-----result:"+result);
//                Message msg = new Message();
//                msg.what = SDK_PAY_FLAG;
//                msg.obj = result;
//                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    /**
     * create the order info. 创建订单信息
     *
     */
    public String getOrderInfo(String subject, String body, String price) {
        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm"
                + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     *
     */
    public String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
                Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }


    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content
     *            待签名订单信息
     */
    public String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     *
     */
    public String getSignType() {
        return "sign_type=\"RSA\"";
    }



}

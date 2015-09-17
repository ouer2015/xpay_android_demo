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
package com.ouertech.android.sails.xpay.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ouertech.android.sails.ouer.base.constant.CstHttp;
import com.ouertech.android.sails.ouer.base.future.base.OuerFutureData;
import com.ouertech.android.sails.ouer.base.future.base.OuerFutureListener;
import com.ouertech.android.sails.ouer.base.future.core.AgnettyResult;
import com.ouertech.android.sails.ouer.base.future.http.HttpFuture;
import com.ouertech.android.sails.ouer.base.future.impl.OuerClient;
import com.ouertech.android.sails.ouer.base.utils.UtilLog;
import com.ouertech.android.sails.xpay.lib.constant.CstXPay;
import com.ouertech.android.sails.xpay.lib.data.bean.Charge;
import com.ouertech.android.sails.xpay.lib.data.bean.Credential;
import com.ouertech.android.sails.xpay.lib.data.bean.PayResult;
import com.ouertech.android.sails.xpay.lib.future.impl.XPay;
import com.xiangqu.app.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * @author : Zhenshui.Xia
 * @date : 2015/9/10.
 * @desc : XPay示例程序，仅供开发者参考
 */
public class DemoActivity extends Activity implements View.OnClickListener{
    //填写开发者申请应用的app ID
    private static final String APP_ID = "###";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xpay_activity_demo);

        //平台初始化，初始化工作必须放在平台服务调用之前，建议初始化工作放在Application里
        OuerClient.init(getApplication(), APP_ID, true, "xpay");

        //微信支付
        findViewById(R.id.xpay_id_wx).setOnClickListener(this);
        //支付宝支付
        findViewById(R.id.xpay_id_alipay).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        String channel = null;
        switch (v.getId()) {
            case R.id.xpay_id_wx:
                channel = CstXPay.CHANNEL_WX;
                break;
            case R.id.xpay_id_alipay:
                channel = CstXPay.CHANNEL_ALIPAY;
                break;
            default:
                break;
        }

        //支付
        pay(channel, "测试的商品", "该测试商品的详细描述", "0.01");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取支付结果
        PayResult result = XPay.getPayResult(requestCode, resultCode, data);
        if(result != null) {
            int status = result.getStatus(); //支付状态
            String memo = result.getMemo(); //支付状态说明
            String extra = result.getExtra();//额外信息

            switch (status) {
                case CstXPay.PAY_SUCCESS: //支付成功
                    break;
                case CstXPay.PAY_CANCELED://支付取消
                    break;
                case CstXPay.PAY_FAILED: //支付失败
                    break;
                case CstXPay.PAY_PENDING: //支付结果确认中,最终交易是否成功以服务端异步通知为准
                    break;
                case CstXPay.PAY_INVALID_CHARGE: //支付凭证格式不合法
                    break;
                case CstXPay.PAY_INVALID_PAY_CHANNEL: //支付渠道不合法
                    break;
                case CstXPay.PAY_INVALID_WX_UNINSTALLED: //微信未安装
                    break;
                case CstXPay.PAY_INVALID_WX_UNSUPPORTED: //微信版本不支持支付
                    break;
            }

            Toast.makeText(this, memo, Toast.LENGTH_SHORT).show();
        }
    }



    private void pay(String channel, String subject, String body, String price) {
        if(CstXPay.CHANNEL_WX.equals(channel)) {
            new HttpFuture.Builder(this, CstHttp.POST)
                    .setUrl("https://api.mch.weixin.qq.com/pay/unifiedorder")
                    .setHandler(OrderHandler.class)
                    .setData(new OuerFutureData(new OrderReq(), new TypeToken<Void>() {
                    }.getType()))
                    .setListener(new OuerFutureListener(this) {
                        @Override
                        public void onComplete(AgnettyResult result) {
                            super.onComplete(result);
                            String charge = (String) result.getAttach();
                            XPay.pay(DemoActivity.this, charge);
                        }
                    })
                    .execute();
        } else if(CstXPay.CHANNEL_ALIPAY.equals(channel)) {
            Charge ch = new Charge();
            ch.setChannel("alipay");
            Credential credential = new Credential();
            credential.setSign(alipayPay());
            ch.setCredential(credential);

//          String orderInfo = UtilString.utf8UrlEncode(alipayPay());
//          String charge = "{\"channel\":\"alipay\", \"credential\":{\"sign\":\""+orderInfo+"\"}}";
            String charge = new Gson().toJson(ch);
            UtilLog.d("--charge:" + charge);
            XPay.pay(this, charge);
        }
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

    private String alipayPay() {
        // 订单
        String orderInfo = getOrderInfo("测试的商品", "该测试商品的详细描述", "0.01");

        // 对订单做RSA 签名
        String sign = sign(orderInfo);
        UtilLog.d("---sing:" + sign);
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

        return payInfo;


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

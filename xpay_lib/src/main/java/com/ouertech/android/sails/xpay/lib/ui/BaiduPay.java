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
package com.ouertech.android.sails.xpay.lib.ui;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.baidu.android.pay.PayCallBack;
import com.baidu.paysdk.PayCallBackManager.PayStateModle;
import com.ouertech.android.sails.ouer.base.utils.UtilLog;
import com.ouertech.android.sails.xpay.lib.constant.CstXPay;
import com.ouertech.android.sails.xpay.lib.data.bean.Charge;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : Zhenshui.Xia
 * @since : 2015/9/15.
 * desc :百度支付
 */
public class BaiduPay extends AbsPay{
    public BaiduPay(XPayActivity activity, Charge charge) {
        super(activity, charge);
    }

    @Override
    protected void pay() {
        //test
//        String sign = createOrderInfo("XPay测试", "1", "1");
        String sign = mCharge.getCredential().getSign();

        //创建保留字段
        Map<String, String> map = new HashMap<String, String>();
        //获取百度支付实例对象执行支付
        com.baidu.paysdk.api.BaiduPay.getInstance().doPay(mActivity, sign, new PayCallBack() {
            //回调结果
            public void onPayResult(int stateCode, String payDesc) {
                UtilLog.d("stateCode=" + stateCode + "#payDesc=" + payDesc);
                handlepayResult(stateCode, payDesc);
            }

            //是否隐藏滚动条
            public boolean isHideLoadingDialog() {
                return true;
            }
        }, map);
    }

    @Override
    protected void onRestart() {

    }

    @Override
    protected void onNewIntent(Intent intent) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    /**
     * 支付结果处理
     * @param stateCode
     * @param payDesc
     */
    private void handlepayResult(int stateCode, String payDesc) {
        int status = CstXPay.PAY_SUCCESS;
        String memo = SUCCESS_PAY_RESULT;
        String attach = mCharge.getAttach();

        switch (stateCode) {
            case PayStateModle.PAY_STATUS_SUCCESS:// 需要到服务端验证支付结果
                status = CstXPay.PAY_SUCCESS;
                memo = SUCCESS_PAY_RESULT;
                break;
            case PayStateModle.PAY_STATUS_PAYING:// 需要到服务端验证支付结果
                status = CstXPay.PAY_PENDING;
                memo = PENDING_PAY_RESULT;
                break;
            case PayStateModle.PAY_STATUS_CANCEL://取消
                status = CstXPay.PAY_CANCELED;
                memo = CANCELED_PAY_RESULT;
                break;
            case PayStateModle.PAY_STATUS_NOSUPPORT://不支持该种支付方式
                status = CstXPay.PAY_INVALID;
                memo = INVALID_BAIDU_NOSUPPORT;
                break;
            case PayStateModle.PAY_STATUS_TOKEN_INVALID://无效的登陆状态
                status = CstXPay.PAY_INVALID;
                memo = INVALID_BAIDU_TOKEN_INVALID;
                break;
            case PayStateModle.PAY_STATUS_LOGIN_ERROR://登陆失败
                status = CstXPay.PAY_INVALID;
                memo = INVALID_BAIDU_LOGIN_ERROR;
                break;
            case PayStateModle.PAY_STATUS_LOGIN_OUT://退出登录
                status = CstXPay.PAY_INVALID;
                memo = INVALID_BAIDU_LOGIN_OUT;
                break;
            case PayStateModle.PAY_STATUS_ERROR://支付失败
            default:
                status = CstXPay.PAY_FAILED;
                memo = FAILED_PAY_RESULT;
                break;
        }

        setPayResult(status, memo, attach);
        UtilLog.d("status:" + status + " memo:" + memo);
    }



    /**
     * 合作商户ID
     */
    public static String PARTNER_ID = "3400000001";

    /**
     * 商家私钥，<strong>实际App中切忌采用该方式获取私钥，而需要保护好私钥，采用安全的方式获取</strong>
     */
    public static String MD5_PRIVATE = "Au88LPiP5vaN5FNABBa7NC4aQV28awRK";

    /**
     *
     * @return TODO true - 通过检测；false - 未通过检测
     */
    public static boolean isPartnerValid() {
        return !(TextUtils.isEmpty(PARTNER_ID) || TextUtils.isEmpty(MD5_PRIVATE));
    }

    /**
     * 组装订单信息
     *
     * @return TODO
     */
    private String createOrderInfo(String name, String price, String num) {
        BigDecimal bigPrice = new BigDecimal(price); // 创建BigDecimal对象
        BigDecimal bigNum = new BigDecimal(num);
        BigDecimal bigInterest = bigPrice.multiply(bigNum);
        String spUno = "";
        String spUnoParam = "";
        if (!TextUtils.isEmpty(spUno)) {
            spUnoParam = "&sp_uno=" + spUno;
        }
        if (TextUtils.isEmpty(PARTNER_ID) || TextUtils.isEmpty(MD5_PRIVATE)) {
            Toast.makeText(mActivity, "商户号和商户秘钥不能为空", Toast.LENGTH_LONG).show();
        }
        StringBuffer orderInfo = new StringBuffer("currency=1&extra=");
        String orderNo = String.valueOf(System.currentTimeMillis());
        orderInfo
                .append("&goods_desc=")
                .append(new String(getUTF8toGBKString(name)))
                .append("&goods_name=")
                .append(new String(getUTF8toGBKString(name)))
                .append("&goods_url=http://item.jd.com/736610.html")
                .append("&input_charset=1&order_create_time=20130508131702&order_no=" + orderNo
                        + "&pay_type=2&return_url=http://item.jd.com/736610.html")
                .append("&service_code=1&sign_method=1&sp_no=" + PARTNER_ID + "&sp_request_type="
                        +"" + spUnoParam + "&total_amount=" + bigInterest
                        + "&transport_amount=0&unit_amount=" + bigPrice.toString() + "&unit_count=" + bigNum.toString()
                        + "&version=2");
        System.out.println("orderInfo————>"+orderInfo);
        StringBuffer orderInfo1 = new StringBuffer("currency=1&extra=");
        try {
            orderInfo1
                    .append("&goods_desc=")
                    .append(URLEncoder.encode(name, "GBK"))
                    .append("&goods_name=")
                    .append(URLEncoder.encode(name, "GBK"))
                    .append("&goods_url=http://item.jd.com/736610.html")
                    .append("&input_charset=1&order_create_time=20130508131702&order_no=" + orderNo
                            + "&pay_type=2&return_url=http://item.jd.com/736610.html")
                    .append("&service_code=1&sign_method=1&sp_no=" + PARTNER_ID + "&sp_request_type="
                            + "" + spUnoParam + "&total_amount=" + bigInterest
                            + "&transport_amount=0&unit_amount=" + bigPrice.toString() + "&unit_count="
                            + bigNum.toString() + "&version=2");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String signed = MD5.toMD5(orderInfo.toString() + "&key=" + MD5_PRIVATE);
        System.out.println("最终要发送到的数据:"+orderInfo1+ "&sign=" + signed);
        return orderInfo1.toString() + "&sign=" + signed;
    }

    /**
     * 字符转换从UTF-8到GBK
     *
     * @param gbkStr
     * @return TODO
     */
    public static byte[] getUTF8toGBKString(String gbkStr) {
        //获取字符串长度
        int n = gbkStr.length();
        //创建字符串长度的3倍字节数组
        byte[] utfBytes = new byte[3 * n];
        //初始数组utfBytes角标
        int k = 0;
        //取字符串中的值
        for (int i = 0; i < n; i++) {
            //获取第i个字符
            int m = gbkStr.charAt(i);
            //判断字符大小
            if (m < 128 && m >= 0) {
                //将字符转成字节数组中的元素
                utfBytes[k++] = (byte) m;
                continue;
            }
            utfBytes[k++] = (byte) (0xe0 | (m >> 12));
            utfBytes[k++] = (byte) (0x80 | ((m >> 6) & 0x3f));
            utfBytes[k++] = (byte) (0x80 | (m & 0x3f));
        }
        if (k < utfBytes.length) {
            byte[] tmp = new byte[k];
            System.arraycopy(utfBytes, 0, tmp, 0, k);
            return tmp;
        }
        return utfBytes;
    }
}

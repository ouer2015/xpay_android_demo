package com.baidu.paysdk.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.baidu.android.pay.PayCallBack;
import com.baidu.paysdk.PayCallBackManager.PayStateModle;
import com.baidu.paysdk.api.BaiduPay;
import com.baidu.wallet.core.utils.LogUtil;
@SuppressWarnings("unused")
public class NativePay extends Activity implements OnClickListener {

    public static final String TAG = "EbPayDemo";
    /** 商品名称 */
    private EditText mGoodsName;
    /** 商品价格 */
    private EditText mTotalAmount;
    /** 商品描述 */
    private EditText mGoodsDesc;
    /** 商品地址 */
    private EditText mGoodsUrl;
    /** 收银台类型 */
    private EditText mCashierType;
    /** 合约用户 */
    private EditText mSpUno;
    /** 后台通知地址 */
    private EditText mReturnUrl;
    /** 分润方案，只有涉及分润的商户才需要传入改字段 */
    private EditText mProfitSolution;

    private CheckBox mCheck;
    private Button payBt;
    private ToggleButton mServerSwitch;
    ProgressDialog progressDialog;

    private static final int CREATE_ORDER = 1;

    @SuppressLint("HandlerLeak")
    private Handler mDopayHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (progressDialog != null) {
                if (progressDialog.isShowing()) {
                    progressDialog.cancel();
                }
            }
            switch (msg.what) {
            case CREATE_ORDER:
                if (msg.obj != null) {
                    if (msg.obj instanceof String) {
                        String str = (String) msg.obj;
                        if (!TextUtils.isEmpty(str) && str.contains("service_code")) {
                            realPay(str);
                        } else {
                            Toast.makeText(NativePay.this, "订单创建失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;

            default:
                break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.layout_nativepay);
        mGoodsName = (EditText) findViewById(R.id.goods_name);
        mTotalAmount = (EditText) findViewById(R.id.total_amount);
        mGoodsDesc = (EditText) findViewById(R.id.goods_desc);
        mGoodsUrl = (EditText) findViewById(R.id.goods_url);
        mReturnUrl = (EditText) findViewById(R.id.return_url);
        mProfitSolution = (EditText) findViewById(R.id.profit_solution);
        mCashierType = (EditText) findViewById(R.id.cashier_type);
        mSpUno = (EditText) findViewById(R.id.sp_uno);
        payBt = (Button) findViewById(R.id.pay);
        payBt.setOnClickListener(this);
        mCheck = (CheckBox) findViewById(R.id.is_dis);
    }

    @Override
    public void onClick(View view) {
        if (view == payBt) {
            pay();
        }
    }

    /**
     * 发起支付
     */
    private void pay() {
        final String name = mGoodsName.getText().toString();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "商品名字不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        String price = mTotalAmount.getText().toString();
        if (TextUtils.isEmpty(price)) {
            Toast.makeText(this, "商品价格不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        BigDecimal bigPrice = new BigDecimal(price);
        if (bigPrice.compareTo(new BigDecimal(0)) != 1) {
            Toast.makeText(this, "商品价格必须大于0", Toast.LENGTH_SHORT).show();
            return;
        }
        //创建订单
        String orderinfo = createOrderInfo(name, price, "1");
        realPay(orderinfo);

    }

    /**
     * 支付结果处理
     * 
     * @param stateCode
     * @param payDesc
     */
    private void handlepayResult(int stateCode, String payDesc) {
        LogUtil.logd("payDesc=" + payDesc + "#len=" + payDesc.length());
        switch (stateCode) {
        case PayStateModle.PAY_STATUS_SUCCESS:// 需要到服务端验证支付结果
            Toast.makeText(NativePay.this, "支付成功", Toast.LENGTH_SHORT).show();
            break;
        case PayStateModle.PAY_STATUS_PAYING:// 需要到服务端验证支付结果
            Toast.makeText(NativePay.this, "支付处理中", Toast.LENGTH_SHORT).show();
            break;
        case PayStateModle.PAY_STATUS_CANCEL:
            Toast.makeText(NativePay.this, "取消", Toast.LENGTH_SHORT).show();
            break;
        case PayStateModle.PAY_STATUS_NOSUPPORT:
            Toast.makeText(NativePay.this, "不支持该种支付方式", Toast.LENGTH_SHORT).show();
            break;
        case PayStateModle.PAY_STATUS_TOKEN_INVALID:
            Toast.makeText(NativePay.this, "无效的登陆状态", Toast.LENGTH_SHORT).show();
            break;
        case PayStateModle.PAY_STATUS_LOGIN_ERROR:
            Toast.makeText(NativePay.this, "登陆失败", Toast.LENGTH_SHORT).show();
            break;
        case PayStateModle.PAY_STATUS_ERROR:
            Toast.makeText(NativePay.this, "支付失败", Toast.LENGTH_SHORT).show();
            break;
        case PayStateModle.PAY_STATUS_LOGIN_OUT:
            Toast.makeText(NativePay.this, "退出登录", Toast.LENGTH_SHORT).show();
            break;
        default:
            Toast.makeText(NativePay.this, "支付失败" + stateCode, Toast.LENGTH_SHORT).show();
            break;
        }
    }

    /**
     * 组装订单信息
     * 
     * @param position
     * @return
     */
    private String createOrderInfo(String name, String price, String num) {
        BigDecimal bigPrice = new BigDecimal(price); // 创建BigDecimal对象
        BigDecimal bigNum = new BigDecimal(num);
        BigDecimal bigInterest = bigPrice.multiply(bigNum);
        String spUno = mSpUno.getText().toString();
        String spUnoParam = "";
        if (!TextUtils.isEmpty(spUno)) {
            spUnoParam = "&sp_uno=" + spUno;
        }
        if (TextUtils.isEmpty(PartnerConfig.PARTNER_ID) || TextUtils.isEmpty(PartnerConfig.MD5_PRIVATE)) {
            Toast.makeText(NativePay.this, "商户号和商户秘钥不能为空", Toast.LENGTH_LONG).show();
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
                .append("&service_code=1&sign_method=1&sp_no=" + PartnerConfig.PARTNER_ID + "&sp_request_type="
                        + mCashierType.getText().toString() + spUnoParam + "&total_amount=" + bigInterest
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
                    .append("&service_code=1&sign_method=1&sp_no=" + PartnerConfig.PARTNER_ID + "&sp_request_type="
                            + mCashierType.getText().toString() + spUnoParam + "&total_amount=" + bigInterest
                            + "&transport_amount=0&unit_amount=" + bigPrice.toString() + "&unit_count="
                            + bigNum.toString() + "&version=2");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String signed = MD5.toMD5(orderInfo.toString() + "&key=" + PartnerConfig.MD5_PRIVATE);
        System.out.println("最终要发送到的数据:"+orderInfo1+ "&sign=" + signed);
        return orderInfo1.toString() + "&sign=" + signed;
    }

    private void realPay(String orderInfo) {
    	//创建保留字段
        Map<String, String> map = new HashMap<String, String>();
        //获取百度支付实例对象执行支付
        BaiduPay.getInstance().doPay(this, orderInfo, new PayCallBack() {
        	//回调结果
            public void onPayResult(int stateCode, String payDesc) {
                // TODO Auto-generated method stub
                Log.d(TAG, "rsult=" + stateCode + "#desc=" + payDesc);
                handlepayResult(stateCode, payDesc);
            }
            //是否隐藏滚动条
            public boolean isHideLoadingDialog() {
                // TODO Auto-generated method stub
                return mCheck.isChecked();
            }
        }, map);

    }

    /**
     * 字符转换从UTF-8到GBK
     * 
     * @param gbkStr
     * @return
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
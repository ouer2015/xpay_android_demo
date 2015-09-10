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
package com.ouertech.android.sails.xpay.pay.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ouertech.android.sails.ouer.base.utils.UtilList;
import com.ouertech.android.sails.ouer.base.utils.UtilPref;
import com.ouertech.android.sails.ouer.base.utils.UtilString;
import com.ouertech.android.sails.xpay.lib.data.bean.Payment;
import com.ouertech.android.sails.xpay.pay.cache.image.SmartImageView;
import com.ouertech.android.sails.xpay.pay.cache.image.WebImage;
import com.ouertech.android.sails.xpay.pay.ui.widget.RadioButton;
import com.ouertech.android.sails.xpay.pay.utils.UtilCache;
import com.xiangqu.app.R;

import java.util.List;

/**
 * @author : Zhenshui.Xia
 * @date : 2015/9/1.
 * @desc : 支付方式适配器
 */
public class PaymentAdapter extends BaseAdapter{
    private static final String KEY_CHANNEL = "channel";

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Payment> mDatas;
    private String mChannel;
    private RadioButton mRbCurrentRadio;

    public PaymentAdapter(Context context, List<Payment> datas) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mDatas = datas;
        mChannel = UtilCache.getPaychannel(mContext);
        if(UtilList.isNotEmpty(mDatas)) {
            if(UtilString.isBlank(mChannel)) {
                mChannel = mDatas.get(0).getChannel();
                UtilCache.savePaychannel(mContext, mChannel);
            } else {
                boolean flag = false;
                for(int i=0; i<mDatas.size(); i++) {
                    if(mDatas.get(i).getChannel().equals(mChannel)) {
                        flag = true;
                        break;
                    }
                }

                if(!flag) {
                    mChannel = mDatas.get(0).getChannel();
                    UtilCache.savePaychannel(mContext, mChannel);
                }
            }
        }
    }

    @Override
    public int getCount() {
        return UtilList.getCount(mDatas);
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.xpay_layout_payment_item, null);
            holder = new ViewHolder();
            holder.mSivImg = (SmartImageView) convertView.findViewById(R.id.xpay_id_img);
            holder.mTvName = (TextView) convertView.findViewById(R.id.xpay_id_name);
            holder.mRbRadio = (RadioButton) convertView.findViewById(R.id.xpay_id_radio);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Payment data = mDatas.get(position);
        String url = data.getImgUrl();
        if(UtilString.isBlank(url)) {
            holder.mSivImg.setVisibility(View.GONE);
        } else {
            holder.mSivImg.setVisibility(View.VISIBLE);
            holder.mSivImg.setImage(new WebImage(url), R.drawable.xpay_ic_launcher);
        }

        holder.mTvName.setText(data.getName());

        final String channel = data.getChannel();

        if(getCount() == 1) {
            holder.mRbRadio.setVisibility(View.GONE);
        }

        if(mChannel.equals(channel)) {
            holder.mRbRadio.setChecked(true);
            mRbCurrentRadio = holder.mRbRadio;
        } else {
            holder.mRbRadio.setChecked(false);
        }

        holder.mRbRadio.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(!mChannel.equals(channel)) {
                    mChannel = channel;
                    UtilPref.putString(mContext, KEY_CHANNEL, channel);
                    holder.mRbRadio.setChecked(true);
                    if(mRbCurrentRadio != null
                            && !mRbCurrentRadio.equals(holder.mRbRadio)) {
                        mRbCurrentRadio.setChecked(false);
                    }

                    mRbCurrentRadio = holder.mRbRadio;
                }
            }
        });

        return convertView;
    }



    /**
     * 获取当前选中的支付渠道
     * @return
     */
    public String getChannel() {
        return mChannel;
    }

    /**
     * 刷新数据
     * @param datas
     */
    public void refresh(List<Payment> datas) {
        mDatas = datas;

        if(UtilList.isNotEmpty(mDatas)) {
            mChannel = UtilCache.getPaychannel(mContext);

            if(UtilString.isBlank(mChannel)) {
                mChannel = mDatas.get(0).getChannel();
                UtilCache.savePaychannel(mContext, mChannel);
            } else {
                boolean flag = false;
                for(int i=0; i<mDatas.size(); i++) {
                    if(mDatas.get(i).getChannel().equals(mChannel)) {
                        flag = true;
                        break;
                    }
                }

                if(!flag) {
                    mChannel = mDatas.get(0).getChannel();
                    UtilCache.savePaychannel(mContext, mChannel);
                }
            }
        } else {
            mChannel = "";
            UtilCache.savePaychannel(mContext, mChannel);
        }

        notifyDataSetChanged();
    }

    class ViewHolder {
        //支付渠道图标
        public SmartImageView mSivImg;
        //支付渠道名
        public TextView mTvName;
        //是否选中该渠道
        public RadioButton mRbRadio;
    }
}

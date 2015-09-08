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
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.ouertech.android.sails.ouer.base.utils.UtilList;
import com.ouertech.android.sails.ouer.base.utils.UtilString;
import com.ouertech.android.sails.xpay.lib.data.bean.Payment;
import com.ouertech.android.sails.xpay.pay.R;
import com.ouertech.android.sails.xpay.pay.cache.image.SmartImageView;
import com.ouertech.android.sails.xpay.pay.cache.image.WebImage;
import com.ouertech.android.sails.xpay.pay.ui.widget.sticky.StickyListHeadersAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Zhenshui.Xia
 * @date : 2015/9/1.
 * @desc : 信用卡适配器
 */
public class DepositCardAdapter extends BaseAdapter implements
        StickyListHeadersAdapter, SectionIndexer {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<Payment> mDatas;
    private int[] mSectionIndices;
    private String[] mSectionLetters;

    public DepositCardAdapter(Context context, List<Payment> datas) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mDatas = datas;
        this.mSectionIndices = getSectionIndices();
        this.mSectionLetters = getSectionLetters();
    }

    private int[] getSectionIndices() {
        if(UtilList.isEmpty(mDatas)) {
            return null;
        }

        ArrayList<Integer> sectionIndices = new ArrayList<Integer>();
        String group = mDatas.get(0).getGroup();
        sectionIndices.add(0);
        for (int i = 0; i < mDatas.size(); i++) {
            String tmp = mDatas.get(i).getGroup();
            if (!tmp.equals(group)) {
                group = tmp;
                sectionIndices.add(i);
            }
        }

        int[] sections = new int[sectionIndices.size()];
        for (int i = 0; i < sectionIndices.size(); i++) {
            sections[i] = sectionIndices.get(i);
        }

        return sections;
    }

    private String[] getSectionLetters() {
        if(UtilList.isEmpty(mDatas)) {
            return null;
        }

        String[] letters = new String[mSectionIndices.length];
        for (int i = 0; i < mSectionIndices.length; i++) {
            letters[i] = mDatas.get(mSectionIndices[i]).getGroup().substring(0, 1);
        }
        return letters;
    }

    @Override
    public int getCount() {
        return UtilList.getCount(mDatas);
    }

    @Override
    public Payment getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.xpay_layout_bank_item, null);
            holder = new ViewHolder();
            holder.mSivImg = (SmartImageView) convertView.findViewById(R.id.xpay_id_img);
            holder.mTvName = (TextView) convertView.findViewById(R.id.xpay_id_name);
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

        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;

        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = mInflater.inflate(R.layout.xpay_layout_bank_sticky_header, parent, false);
            holder.mTvName = (TextView) convertView.findViewById(R.id.xpay_id_name);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        CharSequence headerChar = mDatas.get(position).getGroup();
        holder.mTvName.setText(headerChar);

        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return mDatas.get(position).getGroup().charAt(0);
    }

    @Override
    public Object[] getSections() {
        return mSectionLetters;
    }

    @Override
    public int getPositionForSection(int section) {
        if (mSectionIndices.length == 0) {
            return 0;
        }

        if (section >= mSectionIndices.length) {
            section = mSectionIndices.length - 1;
        } else if (section < 0) {
            section = 0;
        }
        return mSectionIndices[section];
    }

    @Override
    public int getSectionForPosition(int position) {
        for (int i = 0; i < mSectionIndices.length; i++) {
            if (position < mSectionIndices[i]) {
                return i - 1;
            }
        }
        return mSectionIndices.length - 1;
    }


    /**
     * 刷新数据
     * @param datas
     */
    public void refresh(List<Payment> datas) {
        this.mDatas = datas;
        this.mSectionIndices = getSectionIndices();
        this.mSectionLetters = getSectionLetters();
        notifyDataSetChanged();
    }


    class ViewHolder {
        //支付渠道图标
        public SmartImageView mSivImg;
        //支付渠道名
        public TextView mTvName;
    }

    class HeaderViewHolder {
        TextView mTvName;
    }
}

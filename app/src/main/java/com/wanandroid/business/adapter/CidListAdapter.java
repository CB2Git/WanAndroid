package com.wanandroid.business.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wanandroid.R;
import com.wanandroid.model.entity.Cid;
import com.wanandroid.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 知识体系分类的Adapter
 */
public class CidListAdapter extends BaseAdapter {

    private Context mContext;

    private List<Cid> mCids;

    private int mCurrSelect = -1;

    public CidListAdapter(Context context) {
        mContext = context;
        mCids = new ArrayList<>();
    }

    public CidListAdapter(Context context, List<Cid> cids) {
        mContext = context;
        mCids = cids;
    }

    public void setCids(List<Cid> newCids) {
        mCurrSelect = -1;
        mCids.clear();
        mCids.addAll(newCids);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mCids == null ? 0 : mCids.size();
    }

    @Override
    public Object getItem(int position) {
        return mCids == null ? null : mCids.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setCurrSelect(int currSelect) {
        mCurrSelect = currSelect;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.dialg_classify_item, parent, false);
        }
        Cid item = (Cid) getItem(position);
        TextView tv = ViewHolder.get(convertView, R.id.classify_cid_text);
        tv.setText(item.getCidTitle());
        tv.setEnabled(mCurrSelect != position);
        return convertView;
    }
}

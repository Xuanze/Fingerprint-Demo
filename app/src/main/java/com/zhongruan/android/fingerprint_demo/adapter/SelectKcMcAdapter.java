package com.zhongruan.android.fingerprint_demo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongruan.android.fingerprint_demo.R;
import com.zhongruan.android.fingerprint_demo.db.entity.Ks_kc;

import java.util.ArrayList;
import java.util.List;

public class SelectKcMcAdapter extends BaseAdapter {
    private boolean isSelectAll;
    private boolean[] ischoiced;
    private List<Ks_kc> kcList;
    private Context mContext;
    private int temp;

    static class ViewHolder {
        TextView list_item_kckm_id;
        LinearLayout list_item_kckm_ll;

        ViewHolder() {
        }
    }

    public SelectKcMcAdapter(Context mContext, List<Ks_kc> kcList) {
        this.isSelectAll = false;
        this.temp = 0;
        this.temp = 0;
        this.mContext = mContext;
        this.kcList = new ArrayList();
        this.kcList.addAll(kcList);
        this.ischoiced = new boolean[kcList.size()];
        for (int i = 0; i < this.ischoiced.length; i++) {
            if ( kcList.get(i).getKc_extract() == "1") {
                this.ischoiced[i] = true;
                this.temp++;
            } else {
                this.ischoiced[i] = false;
            }
        }
        if (this.temp == this.ischoiced.length) {
            selectAll();
        }
    }

    public void setDataSource(List<Ks_kc> list) {
        this.temp = 0;
        this.kcList.clear();
        this.kcList.addAll(list);
        this.ischoiced = new boolean[this.kcList.size()];
        for (int i = 0; i < this.ischoiced.length; i++) {
            if ((this.kcList.get(i)).getKc_extract() == "1") {
                this.ischoiced[i] = true;
                this.temp++;
            } else {
                this.ischoiced[i] = false;
            }
        }
        if (this.temp == this.ischoiced.length) {
            selectAll();
        }
        notifyDataSetChanged();
    }

    public List<Ks_kc> getDataSource() {
        return this.kcList;
    }

    public int getCount() {
        return this.kcList.size();
    }

    public Object getItem(int position) {
        return this.kcList.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(this.mContext, R.layout.pad_adapter_select_kcmc, null);
            holder = new ViewHolder();
            holder.list_item_kckm_id = convertView.findViewById(R.id.list_item_kckm_id);
            holder.list_item_kckm_ll = convertView.findViewById(R.id.list_item_kckm_ll);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (this.kcList != null && this.kcList.size() > 0) {
            holder.list_item_kckm_id.setText((this.kcList.get(position)).getKc_no());
        }
        if (this.ischoiced[position]) {
            holder.list_item_kckm_ll.setBackgroundResource(R.drawable.selector_module_tab_arrange_levelsubject_selectcc_selected);
            holder.list_item_kckm_id.setTextColor(this.mContext.getResources().getColor(R.color.color_module_tab_arrange_levelsubject_selectcc_text_white));
        } else {
            holder.list_item_kckm_ll.setBackgroundResource(R.drawable.selector_module_tab_arrange_levelsubject_selectcc_unselected);
            holder.list_item_kckm_id.setTextColor(this.mContext.getResources().getColor(R.color.color_module_tab_arrange_levelsubject_selectcc_text_darkgray));
        }
        return convertView;
    }

    public void choiceState(int post) {
        boolean z = true;
        boolean[] zArr = this.ischoiced;
        if (this.ischoiced[post]) {
            z = false;
        }
        zArr[post] = z;
        notifyDataSetChanged();
    }

    public List<Ks_kc> getChosenKcList() {
        ArrayList<Ks_kc> kcSelectedList = new ArrayList();
        for (int i = 0; i < this.ischoiced.length; i++) {
            if (this.ischoiced[i]) {
                kcSelectedList.add(this.kcList.get(i));
            }
        }
        return kcSelectedList;
    }

    public void selectAll() {
        this.isSelectAll = !this.isSelectAll;
        for (int i = 0; i < this.ischoiced.length; i++) {
            this.ischoiced[i] = this.isSelectAll;
        }
        notifyDataSetChanged();
    }

    public void unselectAll() {
        this.isSelectAll = false;
        for (int i = 0; i < this.ischoiced.length; i++) {
            this.ischoiced[i] = this.isSelectAll;
        }
        notifyDataSetChanged();
    }

    public boolean checkSelectAll() {
        boolean selectAll = true;
        for (boolean z : this.ischoiced) {
            if (!z) {
                selectAll = false;
                break;
            }
        }
        if (selectAll) {
            this.isSelectAll = true;
        } else {
            this.isSelectAll = false;
        }
        return selectAll;
    }

    public boolean isSelectAll() {
        return this.isSelectAll;
    }
}

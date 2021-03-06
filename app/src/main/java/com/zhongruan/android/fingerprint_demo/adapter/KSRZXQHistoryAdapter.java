package com.zhongruan.android.fingerprint_demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zhongruan.android.fingerprint_demo.R;
import com.zhongruan.android.fingerprint_demo.adapter.view.KsRzGjHistory;
import com.zhongruan.android.fingerprint_demo.utils.FileUtils;

import java.io.File;
import java.util.List;

/**
 * Created by LHJ on 2018/2/2.
 */

public class KSRZXQHistoryAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<KsRzGjHistory> ksRzGjHistoryList;

    public KSRZXQHistoryAdapter(Context context, List<KsRzGjHistory> ksRzGjHistoryList) {
        this.context = context;
        this.ksRzGjHistoryList = ksRzGjHistoryList;
    }

    //  获得父项的数量
    @Override
    public int getGroupCount() {
        return ksRzGjHistoryList.size();
    }

    //  获得某个父项的子项数目
    @Override
    public int getChildrenCount(int groupPosition) {
        return ksRzGjHistoryList.get(groupPosition).getViewList().size();
    }

    //  获得某个父项
    @Override
    public Object getGroup(int groupPosition) {
        return ksRzGjHistoryList.get(groupPosition);
    }

    //  获得某个父项的某个子项
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return ksRzGjHistoryList.get(groupPosition).getViewList();
    }

    //  获得某个父项的id
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    //  获得某个父项的某个子项的id
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    //  获得父项显示的view
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        DataHolder dataHolder = null;
        if (dataHolder == null) {
            dataHolder = new DataHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.pad_adapter_listitem_rzjg, null);
            dataHolder.mListitemRzjg = convertView.findViewById(R.id.listitem_rzjg);
            dataHolder.mListitemRzjgsj = convertView.findViewById(R.id.listitem_rzjgsj);
            dataHolder.mListitemRzjgSpread = convertView.findViewById(R.id.listitem_rzjg_spread);
            convertView.setTag(dataHolder);
        } else {
            dataHolder = (DataHolder) convertView.getTag();
        }
        if (ksRzGjHistoryList.get(groupPosition).getKs_rzzt().equals("11")) {
            dataHolder.mListitemRzjg.setText("考中补充拍照");
        } else if (ksRzGjHistoryList.get(groupPosition).getKs_rzzt().equals("21")) {
            dataHolder.mListitemRzjg.setText("现场认证通过");
        } else if (ksRzGjHistoryList.get(groupPosition).getKs_rzzt().equals("22")) {
            dataHolder.mListitemRzjg.setText("现场认证不通过");
        } else if (ksRzGjHistoryList.get(groupPosition).getKs_rzzt().equals("23")) {
            dataHolder.mListitemRzjg.setText("现场认证缺考");
        }
        dataHolder.mListitemRzjgsj.setText(ksRzGjHistoryList.get(groupPosition).getKs_rztime());
        //判断是否已经打开列表
        if (isExpanded) {
            dataHolder.mListitemRzjgSpread.setBackgroundResource(R.drawable.img_module_tab_auth_statisticlist_arrow_down);
        } else {
            dataHolder.mListitemRzjgSpread.setBackgroundResource(R.drawable.img_module_tab_auth_statisticlist_arrow_up);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (viewHolder == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.pad_adapter_listitem_rzjl, null);
            viewHolder.mListitemRzjlIv = convertView.findViewById(R.id.listitem_rzjl_iv);
            viewHolder.mListitemRzjlRzfs = convertView.findViewById(R.id.listitem_rzjl_rzfs);
            viewHolder.mListitemDivider = convertView.findViewById(R.id.listitem_divider);
            viewHolder.mListitemRzjlResult = convertView.findViewById(R.id.listitem_rzjl_result);
            viewHolder.mListitemRzjlRzjlsj = convertView.findViewById(R.id.listitem_rzjl_rzjlsj);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mListitemRzjlRzjlsj.setText(ksRzGjHistoryList.get(groupPosition).getViewList().get(childPosition).getKs_rztime());
        if (ksRzGjHistoryList.get(groupPosition).getViewList().get(childPosition).getKs_rzfs().equals("8003")) {
            viewHolder.mListitemRzjlRzfs.setText("指纹比对");
            viewHolder.mListitemRzjlResult.setText("比对通过");
            Picasso.with(context).load(new File(FileUtils.getAppSavePath() + "/" + ksRzGjHistoryList.get(groupPosition).getViewList().get(childPosition).getKs_rzxppith())).into(viewHolder.mListitemRzjlIv);
        } else if (ksRzGjHistoryList.get(groupPosition).getViewList().get(childPosition).getKs_rzfs().equals("8006")) {
            viewHolder.mListitemRzjlRzfs.setText("指纹比对");
            Picasso.with(context).load(new File(FileUtils.getAppSavePath() + "/" + ksRzGjHistoryList.get(groupPosition).getViewList().get(childPosition).getKs_rzxppith())).into(viewHolder.mListitemRzjlIv);
            viewHolder.mListitemRzjlResult.setText("比对不通过");
        } else if (ksRzGjHistoryList.get(groupPosition).getViewList().get(childPosition).getKs_rzfs().equals("8007")) {
            viewHolder.mListitemRzjlRzfs.setText("补充拍照");
            Picasso.with(context).load(new File(FileUtils.getAppSavePath() + "/" + ksRzGjHistoryList.get(groupPosition).getViewList().get(childPosition).getKs_rzxppith())).into(viewHolder.mListitemRzjlIv);
            viewHolder.mListitemRzjlResult.setText("");
            viewHolder.mListitemDivider.setVisibility(View.GONE);
        } else if (ksRzGjHistoryList.get(groupPosition).getViewList().get(childPosition).getKs_rzfs().equals("8008")) {
            viewHolder.mListitemRzjlResult.setText("");
            viewHolder.mListitemRzjlRzfs.setText("缺考");
            viewHolder.mListitemDivider.setVisibility(View.INVISIBLE);
            viewHolder.mListitemRzjlIv.setVisibility(View.GONE);
        }
        return convertView;
    }

    static class DataHolder {
        TextView mListitemRzjg, mListitemRzjgsj;
        ImageView mListitemRzjgSpread;
    }

    static class ViewHolder {
        ImageView mListitemRzjlIv;
        TextView mListitemRzjlRzfs, mListitemRzjlResult, mListitemRzjlRzjlsj;
        View mListitemDivider;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

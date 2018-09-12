package com.tengbo.commonlibrary.base;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tengbo.commonlibrary.R;

import java.util.List;

public abstract class QuickAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {

    private static final int DEF_COUNT = 20;
//    private boolean mScrollIdle = true;
//
//    public void setmScrollIdle(boolean mScrollIdle) {
//        this.mScrollIdle = mScrollIdle;
//    }

    public QuickAdapter(int layoutResId, List<T> data) {
        super(layoutResId, data);
    }


    public void setEmptyView() {
        setEmptyView(getEmptyView());
    }

    public View getEmptyView() {
        return LayoutInflater.from(BaseApplication.get()).inflate(R.layout.empty_data, null);
    }

    public void clear() {
        getData().clear();
        notifyDataChanged();
    }

    public void replaceAll(List<T> elements) {
        setNewData(elements);
        if (isLoadMoreEnable() && elements.size() < DEF_COUNT) {
            loadMoreEnd();
        }
    }


    public void addAll(List<T> elements) {
        addData(elements);
    }

    public void addAll(List<T> elements, int defDataCount) {
        addData(elements);
        if (isLoadMoreEnable()) {
            loadMoreComplete();
        }
        setIsLoadMoreEnd(elements, defDataCount);
    }

    /**
     * @param isDefDataCount 每次加载更多是否是默认的数据量
     */
    public void addAll(List<T> elements, boolean isDefDataCount) {
        addData(elements);
        if (isLoadMoreEnable()) {
            loadMoreComplete();
        }
        if (isDefDataCount) {
            setIsLoadMoreEnd(elements, DEF_COUNT);
        }

    }

    private void setIsLoadMoreEnd(List<T> elements, int defCount) {
        if (elements.size() < defCount) {
            loadMoreEnd();
        }
    }

    //由于adapter里可能加入了Header,改变位置必须要要将其计算入内
    public void add(T element) {
        getData().add(element);
        //   notifyDataSetChanged();
        notifyItemChanged(getData().size() + getHeaderLayoutCount() - 1);
    }

    public void remove(T element) {
        getData().remove(element);
        notifyDataSetChanged();
    }


    public void addAllReverse(List<T> elements) {
        getData().addAll(0, elements);
        notifyDataSetChanged();
    }


    public void notifyDataChanged() {
        super.notifyDataSetChanged();
        if (getData().size() == 0) {
            onEmptyData();
        } else {
            onHasData();
        }
    }


    private void onHasData() {

    }

    private void onEmptyData() {

    }

}

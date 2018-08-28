package com.tengbo.module_order.custom.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tengbo.module_order.R;

import java.util.List;


/**
 * 自定义SpinnerPopupWindow，用于实现SpinnerTextView的下拉框
 */
public class SpinnerPopupWindow extends PopupWindow {

	private LayoutInflater mInflater;
	/**
	 * 下拉框选择列表
 	 */
	private ListView lv_spinner;
	/**
	 * 下拉列表数据，通过setList方法传入
	 */
	private List<String> list;
	private MySpinnerLvAdapter  mAdapter;
	
	public SpinnerPopupWindow(Context context, List<String> list, OnItemClickListener clickListener) {
		super(context);
		mInflater= LayoutInflater.from(context);
		this.list=list;
		init(clickListener);
	}

	public List<String> getList() {
		return list;
	}

	public void setList(List<String> list) {
		this.list = list;
	}

	private void init(OnItemClickListener clickListener){
//		View view = mInflater.inflate(R.layout.layout_lv_spinner, null);
		/**
		 * PopupWindow的方法，用于添加view
		 */
//		setContentView(view);
		/**
		 * 设置宽高
		 */
		setWidth(LayoutParams.WRAP_CONTENT);
		setHeight(LayoutParams.WRAP_CONTENT);

		setFocusable(true);
    		ColorDrawable dw = new ColorDrawable(0x00);
		setBackgroundDrawable(dw);


//		lv_spinner = (ListView) view.findViewById(R.id.lv_spinner);
		lv_spinner.setAdapter(mAdapter=new MySpinnerLvAdapter());
		lv_spinner.setOnItemClickListener(clickListener);
	}
	
	private class MySpinnerLvAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public String getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if(convertView==null){
				holder=new ViewHolder();
//				convertView=mInflater.inflate(R.layout.layout_lv_item_spinner, null);
//				holder.tv_item_spinner=(TextView) convertView.findViewById(R.id.tv_item_spinner);
				convertView.setTag(holder);
			}else{
				holder=(ViewHolder) convertView.getTag();
			}
			holder.tv_item_spinner.setText(getItem(position).toString());
			return convertView;
		}
	}

	public interface DismissListener{
		void dismiss();
	}
	private DismissListener mDismissListener;
	public void setDismissListener(DismissListener dismissListener)
	{
		this.mDismissListener = dismissListener;
	}
	@Override
	public void dismiss() {
		mDismissListener.dismiss();
		super.dismiss();
	}

	private class ViewHolder{
		private TextView tv_item_spinner;
	}
}

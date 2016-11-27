package com.ascba.rebate.adapter;

import android.content.Context;

import com.ascba.rebate.view.wheelview.AbstractWheelTextAdapter;

import java.util.List;


public abstract class BaseWheelAdapter<T> extends AbstractWheelTextAdapter {

	/** The default items length */
	public static final int DEFAULT_LENGTH = -1;

	// items
	protected List<T> mList = null;

	/**
	 * Constructor
	 * @param list the items
	 * @param length the max items length
	 */
	public BaseWheelAdapter(Context context, List<T> list, int length) {
		super(context);
		this.mList = list;
	}

	/**
	 * Contructor
	 * @param list the items
	 */
	public BaseWheelAdapter(Context context, List<T> list) {
		this(context,list, DEFAULT_LENGTH);
	}

	public T getItemData(int index){
		if (index >= 0 && index < mList.size()) {
			return mList.get(index);
		}
		return null;
	}

	@Override
	public int getItemsCount() {
		return mList.size();
	}
}

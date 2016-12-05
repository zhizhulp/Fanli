package com.ascba.rebate.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.ascba.rebate.R;
import com.ascba.rebate.adapter.PayViewAdp;
import com.ascba.rebate.handlers.OnPasswordInputFinish;


/**
 * 功能说明：分类
 * @author 作者：jarvisT
 * @date 2015-1-26 下午2:32:28
 */
@SuppressLint("CommitPrefEdits")
public class PayPopWindow implements OnDismissListener, OnClickListener {
	private PopupWindow popupWindow;
	private OnItemClickListener listener;
	private Context context;
	private String cate_id;//回调的id
	private String strPassword;     //输入的密码
	private TextView[] tvList;      //用数组保存6个TextView，为什么用数组？
	//因为就6个输入框不会变了，用数组内存申请固定空间，比List省空间（自己认为）
	private GridView gridView;    //用GrideView布局键盘，其实并不是真正的键盘，只是模拟键盘的功能
	private ArrayList<Map<String, String>> valueList;    //有人可能有疑问，为何这里不用数组了？
	//因为要用Adapter中适配，用数组不能往adapter中填充

	private ImageView imgCancel;
	private TextView tvForget;
	private int currentIndex = -1;    //用于记录当前输入密码格位置

	private LinearLayout line;
	private ImageView pic;
	private PayViewAdp adapter;
	private Intent intent;
	private View backgroundView;
	private AnimationDrawable animationDrawable;

	public PayPopWindow(final Context context, View backgroundView) {
		this.context=context;
		this.backgroundView=backgroundView;
		View view=LayoutInflater.from(context).inflate(R.layout.layout_popup_bottom, null);
		popupWindow=new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		//设置popwindow的动画效果
		popupWindow.setAnimationStyle(R.style.popWindow_anim_style);
		popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
		popupWindow.setOnDismissListener(this);// 当popWindow消失时的监听
		valueList = new ArrayList<Map<String, String>>();
		tvList = new TextView[6];
		setView();

		imgCancel = (ImageView) view.findViewById(R.id.img_cance);
		tvForget = (TextView) view.findViewById(R.id.tv_forgetPwd);

		line=(LinearLayout)view.findViewById(R.id.pay_lin);
		pic=(ImageView)view.findViewById(R.id.pay_status);

		tvList[0] = (TextView) view.findViewById(R.id.tv_pass1);
		tvList[1] = (TextView) view.findViewById(R.id.tv_pass2);
		tvList[2] = (TextView) view.findViewById(R.id.tv_pass3);
		tvList[3] = (TextView) view.findViewById(R.id.tv_pass4);
		tvList[4] = (TextView) view.findViewById(R.id.tv_pass5);
		tvList[5] = (TextView) view.findViewById(R.id.tv_pass6);

		gridView = (GridView) view.findViewById(R.id.gv_keybord);
		adapter=new PayViewAdp(context,valueList);
		gridView.setAdapter(adapter);

		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position < 11 && position != 9) {    //点击0~9按钮
					if (currentIndex >= -1 && currentIndex < 5) {      //判断输入位置————要小心数组越界
						tvList[++currentIndex].setText(valueList.get(position).get("name"));
					}
				} else {
					if (position == 11) {      //点击退格键
						if (currentIndex - 1 >= -1) {      //判断是否删除完毕————要小心数组越界
							tvList[currentIndex--].setText("");
						}
					}
				}
			}
		});
		imgCancel.setOnClickListener(this);
		tvForget.setOnClickListener(this);
	}

	public interface OnItemClickListener{
		/** 设置点击确认按钮时监听接口 */
		public void onClickOKPop();
	}

	/**设置监听*/
	public void setOnItemClickListener(OnItemClickListener listener){
		this.listener=listener;
	}


	//当popWindow消失时响应
	@Override
	public void onDismiss() {
		setBackgroundBlack(backgroundView, 1);
		popupWindow.dismiss();
	}


	/**弹窗显示的位置*/  
	public void showAsDropDown(View position){
		popupWindow.showAtLocation(position, Gravity.BOTTOM, 0,  0);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.update();
		setBackgroundBlack(backgroundView, 0);
	}


	/** 控制背景变暗 0变暗 1变亮 */
	private void setBackgroundBlack(View view, int what) {
		switch (what) {
		case 0:
			view.setVisibility(View.VISIBLE);
			break;
		case 1:
			view.setVisibility(View.GONE);
			break;
		}
	}

	public void StartAnima(){
		line.setVisibility(View.VISIBLE);
		gridView.setVisibility(View.GONE);
		// 播放逐帧动画
		animationDrawable = (AnimationDrawable) pic.getDrawable();
		animationDrawable.start();

		int duration = 0; 
		for(int i=0;i<animationDrawable.getNumberOfFrames();i++){ 
			duration += animationDrawable.getDuration(i); 
		} 
		Handler handler = new Handler(); 
		handler.postDelayed(new Runnable() { 
			public void run() { 
				//此处调用第二个动画播放方法   
				onDismiss();
//				((Activity)context).finish();
			} 
		}, duration); 
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_cance:
			onDismiss();
			break;
		case R.id.tv_forgetPwd:
			Toast.makeText(context, "前往忘记密码界面", Toast.LENGTH_SHORT).show();
		
			break;
		default:
			break;
		}
	}

	//设置监听方法，在第6位输入完成后触发
	public void setOnFinishInput(final OnPasswordInputFinish pass) {
		tvList[5].addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString().length() == 1) {
					strPassword = "";     //每次触发都要先将strPassword置空，再重新获取，避免由于输入删除再输入造成混乱
					for (int i = 0; i < 6; i++) {
						strPassword += tvList[i].getText().toString().trim();
					}
					pass.inputFinish();    //接口中要实现的方法，完成密码输入完成后的响应逻辑
				}
			}
		});
	}

	/* 获取输入的密码 */
	public String getStrPassword() {
		return strPassword;
	}


	private void setView() {
		/* 初始化按钮上应该显示的数字 */
		for (int i = 1; i < 13; i++) {
			Map<String, String> map = new HashMap<String, String>();
			if (i < 10) {
				map.put("name", String.valueOf(i));
			} else if (i == 10) {
				map.put("name", "");
			} else if (i == 11) {
				map.put("name", String.valueOf(0));
			} else if (i == 12) {
				map.put("name", "<");
			}
			valueList.add(map);
		}
	}
}

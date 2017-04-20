package com.ascba.rebate.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.ascba.rebate.activities.main_page.sweep.PushResultWorkActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";

	@Override
	public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
		Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
		
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...
                        
        }/* else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
        	Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
        	//processCustomMessage(context, bundle);
			//自定义通知栏
			createNotification(bundle,context);
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
        	
        }*/ else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
			String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
			try {
				JSONObject jObj = new JSONObject(extra);
				String from_msg_type = jObj.getString("from_msg_type");
				if("notify".equals(from_msg_type)){
					//打开自定义的Activity
					Intent i = new Intent(context, PushResultWorkActivity.class);
					i.putExtras(bundle);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
					context.startActivity(i);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}



        } /*else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
        	
        } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
        	boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
        	Log.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
        } else {
        	Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }*/
	}

	/*private void createNotification(Bundle bundle,Context context) {
		//需要获取推送中的title和content
		String title = bundle.getString(JPushInterface.EXTRA_TITLE);
		String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
		String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
		//创建通知Notification对象，并将title和message设置到通知对象当中
		//1 创建通知构造类
		Notification.Builder builder = new Notification.Builder(context);
		//2 通过builder设置通知的title content icon
		builder.setSmallIcon(R.mipmap.logo);  //设置通知的图标
		builder.setContentTitle(title == null ? "Message From JPush" : title);  //设置通知的标题
		builder.setContentText(message);  //设置通知的正文

		//获取点击通知之后需要跳转的Intent对象
		Intent messageIntent = new Intent(context, PushResultWorkActivity.class);
		messageIntent.putExtra("title", title == null ? "Message From JPush" : title);
		messageIntent.putExtra("content", message);
		messageIntent.putExtra("extra", extra);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				messageIntent, PendingIntent.FLAG_ONE_SHOT);
		builder.setContentIntent(pendingIntent);
		//3 获取Notification对象
		Notification notification = builder.getNotification();
		//notification.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" +R.raw.test));
		//4 调用系统服务通知管理器发送通知
		NotificationManager manager = (NotificationManager) context.getSystemService(
				Context.NOTIFICATION_SERVICE);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		manager.notify(100, notification);
	}*/


	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			}else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
				if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
					Log.i(TAG, "This message has no Extra data");
					continue;
				}

				try {
					JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
					Iterator<String> it =  json.keys();

					while (it.hasNext()) {
						String myKey = it.next().toString();
						sb.append("\nkey:" + key + ", value: [" +
								myKey + " - " +json.optString(myKey) + "]");
					}
				} catch (JSONException e) {
					Log.e(TAG, "Get message extra JSON error!");
				}

			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}
	
	//send msg to MainWorkActivity
	/*private void processCustomMessage(Context context, Bundle bundle) {
		if (MainWorkActivity.isForeground) {
			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			Intent msgIntent = new Intent(MainWorkActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(MainWorkActivity.KEY_MESSAGE, message);
			if (!isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					if (null != extraJson && extraJson.length() > 0) {
						msgIntent.putExtra(MainWorkActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			context.sendBroadcast(msgIntent);
		}
	}*/

	public static boolean isEmpty(String s) {
		if (null == s)
			return true;
		if (s.length() == 0)
			return true;
		if (s.trim().length() == 0)
			return true;
		return false;
	}
}

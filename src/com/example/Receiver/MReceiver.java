package com.example.Receiver;

import com.example.activity.MainActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Message;
import android.widget.Toast;
/**
 * ר��Ϊ��ҳ�����Ĺ㲥������
 * @author dell
 *
 */
public class MReceiver extends BroadcastReceiver {
	private ConnectivityManager connectivityManager;
	private NetworkInfo info;
	private MainActivity mActivity;

	public MReceiver(MainActivity mActivity) {
		super();
		this.mActivity = mActivity;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
			connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			info = connectivityManager.getActiveNetworkInfo();
			if (info != null && info.isAvailable()) {
				// �������
				Message msg = new Message();
				msg.what = 1;
				// ����UI�̣߳�������������
				mActivity.handler.handleMessage(msg);

			} else { // ���粻����
				Toast.makeText(context, "��������", 0).show();

			}
		}
	}
}
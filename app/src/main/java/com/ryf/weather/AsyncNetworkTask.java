package com.ryf.weather;

import com.ds.tire.R;
import com.ds.tire.util.DialogUtils;
import com.ds.tire.util.NetworkUtils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
//import com.ouc.sei.lorry.R;
//import com.ouc.sei.lorry.util.DialogUtils;
//import com.ouc.sei.lorry.util.NetworkUtils;

public abstract class AsyncNetworkTask<T> extends AsyncTask<Void, Void, T> {
	private Context context = null;
	private boolean error = false;
	private Dialog progressDialog = null;
	private boolean showNetworkSetting = true;
	String hintText;

	public AsyncNetworkTask(Context context) {

		this.context = context;
	}

	public Context getContext() {
		return context;
	}

	public boolean isError() {
		return error;
	}

	public void hideProgressDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
			progressDialog=null;
		}

	}

	public void showProgressDialog(String message) {
		hintText = message;
	}

	private void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new Dialog(context, R.style.MyDialogStyle);
			View view = LayoutInflater.from(context).inflate(
					R.layout.progress_bar, null);
			TextView hint = (TextView) view.findViewById(R.id.loading);
			hint.setText(hintText);
			progressDialog.setContentView(view);
			// progressDialog.setMessage(message);
			progressDialog.setCancelable(true);
			progressDialog
					.setOnCancelListener(new DialogInterface.OnCancelListener() {
						@Override
						public void onCancel(DialogInterface dialog) {
							AsyncNetworkTask.this.cancel(true);
						}
					});
			if (!progressDialog.isShowing())
				progressDialog.show();
		} else {
			TextView hint = (TextView) progressDialog
					.findViewById(R.id.loading);
			hint.setText(hintText);
		}
	}

	public void showNetworkSetting(boolean enable) {
		showNetworkSetting = enable;
	}

	/**
	 * 执行异步任务，注意该函数只能调用�?�?
	 */
	public final void execute() {
		super.execute();
	}

	@Override
	protected void onPreExecute() {
		if (NetworkUtils.isConnected(context)) {
			error = false;
			if (!TextUtils.isEmpty(hintText)) {
				showProgressDialog();
			}
		} else {
			if (showNetworkSetting) {
				int icon = R.drawable.ic_launcher;
				int title = R.string.network_dialog_title;
				int message = R.string.network_dialog_message;
				int positive = R.string.ok;
				int negative = R.string.cancel;
				ClickListener l = new ClickListener();
				DialogUtils.alert(context, icon, title, message, positive, l,
						negative, l);
			}

			error = true;
		}

		Log.d("TAG", "error=" + error);
	}

	@Override
	protected T doInBackground(Void... params) {
		return error ? null : doNetworkTask();
	}

	@Override
	protected void onPostExecute(T result) {

		hideProgressDialog();
		handleResult(result);

	}

	/**
	 * 执行网络任务
	 * 
	 * @return 网络任务结果
	 */
	protected abstract T doNetworkTask();

	/**
	 * 处理网络任务结果
	 * 
	 * @param result
	 *            网络任务结果
	 */
	protected abstract void handleResult(T result);

	class ClickListener implements DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {

			switch (which) {
			case Dialog.BUTTON_POSITIVE:
				setNetwork();
				break;
			case Dialog.BUTTON_NEGATIVE:
				break;
			default:
				break;
			}

			Log.d("WebService", "which=" + which);
		}
	}

	private void setNetwork() {
		context.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
	}
}

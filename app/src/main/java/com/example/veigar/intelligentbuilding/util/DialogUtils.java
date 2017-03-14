package com.example.veigar.intelligentbuilding.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;

/**
 * 对话框工具类
 * @author Administrator
 *
 */
public class DialogUtils {

	/**
	 * 显示对话框
	 * @param context
	 * @param arrayRes
	 * @param listener
	 * @return
	 */
	public static AlertDialog showDialog(Context context, int arrayRes, OnClickListener listener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setItems(arrayRes, listener);
		return builder.show();
	}

	/**
	 * 显示对话框
	 * @param context
	 * @param message 对话框消息
	 * @param sureText 确定文本
	 * @param cancelText 取消文本
	 * @param sureListener 确定监听器
	 * @param cancelListener 取消监听器
	 * @param cancelable 是否可取消
	 * @return
	 */
	public static AlertDialog showDialog(Context context, int message, int sureText,
																int cancelText, OnClickListener sureListener,
																OnClickListener cancelListener, boolean cancelable){
		AlertDialog dialog = new AlertDialog.Builder(context).setTitle("提示").
		setMessage(message).setPositiveButton(sureText,
		sureListener).setNegativeButton(cancelText, cancelListener).
		setCancelable(cancelable).show();
		return dialog;
	}

	/**
	 * 显示对话框
	 * @param context
	 * @param title
	 * @param view
     * @return
     */
	public static AlertDialog showDialog(Context context, int title, View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setView(view);
		return builder.show();
	}

	/**
	 * 显示进度对话框
	 * @param context
	 * @param message
	 * @return
	 */
	public static ProgressDialog showProgressDialog(Context context, int message){
		ProgressDialog progressDialog = new ProgressDialog(context);
		progressDialog.setMessage(context.getString(message));
		progressDialog.show();
		return progressDialog;
	}

}

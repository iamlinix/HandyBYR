package com.lin.handybyr;

import java.lang.reflect.Method;

import com.lin.Controller.ParamPair;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class alertDlg {
	
	public static void showRawPositiveAlertWindow(Context context, String title, String message, 
			String button, DialogInterface.OnClickListener listener) {
		new AlertDialog.Builder(context).setTitle(title).setMessage(message).
		setPositiveButton(button, listener).show();
	}
	
	public static void showRawNegativeAlertWindow(Context context, String title, String message, 
			String button, DialogInterface.OnClickListener listener) {
		new AlertDialog.Builder(context).setTitle(title).setMessage(message).
		setNegativeButton(button, listener).show();
	}
	
	public static void showRawAlertWindow(Context context, String title, String message, 
			String posButton, DialogInterface.OnClickListener posListener, String negButton,
			DialogInterface.OnClickListener negListener) {
		new AlertDialog.Builder(context).setTitle(title).setMessage(message).
		setPositiveButton(posButton, posListener).setNegativeButton(negButton, negListener).
		show();
	}
	
	public static void showDesignedAlertWindow(Context context, String title, View view, 
			String posButton, DialogInterface.OnClickListener posListener, String negButton,
			DialogInterface.OnClickListener negListener) {
		new AlertDialog.Builder(context).setTitle(title).setView(view).
		setPositiveButton(posButton, posListener).setNegativeButton(negButton, negListener).
		show();
	}
	
	public static void showDesignedAlertWindowWithTextFields(Context context, String title, 
			View view, String posButton, DialogInterface.OnClickListener posListener, 
			String negButton, DialogInterface.OnClickListener negListener, Bundle bundle) {
		new AlertDialog.Builder(context).setTitle(title).setView(view).
		setPositiveButton(posButton, posListener).setNegativeButton(negButton, negListener).
		show();
		if(bundle != null) {
			for(int i = 0; i < bundle.size(); i ++) {
				ParamPair pp = (ParamPair)bundle.getSerializable(String.valueOf(i));
				try {
					Class<?> cls = Class.forName(pp.getClassName());
					Object obj = view.findViewById(pp.getId());
					Method method = cls.getMethod("setText", new Class[] {CharSequence.class});
					method.invoke(obj, pp.getValue());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}
}

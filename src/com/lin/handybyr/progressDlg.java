package com.lin.handybyr;

import android.app.ProgressDialog;
import android.content.Context;

public class progressDlg {

	private static ProgressDialog progress;
	
    public static void showProgressDialog(Context context) {
    	progress = ProgressDialog.show(context, 
    			context.getString(R.string.progressDlgTitle), 
    			context.getString(R.string.progressMessage));
    }
    
    public static void dismissProgresDialog() {
    	if(progress != null && progress.isShowing()) {
    		progress.dismiss();
    	}
    }
}

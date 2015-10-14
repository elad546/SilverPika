package com.elad546.notifyme;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class NotificationInfo extends NotificationListenerService {
	public void onCreate() {
		super.onCreate();
	}

	//When notification is posted, it filters it base on what we want to get
	@Override
	public void onNotificationPosted(StatusBarNotification sbn) {
		// TODO Auto-generated method stub
		// this.t.setText("here");
		// Log.i(this.getClass().getSimpleName(), sbn.getPackageName());
		Log.i("notifyme", sbn.getPackageName());
		Intent i = new Intent("com.elad546.notifyme");
		i.putExtra("isNotification", true);
		if (sbn.getPackageName().equals("com.whatsapp")) {
			i.putExtra("data", "oc114189100v11011000");
		} else if (sbn.getPackageName().equals("com.android.phone")) {
//			i.putExtra("data", "oc039038100v10100000");
		} else {
			i.putExtra("data", "oc000111111v10100000");
		}
		sendBroadcast(i);
	}

	@Override
	public void onNotificationRemoved(StatusBarNotification sbn) {
		// TODO Auto-generated method stub
		// Intent i = new Intent("com.elad546.notifyme");
		// if (sbn.getPackageName().equals("com.whatsapp")) {
		// i.putExtra("data", "ic114189100v11011000");
		// sendBroadcast(i);
		// } else if (sbn.getPackageName().equals("com.android.phone")) {
		// i.putExtra("data", "ic039038100v10100000");
		// }
	}
}

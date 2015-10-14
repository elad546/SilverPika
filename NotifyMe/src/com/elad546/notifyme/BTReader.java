package com.elad546.notifyme;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.util.Log;

//loop for reading incoming messages from the bracelet
public class BTReader extends Thread {
	private InputStream is;
	private Activity act;

	public BTReader(BluetoothSocket sock, Activity a) {
		this.act = a;
		try {
			this.is = sock.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {

//		this.act.runOnUiThread(new Runnable() {
//
//			@Override
//			public void run() {
				byte[] buff = new byte[1024];
				String s;
				boolean flag = false;
				while (true) {
					try {
						s = "";
						while (is.available() > 0) {
							flag = true;
							is.read(buff);
						}
						if (flag) {
							s = Character.toString((char) buff[0])
									+ Character.toString((char) buff[1])
									+ Character.toString((char) buff[2]);
							Log.i("btread", s);
							Intent i = new Intent("com.elad546.notifyme");
							i.putExtra("isCommand", true);
							i.putExtra("command", s);
							flag = false;
							act.sendBroadcast(i);
						}
						Thread.sleep(1000);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
//			}
//		});
	}
}

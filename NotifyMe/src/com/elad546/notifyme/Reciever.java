package com.elad546.notifyme;

import java.io.IOException;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

//Take care on every action that is commanded
public class Reciever extends BroadcastReceiver {
	private TextView txt;
	private BluetoothConn bt;
	private ImageView iv;
	private Activity act;

	//Constructor
	public Reciever(TextView t, BluetoothConn btConn, ImageView i, Activity a) {
		this.txt = t;
		this.txt.setText("start");
		this.bt = btConn;
		this.iv = i;
		this.act = a;
		CallInfo ci = new CallInfo(this.act);
		TelephonyManager tm = (TelephonyManager) this.act
				.getSystemService(Context.TELEPHONY_SERVICE);
		tm.listen(ci, PhoneStateListener.LISTEN_CALL_STATE);
	}

	//The care taking itself
	@TargetApi(Build.VERSION_CODES.KITKAT)
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Bundle extras = intent.getExtras();
		Log.i("notifyme", "rec");
		AudioManager audioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		if (extras.getBoolean("isCall")) {					//check if it is
															//a call command
			try {
				switch (extras.getString("state")) {
				case "incoming":

					break;
				case "answer":

					break;
				case "reject":

					break;
				case "ring":								//call state
					audioManager.setSpeakerphoneOn(true);
					bt.send("cc050255255v10110010");
					break;
				case "rang":								//no call state
					audioManager.setSpeakerphoneOn(false);
					bt.send("sc050255255v10110010");
					break;
				default:
					break;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (extras.getBoolean("isCommand")) {		//check if it is
															//a input command
															//from the bracelet
			String input = extras.getString("command");
			Log.i("notifyme", extras.getString("command"));
			switch (input.charAt(1)) {
			case '0':										//music control
				switch (input.charAt(0)) {
				case 'b':
					Functions.play(this.act);
					break;
				case 'r':
					// Functions.next(this.act);
					break;
				case 'l':
					// Functions.previous(this.act);
					break;
				case 'u':
					Functions.volumeUp(this.act);
					break;
				case 'd':
					Functions.volumeDown(this.act);
					break;
				case 'a':									//answer call
					Functions.answer(context);
					break;
				case 's':									//reject call
					Functions.reject(context);
					break;
				default:
					break;
				}
				break;

			case '1':										//create call
				switch (input.charAt(0)) {
				case 'b':
					switch (input.charAt(2)) {
					case '1':
						Functions.call(this.act, "0528424176");
						break;
					case '2':
						Functions.call(this.act, "0528479820");
						break;
					default:
						break;
					}
				default:
					break;
				}
			default:
				break;
			}
		} else if (extras.getBoolean("isNotification")) {	//notification posted
			this.txt.setText(this.txt.getText() + "1");
			try {
				bt.send(extras.getString("data"));
				Log.i("notifyme", extras.getString("data"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	//Create bitmap from an image
	Bitmap convert(Bitmap bm) {
		Bitmap newBM = Bitmap.createScaledBitmap(bm, 16, 16, true);
		newBM = Bitmap.createBitmap(16, 16, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(newBM);
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		Paint p = new Paint();
		p.setColorFilter(new ColorMatrixColorFilter(cm));
		c.drawBitmap(bm, 0, 0, p);
		return newBM;
	}
}

package com.elad546.notifyme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.view.KeyEvent;

//Static functions which are called base on the input from the bracelet
public class Functions {
	
	//Play or pause the music base on the current situation
	public static void play(Activity a) {
		AudioManager am = (AudioManager) a
				.getSystemService(Context.AUDIO_SERVICE);
		Intent i = new Intent("com.android.music.musicservicecommand");
		if (am.isMusicActive()) {
			i.putExtra("command", "pause");
		} else {
			i.putExtra("command", "play");
		}
		a.sendBroadcast(i);
	}

	//Next song
	public static void next(Activity a) {
		AudioManager am = (AudioManager) a
				.getSystemService(Context.AUDIO_SERVICE);
		Intent i = new Intent("com.android.music.musicservicecommand");
		i.putExtra("command", "next");
		a.sendBroadcast(i);
	}

	//Previous song
	public static void previous(Activity a) {
		AudioManager am = (AudioManager) a
				.getSystemService(Context.AUDIO_SERVICE);
		Intent i = new Intent("com.android.music.musicservicecommand");
		i.putExtra("command", "previous");
		a.sendBroadcast(i);
	}

	//Media volume up
	public static void volumeUp(Activity a) {
		AudioManager am = (AudioManager) a
				.getSystemService(Context.AUDIO_SERVICE);
		am.setStreamVolume(AudioManager.STREAM_MUSIC,
				am.getStreamVolume(AudioManager.STREAM_MUSIC) + 1,
				AudioManager.FLAG_SHOW_UI);
	}

	//Media volume down
	public static void volumeDown(Activity a) {
		AudioManager am = (AudioManager) a
				.getSystemService(Context.AUDIO_SERVICE);
		am.setStreamVolume(AudioManager.STREAM_MUSIC,
				am.getStreamVolume(AudioManager.STREAM_MUSIC) - 1,
				AudioManager.FLAG_SHOW_UI);
	}

	//Answer incoming call and hang up current call
	public static void answer(Context context) {
		Intent buttonUp = new Intent(Intent.ACTION_MEDIA_BUTTON);
		buttonUp.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(
				KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
		context.sendOrderedBroadcast(buttonUp,
				"android.permission.CALL_PRIVILEGED");
	}
	
	//Reject incoming call
	public static void reject(Context context) {
		Intent buttonDown = new Intent(Intent.ACTION_MEDIA_BUTTON);
		buttonDown
				.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(
						KeyEvent.ACTION_DOWN,
						KeyEvent.KEYCODE_HEADSETHOOK));
		context.sendOrderedBroadcast(buttonDown,
				"android.permission.CALL_PRIVILEGED");
	}
	
	//Calls the number described in the parameters
	public static void call(Activity act, String num) {
		Intent intent = new Intent(Intent.ACTION_CALL);

		intent.setData(Uri.parse("tel:" + num));
		act.startActivity(intent);
	}
	
	//Send settings to the bracelet(the bracelet still dynamic in his actions)
	public static void setSettings(BluetoothConn bt) {

	}
}

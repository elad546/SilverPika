package com.elad546.notifyme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

//listener for phone state(if is ringing or idle)
public class CallInfo extends PhoneStateListener {
	private Activity act;
	private boolean rang;

	public CallInfo() {
		
	}
	
	public CallInfo(Activity a) {
		this.act = a;
		this.rang = false;
	}

	@Override
	public void onCallStateChanged(int state, String incomingNumber) {
		Intent i = new Intent("com.elad546.notifyme");
		i.putExtra("isCall", true);
		i.putExtra("cmd", "incoming");
		i.putExtra("isCommand", false);
		switch (state) {
		case TelephonyManager.CALL_STATE_RINGING:
			this.rang = true;
			i.putExtra("state", "ring");
			this.act.sendBroadcast(i);
			break;
		case TelephonyManager.CALL_STATE_IDLE:
			if (this.rang) {
				this.rang = false;
				i.putExtra("state", "rang");
				this.act.sendBroadcast(i);
				break;
			}
		default:
			break;
		}
	}

}

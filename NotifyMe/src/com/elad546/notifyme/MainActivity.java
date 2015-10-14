package com.elad546.notifyme;

import java.io.IOException;
import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
	private Reciever recv;
	private TextView tv;
	private Button btn;
	private NotificationInfo ni;
	private BluetoothConn bt;
	private ImageView iv;
	public static String COMM = "com.elad546.notifyme";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.tv = (TextView)findViewById(R.id.textView1);
		this.btn = (Button)findViewById(R.id.button1);
		this.bt = new BluetoothConn(this);
		this.bt.check();
		this.ni = new NotificationInfo();
		this.iv = (ImageView)findViewById(R.id.imageView1);
		this.recv = new Reciever(this.tv, this.bt, this.iv, this);
		
		//Starts Bluetooth connection
		if(this.bt.connect()) {
			try {
				this.bt.intialize();
				this.bt.read();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.elad546.notifyme");
		registerReceiver(this.recv, filter);
		
		Log.i(this.getClass().getSimpleName(), "try");
		this.btn.setOnClickListener(new View.OnClickListener() {
			
			//Check if there is connection between the phone and the bracelet
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					Calendar c = Calendar.getInstance();
					int hour = c.get(Calendar.HOUR);
					String time = Integer.toString(hour);
					if(hour < 10) {
						time = "0" + time;
					}
					int minuite = c.get(Calendar.MINUTE);
					if(minuite < 10) {
						time += "0";
					}
					time += Integer.toString(minuite);
					bt.send("oc255000000v10110110");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

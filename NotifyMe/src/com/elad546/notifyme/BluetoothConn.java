package com.elad546.notifyme;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;

//Manage the bluetooth connection
public class BluetoothConn {
	final static int REQUEST_ENABLE_BT = 1;
	private Activity act;
	private BluetoothAdapter bta;
//	private BluetoothManager btm;
	private BluetoothDevice btd;
	private BluetoothSocket sock;
	private OutputStream os;
//	public boolean isConnected;

	
	public BluetoothConn(Activity a) {
		this.act = a;
		this.bta = BluetoothAdapter.getDefaultAdapter();
//		this.btm = (BluetoothManager) this.act
//				.getSystemService(Context.BLUETOOTH_SERVICE);
	}

	//Check support and enabled bluetooth module
	public boolean check() {
		if (this.bta == null) {
			return false;
		}
		if (!this.bta.isEnabled()) {
			Intent ebi = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			this.act.startActivityForResult(ebi, REQUEST_ENABLE_BT);
		}
		return true;
	}

	//Connects the bracelet
	public boolean connect() {
		Set<BluetoothDevice> pairs = bta.getBondedDevices();
		if (pairs.size() > 0) {
			for (BluetoothDevice device : pairs) {
				if (device.getName().equals("HC-05")) {
					this.btd = device;
					break;
				}
			}
		} else {
			return false;
		}
		return true;
	}
	
	//Intialize the connection
	public void intialize() throws IOException {
		UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
//		this.sock = this.btd.createRfcommSocketToServiceRecord(uuid);
		this.sock = this.btd.createInsecureRfcommSocketToServiceRecord(uuid);
		this.sock.connect();
		this.os = this.sock.getOutputStream();
	}
	
	//Checks if is connected
	public boolean isConnected() {
		return this.sock.isConnected();
	}
	
	//Send data to the bracelet
	public void send(String msg) throws IOException {
		if(!this.isConnected()) {
			this.connect();
		}
		if(this.isConnected())
			this.os.write(msg.getBytes());
	}
	
	//Start thread for data read
	public void read() {
		BTReader btr = new BTReader(this.sock, this.act);
		btr.start();
	}
	
}

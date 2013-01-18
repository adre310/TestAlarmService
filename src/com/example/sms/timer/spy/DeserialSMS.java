package com.example.sms.timer.spy;

import org.json.JSONObject;

import adre310.x10.mx.Serialization.Json.AbstractDeserialization;
import android.content.Context;
import android.util.Log;

public class DeserialSMS  extends AbstractDeserialization {
	private final Context context;

	public DeserialSMS(Context context) {
		this.context=context;
	}
	
	@Override
	protected void handleJsonRequest(JSONObject request) throws Exception {
		long last=request.getLong("id");
		Log.i(this.getClass().getName(),"id: "+Long.toString(last));
		
	}

}

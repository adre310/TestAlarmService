package com.example.sms.timer.spy;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class MyDBProvider  extends ContentProvider {
    public static final String CONTENT_AUTHORITY = "com.example.sms.timer.spy";
    public static final Uri CONTENT_URI=Uri.parse("content://"+CONTENT_AUTHORITY);
    public static final Uri CONTENT_URI_MY=CONTENT_URI.buildUpon().appendPath("my").build();
    
    public static final String CONTENT_TYPE =
            "vnd.android.cursor.dir/vnd.acra_sms.sms";
    public static final String CONTENT_ITEM_TYPE =
            "vnd.android.cursor.item/vnd.acra_sms.sms";

    private static final int ID_MY=1; 
    private static final int ID_MY_ITEM=2; 
    
    private static final UriMatcher mUriMatcher;
	private MyDB mOpenHelper;

	static {
    	mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    	mUriMatcher.addURI(CONTENT_AUTHORITY, "my", ID_MY);
    	mUriMatcher.addURI(CONTENT_AUTHORITY, "my/#", ID_MY_ITEM);		
	}
	
	@Override
	public boolean onCreate() {
		final Context context=getContext();
		mOpenHelper=new MyDB(context);
		return false;
	}

	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		final int match=mUriMatcher.match(uri);
		
		switch(match) {
		case ID_MY: 
	    	return CONTENT_TYPE;
		case ID_MY_ITEM: 
	    	return CONTENT_ITEM_TYPE;
		};
		
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
        final int match = mUriMatcher.match(uri);
        SQLiteDatabase database = mOpenHelper.getWritableDatabase();
        long rowId=-1;
		switch(match) {
		case ID_MY:
        	rowId=database.insert("sms", null, values);
			break;
        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
		}        

		// If the insert succeeded, the row ID exists.
        if (rowId > 0) {
            Uri retUri = uri.buildUpon().appendPath(Long.toString(rowId)).build();
            getContext().getContentResolver().notifyChange(retUri, null);
            return retUri;
        }

        throw new SQLException("Failed to insert row into " + uri);
	}


	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		final int match=mUriMatcher.match(uri);
        final SQLiteDatabase database = mOpenHelper.getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		
		switch(match) {
		case ID_MY:
			qb.setTables("sms");
	    	return qb.query(database, projection, selection, selectionArgs, null, null, sortOrder);
		case ID_MY_ITEM: 
			qb.setTables("sms");
	    	return qb.query(database, projection, selection, selectionArgs, null, null, sortOrder);
		};
		
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}

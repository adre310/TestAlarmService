package adre310.x10.mx.client;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerPNames;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.AbstractHttpMessage;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import adre310.x10.mx.Serialization.IDeserialization;
import adre310.x10.mx.Serialization.ISerialization;
import android.content.Context;
import android.util.Base64;
import android.util.Log;

public class RestClient {
	private final Context mContext;
	private final String mUsername;
	private final String mPassword;
	private final Boolean mEnable;

	public static String AGENT_VERSION=null;
	public static String AGENT_NAME=null;
	
	public static final String BASE_URL=
			"https://money2013.net" 
			//"https://172.26.10.23:9443/Money2013/web/app_dev.php"
			//"https://192.168.0.104/Money2013/web/app_dev.php"
			;
	
	public RestClient(Context context) {
		mContext=context;				
		mUsername="";
		mPassword="";
		mEnable=false;
		init();
	}
	
	public RestClient(Context context, String username,String password) {
		mContext=context;				
		mUsername=username;
		mPassword=password;
		mEnable=true;
		init();
	}

	private void init() {		
		AGENT_NAME="sms";
		AGENT_VERSION="1";
	}
	
	protected void beforeRequest(AbstractHttpMessage request) {
		
	}

	
	public void postMethod(String url,ISerialization inData, IDeserialization outData) throws Exception {
		DefaultHttpClient client=getHttpClient();
		
		Log.d(this.getClass().getName(),"postMethod '"+BASE_URL+url+"'");
		
		HttpPost request=new HttpPost(BASE_URL+url);

		request.addHeader("User-Agent", "Rest Client v"+AGENT_VERSION+" ("+AGENT_NAME+")");
		request.addHeader("Content-Type", "application/json");

		if(mEnable) {
			request.addHeader("Authorization", "Basic "+Base64.encodeToString((mUsername+":"+mPassword).getBytes(),Base64.NO_WRAP));
		}

		beforeRequest(request);
		request.setEntity(inData.Serialization());
		
		HttpResponse response=client.execute(request);
        Log.i(this.getClass().getName(), "Status: "+response.getStatusLine().getStatusCode()+" "+response.getStatusLine().getReasonPhrase());
        for (Header h : response.getAllHeaders()) {
			Log.i(this.getClass().getName(),"Header: "+h.getName()+" "+h.getValue());
		}
		
        outData.Deserialization(response.getEntity());
	}
	
	private DefaultHttpClient getHttpClient() {
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		schemeRegistry.register(new Scheme("https", new EasySSLSocketFactory(), 443));
		 
		HttpParams params = new BasicHttpParams();
		params.setParameter(ConnManagerPNames.MAX_TOTAL_CONNECTIONS, 30);
		params.setParameter(ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE, new ConnPerRouteBean(30));
		params.setParameter(HttpProtocolParams.USE_EXPECT_CONTINUE, false);
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		 
		ClientConnectionManager cm = new SingleClientConnManager(params, schemeRegistry);
		return new DefaultHttpClient(cm, params);		
	}
	
}

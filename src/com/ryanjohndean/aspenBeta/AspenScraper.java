package com.ryanjohndean.aspenBeta;

import java.io.IOException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.ProgressDialog;
import android.os.AsyncTask;

public class AspenScraper extends AsyncTask{
	private Document doc2;
	private String urlLogin, urlHome, myUsername, myPassword,
					sessionID, deploymentID, serverID, data;
	private ProgressDialog dialog;
	
	public AspenScraper(ProgressDialog pdialog) throws Exception {
		dialog = pdialog;
		urlLogin = "https://fp.hcpss.org/aspen/logon.do";
		urlHome = "https://fp.hcpss.org/aspen/portalClassList.do?"
				+ "navkey=academics.classes.list";
		sessionID = "";
		deploymentID = "x2sis";
		serverID = "";
		data = "";
	}
	
	@Override
	protected void onPreExecute() {
		dialog.show();
		super.onPreExecute();
	}
	
	
	@Override
	protected Object doInBackground(Object... params) {
		myUsername = (String) params[0];
		myPassword = (String) params[1];
		
		
		
		
		
		try {
			if (connect())
				return true;
		} catch (Exception e) {
			data += e.toString() + "\n\n";
			e.printStackTrace();
		}
		
		return false;
	}
	
	private boolean connect() throws Exception {
		setTrustAllCerts();
		Connection.Response res = Jsoup.connect(urlLogin)
				.timeout(0)
				.data("username", myUsername)
				.data("password", myPassword)
				.data("userEvent", "930")
				.data("deploymentId", "x2sis")
				.method(Method.POST)
				.execute();
		
		sessionID = res.cookie("JSESSIONID");
		serverID = res.cookie("BIGipServerAspen_pool");
		
		doc2 = Jsoup.connect(urlHome).cookie("JSESSIONID", sessionID)
				.cookie("BIGipServerAspen_pool", serverID)
				.ignoreHttpErrors(true).get();
		
		return true;
	}
	
	public Document getData() {
		return doc2;
	}
	
	
	private static void setTrustAllCerts() throws Exception

	{
		TrustManager[] trustAllCerts = new TrustManager[]{
			new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}
				public void checkClientTrusted( java.security.cert.X509Certificate[] certs, String authType ) {	}
				public void checkServerTrusted( java.security.cert.X509Certificate[] certs, String authType ) {	}
			}
		};


		try {
			SSLContext sc = SSLContext.getInstance( "SSL" );
			sc.init( null, trustAllCerts, new java.security.SecureRandom() );
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			HttpsURLConnection.setDefaultHostnameVerifier( 
				new HostnameVerifier() {
					public boolean verify(String urlHostName, SSLSession session) {
						return true;
					}
				});
		}
		catch ( Exception e ) {

			e.printStackTrace();
		}
	}

	


}


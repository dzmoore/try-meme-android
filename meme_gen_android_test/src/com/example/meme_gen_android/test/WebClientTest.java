package com.example.meme_gen_android.test;

import android.os.Build;
import android.test.AndroidTestCase;
import android.util.Log;

import com.eastapps.meme_gen_android.http.WebClient;
import com.eastapps.meme_gen_android.json.JSONObject;
import com.eastapps.meme_gen_android.util.Constants;
import com.eastapps.meme_gen_server.domain.Meme;
import com.eastapps.meme_gen_server.domain.MemeText;
import com.eastapps.meme_gen_server.domain.ShallowMeme;
import com.eastapps.util.Conca;

public class WebClientTest extends AndroidTestCase {
	private static final String ADDR = "http://192.168.1.20:8080/meme_gen_server/spring";
	private static final int USER_ID = 1;
	private static final int LV_MEME_TYPE_ID = 1;
	private static final int MEME_BACKGROUND_ID = 1;
	
	private WebClient webClient;
	
	@Override
	public void setUp() {
		try {
			super.setUp();
			
			disableConnectionReuseIfNecessary();
			
		} catch (Exception e) { }
		
		webClient = new WebClient();
	}
	
	private void disableConnectionReuseIfNecessary() {
	    // HTTP connection reuse which was buggy pre-froyo
//	    if (Integer.parseInt(Build.VERSION.SDK) < Build.VERSION_CODES.FROYO) {
//	        System.setProperty("http.keepAlive", "false");
//	    }
	}
	
	@Override
	public void tearDown() {
		try {
			super.tearDown();
		} catch (Exception e) { }
	}
	
	
	public void testGetJsonObjWithJsonRequestBody() {
		final String storeAddress = Conca.t(ADDR, "/store_meme");
		final Meme meme = new Meme();
		meme.getUser().setId(USER_ID);
		meme.getLvMemeType().setId(LV_MEME_TYPE_ID);
		meme.getMemeBackground().setId(MEME_BACKGROUND_ID);
		meme.getMemeBackground().getMemes().add(meme);
		
		meme.getMemeTexts().add(new MemeText(meme, "top text test", Constants.TOP, 26));
		meme.getMemeTexts().add(new MemeText(meme, "bottom text test", Constants.BOTTOM, 26));
		
		final JSONObject reqBodyJsonObj = new JSONObject(new ShallowMeme(meme));
		
		Log.d(getClass().getSimpleName(), Conca.t("request={",reqBodyJsonObj.toString()));
	
		final JSONObject respObj = webClient.getJsonObject(storeAddress, reqBodyJsonObj);
		
		Log.d(getClass().getSimpleName(), Conca.t("response={",respObj.toString()));
		
		
		assertNotNull(respObj);
		assertTrue(respObj.keySet().size() > 0);
		
	}
	
	public static void main(String[] args) {
		WebClientTest test = new WebClientTest() {
			protected void runTest() throws Throwable {
				testGetJsonObjWithJsonRequestBody();
			};
		};
		test.run();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

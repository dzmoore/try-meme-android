package com.eastapps.mgs.util;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

import com.google.gson.Gson;

public class TestUtils {
	private final static Logger logger = Logger.getLogger(TestUtils.class);
	private static final String WEB_SVC_URL = "http://localhost:8080";
		
	public static String getJSONObject(final String addr, final Object requestObj) {
		final String url = StringUtils.join(WEB_SVC_URL, addr);
		
		final String requestJson = requestObj instanceof String ? requestObj.toString() : new Gson().toJson(requestObj);
		String responseStr = StringUtils.EMPTY;
		
		try {
			responseStr = postJson(url, requestJson);
			
			if (StringUtils.isBlank(responseStr)) {
				logger.warn(StringUtils.join(
					"response empty:request=[", requestJson.toString(), 
					"],url=[", url, "]"
				));
				
			}
			
		} catch (Exception e) {
			logger.error("error occurred while attempting to get JSON obj", e);
			
		} 		
		
		return responseStr;
	}
	
	public static List<?> getJSONList(final String addr, final Type type) {
		return new Gson().fromJson(getJSONObject(addr, null), type);
	}
	
	public static <T> T getJSONObject(final String addr, final Object requestObj, final Class<T> responseType) {
		final String url = StringUtils.join(WEB_SVC_URL, addr);
		
		final String requestJson = new Gson().toJson(requestObj);
		String responseStr = StringUtils.EMPTY;
		
		T response = null;
		try {
			responseStr = postJson(url, requestJson);
			
			if (StringUtils.isBlank(responseStr)) {
				logger.warn(StringUtils.join(
					"response empty:request=[", requestJson.toString(), 
					"],url=[", url, "]"
				));
				
			} else {
				response = new Gson().fromJson(responseStr, responseType);
			}
			
		} catch (Exception e) {
			logger.error("error occurred while attempting to get JSON obj", e);
			
		} 		
		
		return response;
	}

	private static String postJson(final String addr, final String jsonRequest) throws ClientProtocolException, IOException {
		final DefaultHttpClient client = new DefaultHttpClient();
		final HttpPost httpPost = new HttpPost(addr);
		
		if (StringUtils.isNotBlank(jsonRequest)) {
    		final StringEntity strEnt = new StringEntity(jsonRequest);
    		
    		httpPost.setEntity(strEnt);
		}
		
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-type", "application/json");
		
		ResponseHandler<String> respHandler = new BasicResponseHandler();
		
		final String respStr = client.execute(httpPost, respHandler);
		return respStr;
	}
}

package com.eastapps.meme_gen_android.http;

import java.lang.reflect.Type;
import java.util.List;

import com.eastapps.meme_gen_android.R;
import com.eastapps.meme_gen_android.mgr.ICallback;

import android.graphics.Bitmap;

public interface IWebClient {
	public String getJSONObject(final String addr);
	public <T> T sendRequestAsJson(final String addr, final Object requestObj, final Class<T> returnType);
	public Bitmap getBitmap(final String addr);
	public String getJSONObject(String addr, String jsonRequest);
	void setConnectionTimeoutMs(int connectTimeoutMs);
	void setConnectionUseCaches(boolean useCaches);
	public List<?> sendRequestAsJsonReturnList(String addr, Object requestObj, Type type);
	public List<?> getRequestAsJsonReturnList(String addr, Type returnType);
	void setExceptionCallback(ICallback<Exception> exceptionCallback);
}

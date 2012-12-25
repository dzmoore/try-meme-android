package com.eastapps.meme_gen_server.domain;

import com.eastapps.meme_gen_android.R;
import com.eastapps.meme_gen_android.util.Constants;

public class IntResult {
	private int result;
	
	public IntResult() {
		super();
		setResult(Constants.INVALID);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("IntResult [result=");
		builder.append(result);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.result;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IntResult other = (IntResult) obj;
		if (result != other.result)
			return false;
		return true;
	}
}

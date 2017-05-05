package com.tinyurl.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;

import com.tinyurl.constant.Constants;

public class VerifyCaptcha {

	public static boolean verify(String captcha) {
		boolean verified = false;
		JSONObject json = new JSONObject();
		HttpsURLConnection conn = null;
		InputStream inputStream = null;
		try {
			String postParams = Constants.SECRET + "=" + Constants.PRIVATE_KEY + "&" + Constants.RESPONSE + "=" + captcha;
			conn = (HttpsURLConnection) new URL(Constants.CAPTCHA_VERIFY_URL + "?" + postParams).openConnection();
			conn.setDoOutput(true);
			inputStream = conn.getInputStream();
			BufferedReader bR = new BufferedReader(new InputStreamReader(inputStream));
			String line = "";
			StringBuilder responseStrBuilder = new StringBuilder();
			while ((line = bR.readLine()) != null) responseStrBuilder.append(line);
			json = new JSONObject(responseStrBuilder.toString());
			if(json.getBoolean(Constants.SUCCESS)) verified = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null)
				try {
					inputStream.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			if (conn != null) conn.disconnect();
		}
		return verified;
	}
}

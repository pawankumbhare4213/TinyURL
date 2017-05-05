package com.tinyurl.constant;

import java.io.InputStream;
import java.util.Properties;

public abstract class Constants {
	public static final String URL_COUNTER = "URL_COUNTER";
	public static final String URLS = "URLS";
	public static final String HIT_COUNT = "HIT_COUNT";
	public static final String LONG_URL = "LONG_URL";
	public static final String SHORT_URL = "SHORT_URL";
	public static final String TIMESTAMP = "TIMESTAMP";
	public static final String INVALID_SHORT_URL = "Invalid URL, please try again.";
	public static final String MESSAGE = "MESSAGE";
	public static final String SUCCESS = "success";
	public static final String SECRET = "secret";
	public static final String RESPONSE = "response";
	public static final String CONFIG_FILE = "com/tinyurl/conf/tinyurl.conf";

	public static String PRIVATE_KEY = "PRIVATE_KEY";
	public static String NAMESPACE = "NAMESPACE";
	public static String CAPTCHA_VERIFY_URL = "CAPTCHA_VERIFY_URL";
	public static String IP = "IP";
	public static String PORT_NO = "PORT";
	public static int PORT = 0;

	static {		
		Properties conf = new Properties();
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream input = classLoader.getResourceAsStream(CONFIG_FILE);
			conf.load(input);
			PRIVATE_KEY = conf.getProperty(PRIVATE_KEY);
			NAMESPACE = conf.getProperty(NAMESPACE);
			CAPTCHA_VERIFY_URL = conf.getProperty(CAPTCHA_VERIFY_URL);
			IP = conf.getProperty(IP);
			PORT = Integer.parseInt(conf.getProperty(PORT_NO));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
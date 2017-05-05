package com.tinyurl.main;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.json.JSONException;
import org.json.JSONObject;

import com.aerospike.client.Record;
import com.tinyurl.constant.Constants;
import com.tinyurl.db.AerospikeDB;
import com.tinyurl.util.Codec;

public class TinyURL {

	private static volatile AtomicReference<BigInteger> urlCounter;
	private static AerospikeDB db;

	private TinyURL() { }

	static {
		db = new AerospikeDB(Constants.NAMESPACE);
		Record record = db.readKey(Constants.NAMESPACE, Constants.URL_COUNTER);
		System.out.println(record);
		if (record == null) {
			db.writeKey(Constants.NAMESPACE, Constants.URL_COUNTER, Constants.URL_COUNTER, Codec.encode(BigInteger.ZERO));
			System.out.println("ZERO!");
		}
		BigInteger counter = Codec.decodeToBigInteger(db.readKey(Constants.NAMESPACE, Constants.URL_COUNTER).getString(Constants.URL_COUNTER));
		System.out.println("Counter value = " + counter.toString());
		urlCounter = new AtomicReference<BigInteger>(counter);
	}

	public static synchronized String setURL(String longURL) {
		BigInteger counter = urlCounter.updateAndGet(num -> num.add(BigInteger.valueOf(1)));
		System.out.println("su counter = " + counter);
		db.writeKey(Constants.NAMESPACE, Constants.URL_COUNTER, Constants.URL_COUNTER, Codec.encode(counter));
		String shortURL = Codec.encode(counter);
		Map<String, Object> bins = new HashMap<>();
		bins.put(Constants.LONG_URL, longURL);
		bins.put(Constants.HIT_COUNT, 0);
		bins.put(Constants.TIMESTAMP, System.currentTimeMillis());
		db.writeKey(Constants.URLS, shortURL, bins);
		return shortURL;
	}

	public static String getURL(String shortURL) {
		String longURL = null;
		Record record = db.readKey(Constants.URLS, shortURL);
		if (record != null) {
			longURL = record.getString(Constants.LONG_URL);
			db.incrementBin(Constants.URLS, shortURL, Constants.HIT_COUNT);
		}
		return longURL;
	}

	public static JSONObject getURLDetails(String shortURL) {
		JSONObject json = new JSONObject();
		try {
			Record record = db.readKey(Constants.URLS, shortURL);
			json.append(Constants.SHORT_URL, shortURL);
			json.append(Constants.LONG_URL, record.getString(Constants.LONG_URL));
			json.append(Constants.HIT_COUNT, record.getLong(Constants.HIT_COUNT));
			Date date = new Date(record.getLong(Constants.TIMESTAMP));
			DateFormat format = new SimpleDateFormat( "MM/dd/yyyy HH:mm:ss");
			json.append(Constants.TIMESTAMP, format.format(date));
		} catch (Exception e) {
			e.printStackTrace();
			try {
				json.append(Constants.MESSAGE, Constants.INVALID_SHORT_URL);
				json.remove(Constants.SHORT_URL);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
		return json;
	}

	public static void openConnection() {
		AerospikeDB.setManual(true);
		if (db != null) db.openConnection();
	}

	public static void closeConnection() {
		if (db != null) db.closeConnection();
		AerospikeDB.setManual(false);
	}
}
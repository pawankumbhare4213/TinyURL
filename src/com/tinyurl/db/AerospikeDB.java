package com.tinyurl.db;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.policy.BatchPolicy;
import com.aerospike.client.policy.Policy;
import com.aerospike.client.policy.WritePolicy;
import com.tinyurl.constant.Constants;

public class AerospikeDB {
	private static AerospikeClient client;
	private static WritePolicy writePolicy;
	private static Policy readPolicy;
	private static BatchPolicy batchPolicy;
	private static volatile Object obj = new Object();
	private static boolean isManual = false;
	private static String NAMESPACE;
	private static volatile AerospikeClient aClient = null;

	public AerospikeDB(String namespace) {
		AerospikeDB.NAMESPACE = namespace;
		writePolicy = new WritePolicy();
		readPolicy = new Policy();
		batchPolicy = new BatchPolicy();
		client = new AerospikeClient(Constants.IP, Constants.PORT);
	}
	
	public static boolean isManual() {
		return isManual;
	}

	public static void setManual(boolean isManual) {
		AerospikeDB.isManual = isManual;
	}

	public void openConnection() {
		if (isManual && (client == null || !client.isConnected())) client = new AerospikeClient(Constants.IP, Constants.PORT);
		System.out.println("CONNECTION OPENED");
	}
	
	public void closeConnection() {
		if(isManual && client != null) client.close();
		System.out.println("CONNECTION CLOSED");
	}

	public Record[] readKeys(String name, List<String> keys) {
		Record[] records = null;
		try {
			if (client == null || !client.isConnected()) client = new AerospikeClient(Constants.IP, Constants.PORT);
			Key[] keyArr = new Key[keys.size()];
			for (int i = 0; i < keyArr.length; i++) keyArr[i] = new Key(NAMESPACE, name, keys.get(i));
			records = client.get(batchPolicy, keyArr);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (client != null && !isManual) client.close();
		}
		return records;
	}
	
	public Record readKey(String name, String key) {
		Record record = null;
		try {
			if (client == null || !client.isConnected()) client = new AerospikeClient(Constants.IP, Constants.PORT);
			Key keyObj = new Key(NAMESPACE, name, key);
			record = client.get(readPolicy, keyObj);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (client != null && !isManual) client.close();
		}
		return record;
	}

	public void writeKey(String name, String key, String bin, Object value) {
		try {
			if (client == null || !client.isConnected()) client = new AerospikeClient(Constants.IP, Constants.PORT);
			Key keyObj = new Key(NAMESPACE, name, key);
			Bin binObj = new Bin(bin, value);
			client.put(writePolicy, keyObj, binObj);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (client != null && !isManual) client.close();
		}
	}
	
	public void writeKey(String name, String key, Map<String, Object> bins) {
		try {
			if (client == null || !client.isConnected()) client = new AerospikeClient(Constants.IP, Constants.PORT);
			Key keyObj = new Key(NAMESPACE, name, key);
			Bin[] binArr = new Bin[bins.size()];
			int i = 0;
			for (Entry<String, Object> entry : bins.entrySet()) binArr[i++] = new Bin(entry.getKey(), entry.getValue());
			client.put(writePolicy, keyObj, binArr);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (client != null && !isManual) client.close();
		}
	}

	public void deleteKey(String name, String key) {
		try {
			if (client == null || !client.isConnected()) client = new AerospikeClient(Constants.IP, Constants.PORT);
			Key keyObj = new Key(NAMESPACE, name, key);
			client.delete(writePolicy, keyObj);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (client != null && !isManual) client.close();
		}
	}

	public void deleteBins(String name, String key, List<String> bins) {
		try {
			if (client == null || !client.isConnected()) client = new AerospikeClient(Constants.IP, Constants.PORT);
			Key keyObj = new Key(NAMESPACE, name, key);
			Bin[] binArr = new Bin[bins.size()];
			for (int i = 0; i < binArr.length; i++) binArr[i] = Bin.asNull(bins.get(i));
			client.put(writePolicy, keyObj, binArr);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (client != null && !isManual) client.close();
		}
	}

	public void incrementBin(String name, String key, String bin) {
		Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
            	synchronized (obj) {
            		try {
            			aClient = new AerospikeClient(Constants.IP, Constants.PORT);            		
	            		Key keyObj = new Key(NAMESPACE, name, key);
	                	Bin binObj = new Bin(bin, 1);
	                	aClient.add(writePolicy, keyObj, binObj);
            		} catch (Exception e) {
            			e.printStackTrace();
            		} finally {
            			if (aClient != null) aClient.close();
            		}
				}
            }
        });
        thread.start();
	}
}
package test.tinyurl.service;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

public class TinyURLServiceTest {

	@Test
	public void testGetURL() throws ClientProtocolException, IOException {
		String shortURL = "ba";
		HttpUriRequest request = new HttpGet("http://localhost:8080/TinyURL/details/" + shortURL);
		HttpResponse response = HttpClientBuilder.create().build().execute(request);
		
		HttpEntity entity = response.getEntity();
		String responseString = EntityUtils.toString(entity, "UTF-8");
		System.out.println(responseString);
		//fail("Not yet implemented");
	}

	@Test
	public void testGetURLDetails() {
		//fail("Not yet implemented");
	}

	@Test
	public void testSetURL() {
		//fail("Not yet implemented");
	}
}

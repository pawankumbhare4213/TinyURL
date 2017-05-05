package com.tinyurl.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.json.JSONObject;

import com.tinyurl.main.TinyURL;
import com.tinyurl.util.VerifyCaptcha;

@Path("/")
public class TinyURLService {

	private static final String SUCCESS = "SUCCESS";
	private static final String INVALID_SHORT_URL = "Invalid URL!";
	private static final String SHORT_URL = "SHORT_URL";
	private static final String MESSAGE = "MESSAGE";
	private static final String SOMETHING_WENT_WRONG = "Something went wrong, please try again.";

	@Context
	private HttpServletResponse response;
	@Context
	private HttpServletRequest request;

	@GET
	@Path("/{shortURL}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getURL(@PathParam("shortURL") String shortURL) {
		String longURL = TinyURL.getURL(shortURL);
		if (longURL != null && longURL.length() > 0) {
			try {
				response.sendRedirect(longURL);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return SUCCESS;
		} else return INVALID_SHORT_URL;
	}

	@GET
	@Path("/details/{shortURL}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getURLDetails(@PathParam("shortURL") String shortURL) {
		JSONObject json = TinyURL.getURLDetails(shortURL);
		try {
			json.put(SHORT_URL, request.getServerName() + "/" + shortURL);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				json.append(MESSAGE, SOMETHING_WENT_WRONG);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
		return json.toString();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String setURL(@FormParam("url") String longURL, @FormParam("captcha") String captcha) throws IOException {
		JSONObject json = new JSONObject();
		if(!VerifyCaptcha.verify(captcha)) return json.toString();
		try {
			String shortURL = TinyURL.setURL(longURL);
			json.put(SHORT_URL, request.getServerName() + "/" + shortURL);
		} catch (JSONException e) {
			e.printStackTrace();
			try {
				json.append(MESSAGE, SOMETHING_WENT_WRONG);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
		return json.toString();
	}
	
	@GET
	public void goToIndex() throws IOException {
		response.sendRedirect("/index");
	}
}
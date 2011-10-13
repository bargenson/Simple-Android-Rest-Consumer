package fr.bargenson.util.rest;

import java.net.URI;

public class UnsuccessfulHttpRequestException extends Exception {

	public UnsuccessfulHttpRequestException(String method, URI uri, int statusCode, String reasonPhrase) {
		super("Request: " + method + " " + uri.toString() + " / " + "Response: " + statusCode + " - " + reasonPhrase);
	}

}

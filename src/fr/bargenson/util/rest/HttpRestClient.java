package fr.bargenson.util.rest;

import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class HttpRestClient {
	
	private static final String ACCEPT_HEADER_NAME = "Accept";
	private static final String CONTENT_TYPE_HEADER_NAME = "Content-Type";
	
	private HttpRequestBase request;

	public HttpRestClient(HttpRequestMethod method, String uri) {
		request = HttpRequestFactory.getHTTPRequest(method, uri);
    }

	public HttpRestClient addParam(String name, Object value) {
        request.getParams().setParameter(name, value);
        return this;
    }
	
	public HttpRestClient setAcceptHeader(MediaType type) {
		request.addHeader(new BasicHeader(ACCEPT_HEADER_NAME, type.getMime()));
		return this;
	}
	
	public HttpRestClient setContentTypeHeader(MediaType type) {
		request.addHeader(new BasicHeader(CONTENT_TYPE_HEADER_NAME, type.getMime()));
		return this;
	}
 
    public HttpRestClient addHeader(String name, String value) {
        request.addHeader(new BasicHeader(name, value));
        return this;
    }
    
    public HttpRestClient setEntity(String entity) {
    	if(request instanceof HttpEntityEnclosingRequestBase) {
    		try {
				((HttpEntityEnclosingRequestBase) request).setEntity(new StringEntity(entity));
				return this;
			} catch (UnsupportedEncodingException e) {
				Log.e("HttpRestClient", "UnsupportedEncodingException!", e);
			}
    	}
    	throw new IllegalStateException();
    }

    public String execute() {    	
    	HttpClient httpClient = new DefaultHttpClient();
    	try {
			HttpResponse response = httpClient.execute(request);
			if(isSuccessfulStatusCode(response.getStatusLine().getStatusCode())) {
				if(response.getEntity() != null) {
					return EntityUtils.toString(response.getEntity());
				}
				return null;
			} else {
				throw new UnsuccessfulHttpRequestException(
						request.getMethod(),
						request.getURI(),
						response.getStatusLine().getStatusCode(), 
						response.getStatusLine().getReasonPhrase());
			}
		} catch (Exception e) {
			throw new RestClientException(e);
		}
    }
    
    private boolean isSuccessfulStatusCode(int statusCode) {
    	return statusCode >= 200 && statusCode < 300;
	}

	private static class HttpRequestFactory {
    	
    	static HttpRequestBase getHTTPRequest(HttpRequestMethod method, String uri) {
    		switch (method) {
			case GET:
				return new HttpGet(uri);
			case POST:
				return new HttpPost(uri);
			case PUT:
				return new HttpPut(uri);
			case DELETE:
				return new HttpDelete(uri);
			default:
				throw new UnsupportedOperationException();
			}
    	}
    	
    }
    
}

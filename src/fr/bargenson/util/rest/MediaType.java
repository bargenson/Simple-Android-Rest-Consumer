package fr.bargenson.util.rest;

public enum MediaType {
	
	JSON("application/json"),
	XML("*/xml"),
	TEXT_PLAIN("text/plain");
	
	private String mime;
	
	private MediaType(String mime) {
		this.mime = mime;
	}
	
	public String getMime() {
		return mime;
	}

}

package com.mediadistillery.message.jersey;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Message {
	
	@JsonProperty("sender_id")
	private String id;
	
	@JsonProperty("push_message")
	private String message;
	
	public Message() {}
	
	public Message(String id, String message) {
		this.id = id;
		this.message = message;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "Message [id=" + id + ", message=" + message + "]";
	}

}

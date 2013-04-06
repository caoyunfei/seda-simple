package com.seda.event;

import com.seda.message.Message;

public interface Worker extends Runnable{
	public Message getResult(Message message);
	
	public void setMessage(Message message);
	
	/*
	 * set the message key of returned message, invoker of Worker 
	 * needs this key to get the result 
	 */
	public void setResultMessageKeyName(String name);
}

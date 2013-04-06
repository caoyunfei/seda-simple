package com.seda.message;

import java.util.concurrent.TimeUnit;

public interface MessageContainer {
	public Message getMessage(Message key, long timeout, TimeUnit timeUnit);	
	public boolean putMessage(Message key, Message message);
}

package com.seda.event;

import com.seda.message.Message;

public interface EventHandler {	
	/**
	 * handle the message
	 * @param message
	 * @return the result of handling message
	 */
	public Message handleEvent(Message message);
	
	/**
	 * judge whether the handler is ready to handle the message
	 * @param message
	 * @return
	 */
	public boolean isReady(Message message);
	
	/**
	 * get the worker by message for deal the message 
	 * the worker will be executed in a thread pool
	 * @param message
	 * @return
	 */
	public Worker getWorker(Message message);
	
}

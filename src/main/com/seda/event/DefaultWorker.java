package com.seda.event;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.seda.message.DefaultMessage;
import com.seda.message.Message;
import com.seda.message.MessageType;
import com.seda.util.BlockingMap;

public abstract class DefaultWorker implements Worker {
	protected Message message;
	protected Logger logger = Logger.getLogger(getClass());
	protected String messageKeyName;
	
	@Override
	public void setResultMessageKeyName(String name){
		this.messageKeyName = name;
	}
	/**
	 * time out for get result, seconds
	 */
	protected long timeout = 30;	
	
	public void setMessage(Message message){
		this.message = message;
	}
	
	public void setTimeout(long timeout){
		this.timeout = timeout;
	}
	
	protected final BlockingMap<Message, Message> resultMap = new BlockingMap<Message, Message>(10);

	@Override
	public Message getResult(Message message) {
		try {
			Message result = this.resultMap.get(message, timeout, TimeUnit.SECONDS);
			if(result == null){
				result = new DefaultMessage(MessageType.NULLMESSAGE);
			}
			return result;
		} catch (InterruptedException e) {
			logger.error("failed to get worker result " + e);			
		}
		return new DefaultMessage(MessageType.NULLMESSAGE);
	}
}

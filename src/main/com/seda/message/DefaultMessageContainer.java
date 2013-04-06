package com.seda.message;

import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import com.seda.util.BlockingMap;

public class DefaultMessageContainer implements MessageContainer {	
	Logger logger = Logger.getLogger(getClass());
	private BlockingMap<Message, Message> map = new BlockingMap<Message, Message>(10);
	
	@Override	
	public Message getMessage(Message key, long timeout, TimeUnit timeUnit) {	
		try {			
			Message result = map.get(key, timeout, timeUnit);
			if(result == null){
				result = new DefaultMessage(MessageType.NULLMESSAGE);
			}
			return result;
		} catch (InterruptedException e) {
			logger.debug(e);
		}
		return new DefaultMessage(MessageType.NULLMESSAGE);
	}
	
	@Override
	public boolean putMessage(Message key, Message message) {
		return map.putIfAbsent(key, message);		
	}
}

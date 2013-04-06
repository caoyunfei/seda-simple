package com.seda.message;

import java.util.Map;

public interface Message {
	public int getMessageType();
	
	public void setMessageType(int type);
	
	public Map<String, Object> getMessageBody();
	
	public void setMessageBody(Map<String, Object> mm);
	
	public Object getMessageValue(Object key);
}

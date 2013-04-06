package com.seda.message;

import java.util.Map;


public class DefaultMessage implements Message{	

	private Map<String, Object> mm;
	private int type;
	
	public DefaultMessage(int type){
		this.type = type;
	}
	
	@Override
	public int getMessageType() {
		return this.type;
	}

	@Override
	public Map<String, Object> getMessageBody() {
		return this.mm;
	}

	@Override
	public void setMessageBody(Map<String, Object> mm) {
		this.mm = mm;		
	}

	@Override
	public void setMessageType(int type) {
		this.type = type;
	}

	@Override
	public Object getMessageValue(Object key) {
		return this.mm.get(key);
	}
}


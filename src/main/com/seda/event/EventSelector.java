package com.seda.event;

import java.util.Queue;

import org.apache.log4j.Logger;

import com.seda.message.Message;

public class EventSelector implements Runnable{
	Logger logger = Logger.getLogger(getClass());
	
	Queue<Message> messageList;
	DefaultEventHandler handler;
	private int sleepInterval=100;
	public void setSleepInterval(int sleepInterval) {
		this.sleepInterval = sleepInterval;
	}
	public EventSelector(Queue<Message> list, DefaultEventHandler handler){
		this.messageList = list;
		this.handler = handler;
	}
	
	@Override
	public void run() {
		while(true){
			Message message = this.messageList.poll();
			if(message == null){
				try {
					Thread.sleep(this.sleepInterval);
				} catch (InterruptedException e) {
					logger.debug(e);
				}
			}else{
				Worker worker = this.handler.getWorker(message);
				handler.runTask(worker);
				handler.setEventResult(message, worker.getResult(message));
			}			
		}		
	}
}

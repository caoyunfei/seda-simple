package com.seda.event;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.seda.message.DefaultMessage;
import com.seda.message.DefaultMessageContainer;
import com.seda.message.Message;
import com.seda.message.MessageContainer;
import com.seda.message.MessageType;

public abstract class DefaultEventHandler implements EventHandler {
	/**
	 * the name of the key of message
	 */
	public static String RESULT_MESSAGE_KEY_NAME="result";
	
	protected Logger logger = Logger.getLogger(DefaultEventHandler.class);
	
	private int poolSize = 10;
	private int maxPoolSize = 30;
	private long keepAliveTime = 100;
	private ThreadPoolExecutor threadPool = null;
	/**
	 * waiting for get event result, time unit is second
	 */
	private int timeout = 30;
	private TimeUnit timeUnit = TimeUnit.SECONDS;
	
	private ArrayBlockingQueue<Runnable> queue = null;
	
	private BlockingQueue<Message> messageList = new LinkedBlockingQueue<Message>();
	
	private MessageContainer messageContainer = new DefaultMessageContainer();
	
	private EventSelector es = new EventSelector(this.messageList, this);
	
	public DefaultEventHandler(){
		Thread t = new Thread(es);
		t.start();
	}
	
	void setEventResult(Message event, Message result){
		this.messageContainer.putMessage(event, result);
	}	
	
	private ThreadPoolExecutor getThreadPoll(){
		if(threadPool==null){			
			synchronized(DefaultEventHandler.class){
				if(threadPool == null){
					StringBuffer sb = new StringBuffer().append("/").append(this.getClass().getName()).append(".properties");
					Properties p = new Properties();					
					try {
						p.load(DefaultEventHandler.class.getResourceAsStream(sb.toString() ));
					} catch (IOException e) {
						logger.error(e);						
						try {
							p.load(DefaultEventHandler.class.getResourceAsStream("/com.seda.event.DefaultEventHandler.properties"));
						} catch (IOException e1) {
							logger.debug(e1);							
						}
					}
					if(p.getProperty("handler.poolSize")!=null){
						poolSize = Integer.valueOf(p.getProperty("handler.poolSize"));
						maxPoolSize = Integer.valueOf(p.getProperty("handler.maxPoolSize"));
						keepAliveTime = Integer.valueOf(p.getProperty("handler.keepAliveTime"));
						timeout = Integer.valueOf(p.getProperty("handler.keepAliveTime"));
					}					
					queue = new ArrayBlockingQueue<Runnable>(maxPoolSize);
					threadPool = new ThreadPoolExecutor(poolSize, maxPoolSize, keepAliveTime, TimeUnit.SECONDS, queue);
				}
			}
		}
		return threadPool;
	}
	
	public void runTask(Runnable task) {
		getThreadPoll().execute(task);
	}

	public void shutDown() {
		getThreadPoll().shutdown();
	}
	
	@Override
	public Message handleEvent(Message message) {
		if(this.isReady(message)){
			this.messageList.add(message);
			return this.messageContainer.getMessage(message, timeout, timeUnit);
		}else{
			return new DefaultMessage(MessageType.NULLMESSAGE);
		}					
	}
	
}

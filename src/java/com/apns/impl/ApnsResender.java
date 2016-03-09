package com.apns.impl;

import java.util.Queue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.apns.IApnsService;
import com.apns.model.PushNotification;

public class ApnsResender {
	private static Logger logger = LoggerFactory.getLogger(ApnsResender.class);
	private static ApnsResender instance = new ApnsResender();
	public static ApnsResender getInstance() {
		return instance;
	}
	public void resend(String name, Queue<PushNotification> queue) {
		IApnsService service = ApnsServiceImpl.getCachedService(name);
		if (service != null) {
			while (!queue.isEmpty()) {
				service.sendNotification(queue.poll());
			}
		} else {
			logger.error("Cached service is null. name: " + name);
		}
	}

}

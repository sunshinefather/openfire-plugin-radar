package com.apns;

import java.util.List;

import com.apns.model.Feedback;

public interface IApnsFeedbackConnection {
	
	public List<Feedback> getFeedbacks();
}

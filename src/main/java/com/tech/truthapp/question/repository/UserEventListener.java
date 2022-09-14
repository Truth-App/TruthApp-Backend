package com.tech.truthapp.question.repository;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;

import com.tech.truthapp.model.User;

public class UserEventListener extends AbstractMongoEventListener<User> {

	@Override
	public void onBeforeSave(BeforeSaveEvent<User> event) {
		User user = event.getSource();
		if(user.isNew()) {
			user.setUserId("");
		}
		super.onBeforeSave(event);
	}
}

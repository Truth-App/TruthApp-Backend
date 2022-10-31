package com.tech.truthapp.audit.question;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.tech.truthapp.model.question.QuestionReviewer;

@Component
public class QuestionReviewAuditService {

	public void updateQuestionAuditOnCreate(QuestionReviewer question) {
		question.setCreatedAt(new Date());
		question.setCreatedBy("system");
		question.setUpdatedAt(new Date());
		question.setLastModifiedBy("system");
		question.setVersion(1L);
	}
	
	public void updateQuestionAuditOnUpdate(QuestionReviewer question) {
		question.setUpdatedAt(new Date());
		question.setLastModifiedBy("system");
		Long version = question.getVersion();
		version = version + 1L;
		question.setVersion(version);
	}
}

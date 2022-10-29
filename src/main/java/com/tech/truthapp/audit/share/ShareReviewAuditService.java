package com.tech.truthapp.audit.share;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.tech.truthapp.model.share.ShareReviewer;

@Component
public class ShareReviewAuditService {

	public void updateAuditOnCreate(ShareReviewer share) {
		share.setCreatedAt(new Date());
		share.setCreatedBy("system");
		share.setUpdatedAt(new Date());
		share.setLastModifiedBy("system");
		share.setVersion(1L);
	}
	
	public void updateAuditOnUpdate(ShareReviewer share) {
		share.setUpdatedAt(new Date());
		share.setLastModifiedBy("system");
		Long version = share.getVersion();
		version = version + 1L;
		share.setVersion(version);
	}
}

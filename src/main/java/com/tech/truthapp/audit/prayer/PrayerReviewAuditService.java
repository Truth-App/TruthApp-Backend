package com.tech.truthapp.audit.prayer;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.tech.truthapp.model.prayer.PrayerReviewer;

@Component
public class PrayerReviewAuditService {

	public void updateAuditOnCreate(PrayerReviewer prayer) {
		prayer.setCreatedAt(new Date());
		prayer.setCreatedBy("system");
		prayer.setUpdatedAt(new Date());
		prayer.setLastModifiedBy("system");
		prayer.setVersion(1L);
	}
	
	public void updateAuditOnUpdate(PrayerReviewer prayer) {
		prayer.setUpdatedAt(new Date());
		prayer.setLastModifiedBy("system");
		Long version = prayer.getVersion();
		version = version + 1L;
		prayer.setVersion(version);
	}
}

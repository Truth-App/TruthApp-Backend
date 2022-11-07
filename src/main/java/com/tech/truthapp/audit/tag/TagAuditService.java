package com.tech.truthapp.audit.tag;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.tech.truthapp.model.tag.Tag;

@Component
public class TagAuditService {

	public void updateAuditOnCreate(Tag tag) {
		tag.setCreatedAt(new Date());
		tag.setCreatedBy("system");
		tag.setUpdatedAt(new Date());
		tag.setLastModifiedBy("system");
		tag.setVersion(1L);
	}
	
	public void updateAuditOnUpdate(Tag tag) {
		tag.setUpdatedAt(new Date());
		tag.setLastModifiedBy("system");
		Long version = tag.getVersion();
		version = version + 1L;
		tag.setVersion(version);
	}
}

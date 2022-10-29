package com.tech.truthapp.audit.group;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.tech.truthapp.model.group.Group;


@Component
public class GroupAuditService {

	public void updateAuditOnCreate(Group group) {
		group.setCreatedAt(new Date());
		group.setCreatedBy("system");
		group.setUpdatedAt(new Date());
		group.setLastModifiedBy("system");
		group.setVersion(1L);
	}
	
	public void updateAuditOnUpdate(Group group) {
		group.setUpdatedAt(new Date());
		group.setLastModifiedBy("system");
		Long version = group.getVersion();
		version = version + 1L;
		group.setVersion(version);
	}
}

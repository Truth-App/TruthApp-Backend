package com.tech.truthapp.audit.category;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.tech.truthapp.model.tag.Category;


@Component
public class CategoryAuditService {

	public void updateAuditOnCreate(Category category) {
		category.setCreatedAt(new Date());
		category.setCreatedBy("system");
		category.setUpdatedAt(new Date());
		category.setLastModifiedBy("system");
		category.setVersion(1L);
	}
	
	public void updateAuditOnUpdate(Category category) {
		category.setUpdatedAt(new Date());
		category.setLastModifiedBy("system");
		Long version = category.getVersion();
		version = version + 1L;
		category.setVersion(version);
	}
}

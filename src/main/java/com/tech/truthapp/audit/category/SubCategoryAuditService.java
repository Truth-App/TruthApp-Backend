package com.tech.truthapp.audit.category;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.tech.truthapp.model.tag.SubCategory;



@Component
public class SubCategoryAuditService {

	public void updateAuditOnCreate(SubCategory subCategory) {
		subCategory.setCreatedAt(new Date());
		subCategory.setCreatedBy("system");
		subCategory.setUpdatedAt(new Date());
		subCategory.setLastModifiedBy("system");
		subCategory.setVersion(1L);
	}
	
	public void updateAuditOnUpdate(SubCategory subCategory) {
		subCategory.setUpdatedAt(new Date());
		subCategory.setLastModifiedBy("system");
		Long version = subCategory.getVersion();
		version = version + 1L;
		subCategory.setVersion(version);
	}
}

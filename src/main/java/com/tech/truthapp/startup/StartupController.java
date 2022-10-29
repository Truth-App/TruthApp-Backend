package com.tech.truthapp.startup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tech.truthapp.category.service.CategoryService;
import com.tech.truthapp.dto.category.CategoryDTO;
import com.tech.truthapp.dto.category.SubCategoryDTO;
import com.tech.truthapp.dto.group.GroupDTO;
import com.tech.truthapp.group.service.GroupService;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/api")
@Log4j2
public class StartupController {

	@Autowired
	CategoryService categoryService;
	
	@Autowired
	GroupService groupService;

	@PostMapping("/saveCategoryData")
	public ResponseEntity<Void> saveCategory() throws Exception {
		log.debug("REST request to Save Category Data ");
		savePhysicalCategoryData();
		saveMentalCategoryData();
		saveSocialCategoryData();
		saveSpiritualCategoryData();
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/saveGroupData")
	public ResponseEntity<Void> saveGroupData() throws Exception {
		log.debug("REST request to Save Group Data ");
		saveGroupsData();
		return ResponseEntity.ok().build();
	}

	public void savePhysicalCategoryData() throws Exception {
		CategoryDTO categoryDTO = new CategoryDTO();
		categoryDTO.setCategory("Physical");
		categoryDTO = categoryService.saveCategory(categoryDTO);
		Thread.sleep(10000);
		SubCategoryDTO subCategoryDTO = new SubCategoryDTO();
		subCategoryDTO.setSubCategory("Education");
		categoryService.saveSubCategory(categoryDTO.getId(), subCategoryDTO);

		Thread.sleep(10000);
		subCategoryDTO = new SubCategoryDTO();
		subCategoryDTO.setSubCategory("Job");
		categoryService.saveSubCategory(categoryDTO.getId(), subCategoryDTO);

		Thread.sleep(10000);
		subCategoryDTO = new SubCategoryDTO();
		subCategoryDTO.setSubCategory("Marriage");
		categoryService.saveSubCategory(categoryDTO.getId(), subCategoryDTO);

		Thread.sleep(10000);
		subCategoryDTO = new SubCategoryDTO();
		subCategoryDTO.setSubCategory("Money");
		categoryService.saveSubCategory(categoryDTO.getId(), subCategoryDTO);

		Thread.sleep(10000);
		subCategoryDTO = new SubCategoryDTO();
		subCategoryDTO.setSubCategory("Health");
		categoryService.saveSubCategory(categoryDTO.getId(), subCategoryDTO);
		
	}

	public void saveSpiritualCategoryData() throws Exception {
		CategoryDTO categoryDTO = new CategoryDTO();
		categoryDTO.setCategory("Spiritual");
		categoryDTO = categoryService.saveCategory(categoryDTO);
		Thread.sleep(10000);
		SubCategoryDTO subCategoryDTO = new SubCategoryDTO();
		subCategoryDTO.setSubCategory("Prayer");
		categoryService.saveSubCategory(categoryDTO.getId(), subCategoryDTO);

		Thread.sleep(10000);
		subCategoryDTO = new SubCategoryDTO();
		subCategoryDTO.setSubCategory("Faith");
		categoryService.saveSubCategory(categoryDTO.getId(), subCategoryDTO);

		Thread.sleep(10000);
		subCategoryDTO = new SubCategoryDTO();
		subCategoryDTO.setSubCategory("Sin");
		categoryService.saveSubCategory(categoryDTO.getId(), subCategoryDTO);

		Thread.sleep(10000);
		subCategoryDTO = new SubCategoryDTO();
		subCategoryDTO.setSubCategory("Repentance");
		categoryService.saveSubCategory(categoryDTO.getId(), subCategoryDTO);

		Thread.sleep(10000);
		subCategoryDTO = new SubCategoryDTO();
		subCategoryDTO.setSubCategory("Forgiveness");
		categoryService.saveSubCategory(categoryDTO.getId(), subCategoryDTO);
	
	}

	public void saveSocialCategoryData() throws Exception {
		CategoryDTO categoryDTO = new CategoryDTO();
		categoryDTO.setCategory("Social");
		categoryDTO = categoryService.saveCategory(categoryDTO);
		Thread.sleep(10000);
		SubCategoryDTO subCategoryDTO = new SubCategoryDTO();
		subCategoryDTO.setSubCategory("Caste");
		categoryService.saveSubCategory(categoryDTO.getId(), subCategoryDTO);

		Thread.sleep(10000);
		subCategoryDTO = new SubCategoryDTO();
		subCategoryDTO.setSubCategory("Injustice");
		categoryService.saveSubCategory(categoryDTO.getId(), subCategoryDTO);

		Thread.sleep(10000);
		subCategoryDTO = new SubCategoryDTO();
		subCategoryDTO.setSubCategory("Class");
		categoryService.saveSubCategory(categoryDTO.getId(), subCategoryDTO);

		Thread.sleep(10000);
		subCategoryDTO = new SubCategoryDTO();
		subCategoryDTO.setSubCategory("Bribe");
		categoryService.saveSubCategory(categoryDTO.getId(), subCategoryDTO);

	}

	public void saveMentalCategoryData() throws Exception {
		CategoryDTO categoryDTO = new CategoryDTO();
		categoryDTO.setCategory("Mental");
		categoryDTO = categoryService.saveCategory(categoryDTO);
		Thread.sleep(10000);
		SubCategoryDTO subCategoryDTO = new SubCategoryDTO();
		subCategoryDTO.setSubCategory("Fear");
		categoryService.saveSubCategory(categoryDTO.getId(), subCategoryDTO);

		Thread.sleep(10000);
		subCategoryDTO = new SubCategoryDTO();
		subCategoryDTO.setSubCategory("Depression");
		categoryService.saveSubCategory(categoryDTO.getId(), subCategoryDTO);

		Thread.sleep(10000);
		subCategoryDTO = new SubCategoryDTO();
		subCategoryDTO.setSubCategory("Stress");
		categoryService.saveSubCategory(categoryDTO.getId(), subCategoryDTO);

		Thread.sleep(10000);
		subCategoryDTO = new SubCategoryDTO();
		subCategoryDTO.setSubCategory("Bad habits");
		categoryService.saveSubCategory(categoryDTO.getId(), subCategoryDTO);

		Thread.sleep(10000);
		subCategoryDTO = new SubCategoryDTO();
		subCategoryDTO.setSubCategory("Addictions");
		categoryService.saveSubCategory(categoryDTO.getId(), subCategoryDTO);

	}
	
	public void saveGroupsData() throws Exception {
		GroupDTO groupDTO = new GroupDTO();
		groupDTO.setGroup("KIDS");
		groupService.saveGroup(groupDTO);
		Thread.sleep(10000);
		
		groupDTO = new GroupDTO();
		groupDTO.setGroup("YOUTH");
		groupService.saveGroup(groupDTO);
		Thread.sleep(10000);
		
		groupDTO = new GroupDTO();
		groupDTO.setGroup("ADULT");
		groupService.saveGroup(groupDTO);
		Thread.sleep(10000);
		
		groupDTO = new GroupDTO();
		groupDTO.setGroup("ELDERS");
		groupService.saveGroup(groupDTO);
		Thread.sleep(10000);
		
		groupDTO = new GroupDTO();
		groupDTO.setGroup("OTHERS");
		groupService.saveGroup(groupDTO);
	}
}

package com.tech.truthapp.category.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tech.truthapp.category.service.CategoryService;
import com.tech.truthapp.category.validation.CategoryValidator;
import com.tech.truthapp.dto.category.CategoryDTO;
import com.tech.truthapp.dto.category.SubCategoryDTO;
import com.tech.truthapp.exception.HeaderUtil;

import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/api")
@Log4j2
@Api(tags = "Category Controller", value = "Category Controller", description = "Category Controller")
public class CategoryController {

	/** The Constant ENTITY_NAME. */
	private static final String ENTITY_NAME = "Category";

	@Value("${truth-app.clientApp.name}")
	private String applicationName;

	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private CategoryValidator categoryValidator;
	
	
	/**
	 * 
	 * @param categoryDTO
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/category")
	public ResponseEntity<CategoryDTO> saveCategory(@Valid @RequestBody CategoryDTO categoryDTO) throws Exception {
		log.debug("REST request to Save Category {} ", categoryDTO);
		categoryValidator.validateCreateCategory(categoryDTO);
		categoryDTO = categoryService.saveCategory(categoryDTO);
		return ResponseEntity
				.created(new URI("/api/category/" + categoryDTO.getId())).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, true, ENTITY_NAME, categoryDTO.getId().toString()))
				.body(categoryDTO);
	}
	
	/***
	 * 
	 * @param categoryId
	 * @param categoryDTO
	 * @return
	 * @throws Exception
	 */
	@PutMapping("/category/{categoryId}")
	public ResponseEntity<CategoryDTO> updateCategory(@PathVariable("categoryId") String categoryId,
			@Valid @RequestBody CategoryDTO categoryDTO) throws Exception {
		log.debug("REST request to Update Category {}, {} ", categoryId, categoryDTO);
		categoryDTO.setId(categoryId);
		categoryDTO = categoryService.upateCategory(categoryDTO);
		return ResponseEntity.created(new URI("/api/category/" + categoryId))
				.headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, categoryId))
				.body(categoryDTO);
	}
	/**
	 * 
	 * @param categoryId
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/category/{categoryId}")
	public ResponseEntity<CategoryDTO> getCategory(@PathVariable("categoryId") String categoryId) throws Exception {
		log.debug("REST request to Get Category {}", categoryId);
		CategoryDTO categoryDTO = categoryService.getCategory(categoryId);
		return ResponseEntity.ok().body(categoryDTO);
	}
	
	/***
	 * 
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/category")
	public ResponseEntity<List<CategoryDTO>> getAllCategory() throws Exception {
		log.debug("REST request to Get All Category");
		List<CategoryDTO> categoryDTOList = categoryService.getAllCategory();
		return ResponseEntity.ok().body(categoryDTOList);
	}
	
	/**
	 * 
	 * @param categoryId
	 * @return
	 * @throws Exception
	 */
	@DeleteMapping("/category/{categoryId}")
	public ResponseEntity<Void> DeleteCategory(@PathVariable("categoryId") String categoryId) throws Exception {
		log.debug("REST request to Delete Category {}", categoryId);
		categoryService.deleteCategory(categoryId);
		return ResponseEntity.ok().build();
	}
	
	/**
	 * 
	 * @param categoryId
	 * @param subCategoryDTO
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/category/{categoryId}/subcategory")
	public ResponseEntity<CategoryDTO> createSubCategory(@PathVariable("categoryId") String categoryId,
			@Valid @RequestBody SubCategoryDTO subCategoryDTO) throws Exception {
		log.debug("REST request to Create Sub Category {}, {} ", categoryId, subCategoryDTO);
		CategoryDTO categoryDTO = categoryService.saveSubCategory(categoryId, subCategoryDTO);
		return ResponseEntity.created(new URI("/api/category/" + categoryId))
				.headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, categoryId))
				.body(categoryDTO);
	}
	/***
	 * 
	 * @param categoryId
	 * @param subCategoryId
	 * @param subCategoryDTO
	 * @return
	 * @throws Exception
	 */
	@PutMapping("/category/{categoryId}/subcategory/{subCategoryId}")
	public ResponseEntity<CategoryDTO> updateSubCategory(@PathVariable("categoryId") String categoryId,
			@PathVariable("subCategoryId") String subCategoryId,
			@Valid @RequestBody SubCategoryDTO subCategoryDTO) throws Exception {
		log.debug("REST request to Update Sub Category {}, {}, {} ", categoryId,subCategoryId, subCategoryDTO);
		subCategoryDTO.setId(subCategoryId);
		CategoryDTO categoryDTO = categoryService.updateSubCategory(categoryId, subCategoryDTO);
		return ResponseEntity.created(new URI("/api/category/" + categoryId+ "/subcategory/" + subCategoryId))
				.headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, categoryId))
				.body(categoryDTO);
	}
	
	/***
	 * 
	 * @param categoryId
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/category/{categoryId}/subcategory")
	public ResponseEntity<List<SubCategoryDTO>> getAllSubCategory(@PathVariable("categoryId") String categoryId) throws Exception {
		log.debug("REST request to Get All Sub Category {}, ", categoryId);
		List<SubCategoryDTO> subList = categoryService.getAllSubCategory(categoryId);
		return ResponseEntity.ok().body(subList);
	}
	
	/***
	 * 
	 * @param categoryId
	 * @param subCategoryId
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/category/{categoryId}/subcategory/{subCategoryId}")
	public ResponseEntity<SubCategoryDTO> getSubCategory(@PathVariable("categoryId") String categoryId,
			@PathVariable("subCategoryId") String subCategoryId) throws Exception {
		log.debug("REST request to Get Sub Category {}, {}", categoryId,subCategoryId);
		SubCategoryDTO subCategoryDTO = categoryService.getSubCategory(categoryId, subCategoryId); 
		return ResponseEntity.ok().body(subCategoryDTO);
	}
	
	/***
	 * 
	 * @param categoryId
	 * @param subCategoryId
	 * @return
	 * @throws Exception
	 */
	@DeleteMapping("/category/{categoryId}/subcategory/{subCategoryId}")
	public ResponseEntity<Void> deleteSubCategory(@PathVariable("categoryId") String categoryId,
			@PathVariable("subCategoryId") String subCategoryId) throws Exception {
		log.debug("REST request to Delete Sub Category {}, {}", categoryId,subCategoryId);
		categoryService.deleteSubCategory(categoryId, subCategoryId); 
		return ResponseEntity.ok().build();
	}
}

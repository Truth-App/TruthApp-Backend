package com.tech.truthapp.prayer.controller;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tech.truthapp.dto.PrayerDTO;
import com.tech.truthapp.exception.HeaderUtil;
import com.tech.truthapp.prayer.service.PrayerService;
import com.tech.truthapp.prayer.validation.PrayerValidator;

import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/api")
@Log4j2
@Api(tags = "Prayer Controller", value = "Prayer Controller", description = "Prayer Controller")
public class PrayerController {

	/** The Constant ENTITY_NAME. */
	private static final String ENTITY_NAME = "Prayer";

	@Value("${truth-app.clientApp.name}")
	private String applicationName;

	@Autowired
	private PrayerValidator prayerValidator;

	@Autowired
	private PrayerService prayerService;

	/**
	 * 
	 * @param prayerDTO
	 * @return prayerDTO
	 * @throws Exception
	 */
	@PostMapping("/prayers")
	public ResponseEntity<PrayerDTO> savePrayer(@Valid @RequestBody PrayerDTO prayerDTO) throws Exception {

		log.debug("REST request to Save Prayer {} ", prayerDTO);
		prayerValidator.validateCreatePrayer(prayerDTO);
		prayerDTO = prayerService.savePrayer(prayerDTO);
		return ResponseEntity
				.created(new URI("/api/prayers/" + prayerDTO.getId())).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, true, ENTITY_NAME, prayerDTO.getId().toString()))
				.body(prayerDTO);
	}

	
}

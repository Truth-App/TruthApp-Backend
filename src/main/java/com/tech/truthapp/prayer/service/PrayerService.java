package com.tech.truthapp.prayer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tech.truthapp.dto.PrayerDTO;
import com.tech.truthapp.model.Prayer;
import com.tech.truthapp.prayer.mapper.PrayerMapper;
import com.tech.truthapp.prayer.repository.PrayerRepository;

@Service
@Transactional
public class PrayerService {

	@Autowired
	private PrayerRepository prayerRepository;

	@Autowired
	private PrayerMapper prayerMapper;

	

	public PrayerDTO savePrayer(PrayerDTO prayerDTO) {
		Prayer prayer = prayerMapper.toEntity(prayerDTO);
		prayerRepository.save(prayer);
		prayerDTO = prayerMapper.toDto(prayer);
		return prayerDTO;
	}

	
	
}

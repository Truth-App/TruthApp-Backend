package com.tech.truthapp.prayer.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.tech.truthapp.dto.PrayerDTO;
import com.tech.truthapp.dto.QuestionDTO;
import com.tech.truthapp.mapper.EntityMapper;
import com.tech.truthapp.model.Prayer;

@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PrayerMapper extends EntityMapper<PrayerDTO, Prayer>{
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	Prayer toEntity(QuestionDTO dto);
	
	

	/**
	 * 
	 * @param prayer
	 * @return
	 */
	PrayerDTO toDto(Prayer prayer);

	/**
	 * 
	 * @param prayers
	 * @return
	 */
	List<PrayerDTO> toDto(List<Prayer> prayers);

}

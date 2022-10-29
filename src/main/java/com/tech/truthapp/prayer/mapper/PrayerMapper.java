package com.tech.truthapp.prayer.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.tech.truthapp.dto.prayer.PrayerDTO;
import com.tech.truthapp.mapper.EntityMapper;
import com.tech.truthapp.model.prayer.Prayer;

@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PrayerMapper extends EntityMapper<PrayerDTO, Prayer>{
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	Prayer toEntity(PrayerDTO dto);
	
	

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

package com.tech.truthapp.prayer.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.tech.truthapp.dto.prayer.PrayerResponsesDTO;
import com.tech.truthapp.mapper.EntityMapper;
import com.tech.truthapp.model.prayer.PrayerResponse;

@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PrayerResponseMapper extends EntityMapper<PrayerResponsesDTO, PrayerResponse>{
	
	/**
	 * 
	 * @param userDTO
	 * @return
	 */
	PrayerResponse toEntity(PrayerResponsesDTO prayerResponsesDTO);
	
	

	/**
	 * 
	 * @param user
	 * @return
	 */
	PrayerResponsesDTO toDto(PrayerResponse prayerResponse);

	/**
	 * 
	 * @param users
	 * @return
	 */
	List<PrayerResponsesDTO> toDto(List<PrayerResponse> list);

}

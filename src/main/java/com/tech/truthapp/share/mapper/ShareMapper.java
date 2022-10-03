package com.tech.truthapp.share.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.tech.truthapp.dto.share.ShareDTO;
import com.tech.truthapp.mapper.EntityMapper;
import com.tech.truthapp.model.share.Share;

@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ShareMapper extends EntityMapper<ShareDTO, Share>{
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	Share toEntity(ShareDTO dto);
	
	

	/**
	 * 
	 * @param share
	 * @return
	 */
	ShareDTO toDto(Share prayer);

	/**
	 * 
	 * @param shares
	 * @return
	 */
	List<ShareDTO> toDto(List<Share> prayers);

}

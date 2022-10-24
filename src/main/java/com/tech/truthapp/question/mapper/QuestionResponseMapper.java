package com.tech.truthapp.question.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.tech.truthapp.dto.question.QuestionResponsesDTO;
import com.tech.truthapp.mapper.EntityMapper;
import com.tech.truthapp.model.question.QuestionResponse;

@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface QuestionResponseMapper extends EntityMapper<QuestionResponsesDTO, QuestionResponse>{
	
	/**
	 * 
	 * @param userDTO
	 * @return
	 */
	QuestionResponse toEntity(QuestionResponsesDTO questionResponsesDTO);
	
	

	/**
	 * 
	 * @param user
	 * @return
	 */
	QuestionResponsesDTO toDto(QuestionResponse questionResponse);

	/**
	 * 
	 * @param users
	 * @return
	 */
	List<QuestionResponsesDTO> toDto(List<QuestionResponse> questionResponses);

}

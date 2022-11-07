package com.tech.truthapp.question.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.tech.truthapp.dto.question.QuestionDTO;
import com.tech.truthapp.mapper.EntityMapper;
import com.tech.truthapp.model.question.Question;

@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface QuestionMapper extends EntityMapper<QuestionDTO, Question>{
	
	/**
	 * 
	 * @param userDTO
	 * @return
	 */
	Question toEntity(QuestionDTO questionDTO);
	
	

	/**
	 * 
	 * @param user
	 * @return
	 */
	QuestionDTO toDto(Question question);

	/**
	 * 
	 * @param users
	 * @return
	 */
	List<QuestionDTO> toDto(List<Question> questions);

}

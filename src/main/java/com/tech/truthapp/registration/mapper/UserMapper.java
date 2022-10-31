package com.tech.truthapp.registration.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

import com.tech.truthapp.dto.UserDTO;
import com.tech.truthapp.mapper.EntityMapper;
import com.tech.truthapp.model.User;

@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper extends EntityMapper<UserDTO, User>{
	
	/**
	 * 
	 * @param userDTO
	 * @return
	 */
	User toEntity(UserDTO userDTO);
	
	/**
	 * 
	 * @param userDTO
	 * @return
	 */
	@Mappings({
		  @Mapping(target = "id", ignore = true), 
	      @Mapping(target = "userId", ignore = true), 
	      @Mapping(target="mobile", source="mobile"),
	      @Mapping(target="email", source="email"),
	      @Mapping(target="age", source="age"),
	      @Mapping(target="gender", source="gender"),
	      @Mapping(target = "occupation", source="occupation"), 
	      @Mapping(target = "salvation", source="salvation"),
	      @Mapping(target = "experienceWithGod", source="experienceWithGod") 
	    })
	User toEntityForUpdate(@MappingTarget User user, UserDTO userDTO);

	/**
	 * 
	 * @param user
	 * @return
	 */
	UserDTO toDto(User user);

	/**
	 * 
	 * @param users
	 * @return
	 */
	List<UserDTO> toDto(List<User> users);

}

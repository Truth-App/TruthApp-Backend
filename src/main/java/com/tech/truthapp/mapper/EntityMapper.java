package com.tech.truthapp.mapper;

import java.util.List;

import org.mapstruct.MappingTarget;

/**
 * The Interface EntityMapper.
 *
 * @param <D> the generic type
 * @param <E> the element type
 */
public interface EntityMapper<D, E> {

	/**
	 * To entity.
	 *
	 * @param dto the dto
	 * @return the e
	 */
	E toEntity(D dto);
	
	/**
	 * To entity.
	 *
	 * @param dto the dto
	 * @return the e
	 */
	E toEntityForUpdate(@MappingTarget E e,D dto);

	/**
	 * To dto.
	 *
	 * @param entity the entity
	 * @return the d
	 */
	D toDto(E entity);

	/**
	 * To dto.
	 *
	 * @param entityList the entity list
	 * @return the list
	 */
	List<D> toDto(List<E> entityList);
}

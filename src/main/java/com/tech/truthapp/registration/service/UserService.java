package com.tech.truthapp.registration.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tech.truthapp.config.UserTypes;
import com.tech.truthapp.dto.UserDTO;
import com.tech.truthapp.exception.BadRequestException;
import com.tech.truthapp.model.User;
import com.tech.truthapp.registration.mapper.UserMapper;
import com.tech.truthapp.registration.repository.UserRepository;
import com.tech.truthapp.sequence.SequenceGeneratorService;

@Service
@Transactional
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private SequenceGeneratorService service;
	
	/** The Constant ENTITY_NAME. */
	private static final String ENTITY_NAME = "User";

	/**
	 * 
	 * @param userDTO
	 * @return
	 */
	public UserDTO saveUser(UserDTO userDTO) {
		User user = userMapper.toEntity(userDTO);
		String userId = "USER-";
		Integer nextSequence = service.generateUserSequence(User.SEQUENCE_NAME);
		user.setUserId(userId + nextSequence);
		user.setUserType(UserTypes.USER);
		user.setUserActive(Boolean.TRUE);
		userRepository.save(user);
		userDTO = userMapper.toDto(user);
		return userDTO;
	}
	
	/**
	 * 
	 * @param userDTO
	 * @return
	 */
	public UserDTO updateUser(UserDTO userDTO) {
		List<User> dbUserList = userRepository.findByUserId(userDTO.getUserId());
		if(dbUserList.size() == 0) {
			String errorMsg = "For given User Id have no values :"+userDTO.getUserId();
			throw new BadRequestException(errorMsg, ENTITY_NAME, "NoRecord");
		}else if(dbUserList.size() > 1) {
			String errorMsg = "For given User Id have more values :"+userDTO.getUserId();
			throw new BadRequestException(errorMsg, ENTITY_NAME, "MoreRecord");
		}
		User user = dbUserList.get(0);
		user = userMapper.toEntityForUpdate(user,userDTO);
		userRepository.save(user);
		userDTO = userMapper.toDto(user);
		return userDTO;
	}
	
	/**
	 * 
	 * @param userDTO
	 * @return
	 */
	public UserDTO getUser(String userId) {
		List<User> userList = userRepository.findByUserId(userId);
		UserDTO userDTO = userMapper.toDto(userList.get(0));
		return userDTO;
	}
	
	/**
	 * 
	 * @param userDTO
	 * @return
	 */
	public void deleteUser(String userId) {
		List<User> userList = userRepository.findByUserId(userId);
		User user = userList.get(0);
		userRepository.delete(user);
	}
}

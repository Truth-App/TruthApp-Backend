package com.tech.truthapp.prayer.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tech.truthapp.dto.prayer.PrayerDTO;
import com.tech.truthapp.dto.prayer.PrayerResponsesDTO;
import com.tech.truthapp.model.prayer.Prayer;
import com.tech.truthapp.model.prayer.PrayerResponse;
import com.tech.truthapp.model.prayer.PrayerResponseReviewer;
import com.tech.truthapp.model.prayer.PrayerReviewer;
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

	public List<PrayerDTO> getAllPrayers() {		
		List<Prayer> prayerList = prayerRepository.findAll();
		List<PrayerDTO> list = prayerMapper.toDto(prayerList);
		return list;
	}
	
	public List<PrayerDTO> getAllPrayersByUser(String userId) {
		List<Prayer> userQuestionList = prayerRepository.findByPrayersByUserId(userId);
		List<PrayerDTO> dtoList = prayerMapper.toDto(userQuestionList);
		return dtoList;
	}

	public List<PrayerDTO> getReviewedPrayers() {
		List<Prayer> userQuestionList = prayerRepository.findByReviewedPrayers();
		List<PrayerDTO> dtoList = prayerMapper.toDto(userQuestionList);
		return dtoList;
	}

	public List<PrayerDTO> getReviewPrayers() {
		List<Prayer> userQuestionList = prayerRepository.findByReviewPrayers();
		List<PrayerDTO> dtoList = prayerMapper.toDto(userQuestionList);
		return dtoList;
	}
	
	public PrayerDTO reviewPrayer(String userId, PrayerDTO prayerDTO) {
		Optional<Prayer> optionalPrayer = prayerRepository.findByPrayerForReview(prayerDTO.getId());
				
		if (optionalPrayer.isPresent()) {
			Prayer dbPrayer = optionalPrayer.get();
			dbPrayer.setIsPublic(prayerDTO.getIsPublic());
			if (dbPrayer.getIsApproved()) {
				dbPrayer.setIsApproved(Boolean.TRUE);
				dbPrayer.setCategory(prayerDTO.getCategory());
			} else {
				dbPrayer.setIsApproved(Boolean.FALSE);
				Integer score = dbPrayer.getScore();
				score = score - 1;
				dbPrayer.setScore(score);
			}
			PrayerReviewer prayerReviewer = new PrayerReviewer();
			prayerReviewer.setReviewerId(userId);
			prayerReviewer.setComments(userId);
			prayerReviewer.setId(new ObjectId().toString());
			dbPrayer.getReviews().add(prayerReviewer);
			prayerRepository.save(dbPrayer);
			PrayerDTO dtoObject = prayerMapper.toDto(dbPrayer);
			return dtoObject;
		} else {
			System.out.println("Yes in side else condition");
			// throw exception
		}
		return null;
	}
	
	public PrayerDTO createResponse(String prayerId, PrayerResponsesDTO responseDTO) {
		Optional<Prayer> optionalPrayer = prayerRepository.findById(prayerId);
		if (optionalPrayer.isPresent()) {
			Prayer dbPrayer = optionalPrayer.get();
			PrayerResponse response = new PrayerResponse();
			response.setResponse(responseDTO.getResponse());
			response.setId(new ObjectId().toString());
			response.setCreatedBy("system");
			response.setCreatedAt(new Date());
			response.setIsApproved(Boolean.FALSE);
			response.setScore(2);
			response.setLastModifiedBy("system");
			response.setUpdatedAt(new Date());
			dbPrayer.getResponses().add(response);
			prayerRepository.save(dbPrayer);
			PrayerDTO dtoObject = prayerMapper.toDto(dbPrayer);
			return dtoObject;
		} else {
			// throw exception
		}
		return null;
	}
	
	public List<PrayerResponsesDTO> getPrayerResponse(String prayerId) {
		Optional<Prayer> optionalPrayer = prayerRepository.findById(prayerId);
		if (optionalPrayer.isPresent()) {
			Prayer dbPrayer = optionalPrayer.get();
			PrayerDTO dtoObject = prayerMapper.toDto(dbPrayer);
			return dtoObject.getResponses();
		} else {
			// throw exception
		}
		return null;
	}

	public PrayerDTO validatePrayerResponse(String prayerId, PrayerResponsesDTO responseDTO) {
		Optional<Prayer> optionalPrayer = prayerRepository.findById(prayerId);
		if (optionalPrayer.isPresent()) {
			Prayer dbPrayer = optionalPrayer.get();
			List<PrayerResponse> responses = dbPrayer.getResponses();

			Optional<PrayerResponse> matchingObject = responses.stream()
					.filter(object -> object.getId() != null && object.getId().equalsIgnoreCase(responseDTO.getId()))
					.findFirst();

			if (matchingObject.isPresent()) {
				PrayerResponse responseObject = matchingObject.get();
				responseObject.setIsApproved(Boolean.TRUE);
				List<PrayerResponseReviewer> reviewerList = responseObject.getReviews();
				PrayerResponseReviewer reviewer = new PrayerResponseReviewer();
				reviewer.setId(new ObjectId().toString());
				reviewer.setComments(responseDTO.getComments());
				reviewer.setReviewerId(responseDTO.getReviewerId());
				reviewerList.add(reviewer);				
				prayerRepository.save(dbPrayer);
				PrayerDTO dtoObject = prayerMapper.toDto(dbPrayer);
				return dtoObject;
			}
			return null;
		} else {
			System.out.println("Else block here");
			// throw exception
		}
		return null;
	}
}

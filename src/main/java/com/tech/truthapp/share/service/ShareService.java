package com.tech.truthapp.share.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tech.truthapp.dto.share.ShareDTO;
import com.tech.truthapp.dto.share.ShareResponsesDTO;
import com.tech.truthapp.model.share.Share;
import com.tech.truthapp.model.share.ShareResponse;
import com.tech.truthapp.model.share.ShareResponseReviewer;
import com.tech.truthapp.model.share.ShareReviewer;
import com.tech.truthapp.share.mapper.ShareMapper;
import com.tech.truthapp.share.repository.ShareRepository;


@Service
@Transactional
public class ShareService {

	@Autowired
	private ShareRepository shareRepository;

	@Autowired
	private ShareMapper shareMapper;

	

	public ShareDTO saveShare(ShareDTO shareDTO) {
		Share share = shareMapper.toEntity(shareDTO);
		share.setIsApproved(Boolean.FALSE);
		share.setScore(2);
		shareRepository.save(share);
		shareDTO = shareMapper.toDto(share);
		return shareDTO;
	}

	public List<ShareDTO> getAllSahres() {		
		List<Share> shareList = shareRepository.findAll();
		List<ShareDTO> list = shareMapper.toDto(shareList);
		return list;
	}
	
	public List<ShareDTO> getAllSharesByUser(String userId) {
		List<Share> list = shareRepository.findByShareByUserId(userId);
		List<ShareDTO> dtoList = shareMapper.toDto(list);
		return dtoList;
	}

	public List<ShareDTO> getReviewedShares() {
		List<Share> list = shareRepository.findByReviewedShares();
		List<ShareDTO> dtoList = shareMapper.toDto(list);
		return dtoList;
	}

	public List<ShareDTO> getReviewShares() {
		List<Share> list = shareRepository.findByReviewShare();
		List<ShareDTO> dtoList = shareMapper.toDto(list);
		return dtoList;
	}
	
	public ShareDTO reviewShare(String userId, ShareDTO shareDTO) {
		Optional<Share> optionalObject = shareRepository.findByShareForReview(shareDTO.getId());
				
		if (optionalObject.isPresent()) {
			Share dbShare = optionalObject.get();			
			if (shareDTO.getIsApproved()) {
				dbShare.setIsApproved(Boolean.TRUE);
				dbShare.setCategory(shareDTO.getCategory());
			} else {
				dbShare.setIsApproved(Boolean.FALSE);
				Integer score = dbShare.getScore();
				score = score - 1;
				dbShare.setScore(score);
			}
			ShareReviewer reviewer = new ShareReviewer();
			reviewer.setReviewerId(userId);
			reviewer.setComments(userId);
			reviewer.setId(new ObjectId().toString());
			dbShare.getReviews().add(reviewer);
			shareRepository.save(dbShare);
			ShareDTO dtoObject = shareMapper.toDto(dbShare);
			return dtoObject;
		} else {
			System.out.println("Yes in side else condition");
			// throw exception
		}
		return null;
	}
	
	public ShareDTO createResponse(String shareId, ShareResponsesDTO responseDTO) {
		Optional<Share> optionalObject = shareRepository.findById(shareId);
		if (optionalObject.isPresent()) {
			Share dbObject = optionalObject.get();
			ShareResponse response = new ShareResponse();
			response.setResponse(responseDTO.getResponse());
			response.setId(new ObjectId().toString());
			response.setCreatedBy("system");
			response.setCreatedAt(new Date());
			response.setIsApproved(Boolean.FALSE);
			response.setScore(2);
			response.setLastModifiedBy("system");
			response.setUpdatedAt(new Date());
			dbObject.getResponses().add(response);
			shareRepository.save(dbObject);
			ShareDTO dtoObject = shareMapper.toDto(dbObject);
			return dtoObject;
		} else {
			// throw exception
		}
		return null;
	}
	
	public List<ShareResponsesDTO> getShareResponse(String shareId) {
		Optional<Share> optionalObject = shareRepository.findById(shareId);
		if (optionalObject.isPresent()) {
			Share dbObject = optionalObject.get();
			ShareDTO dtoObject = shareMapper.toDto(dbObject);
			return dtoObject.getResponses();
		} else {
			// throw exception
		}
		return null;
	}

	public ShareDTO validateResponse(String shareId, ShareResponsesDTO responseDTO) {
		Optional<Share> optionalObject = shareRepository.findById(shareId);
		if (optionalObject.isPresent()) {
			Share dbObjct = optionalObject.get();
			List<ShareResponse> responses = dbObjct.getResponses();

			Optional<ShareResponse> matchingObject = responses.stream()
					.filter(object -> object.getId() != null && object.getId().equalsIgnoreCase(responseDTO.getId()))
					.findFirst();

			if (matchingObject.isPresent()) {
				ShareResponse responseObject = matchingObject.get();
				responseObject.setIsApproved(Boolean.TRUE);
				List<ShareResponseReviewer> reviewerList = responseObject.getReviews();
				ShareResponseReviewer reviewer = new ShareResponseReviewer();
				reviewer.setId(new ObjectId().toString());
				reviewer.setComments(responseDTO.getComments());
				reviewer.setReviewerId(responseDTO.getReviewerId());
				reviewerList.add(reviewer);				
				shareRepository.save(dbObjct);
				ShareDTO dtoObject = shareMapper.toDto(dbObjct);
				return dtoObject;
			}
			return null;
		} else {
			System.out.println("Else block here");
			// throw exception
		}
		return null;
	}
	
	public List<ShareDTO> getReviewedShareForCategory(String category) {
		List<Share> list = shareRepository.findByReviewedSharesForCategory(category);
		List<ShareDTO> dtoList = shareMapper.toDto(list);
		return dtoList;
	}
	
	public List<ShareDTO> getReviewResponses() {
		List<Share> list = shareRepository.findByReviewResponses();
		for (Share share : list) {
			share.getResponses().removeIf(object -> object.getIsApproved() && object.getScore() > 0);
		}
		List<ShareDTO> dtoList = shareMapper.toDto(list);
		return dtoList;
	}
	
	public List<ShareDTO> getMyReviewedResponses(String userId) {
		List<Share> list = shareRepository.findByReviewedShares();
		for (Share share : list) {
			share.getResponses().removeIf(object -> object.getResponderId() == null || 
					!object.getResponderId().equalsIgnoreCase(userId));
		}
		List<ShareDTO> dtoList = shareMapper.toDto(list);
		return dtoList;
	}
}

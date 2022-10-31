package com.tech.truthapp.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OtpAuthenticationDTO {

	String otpRequestId;
	String mobile;
	String otpHash;
	String truthAppId;
	String status;
	String userType;
	Boolean isRegistered;
	String intialPrefId;
	String intialPrefType;
	String prefId;
	String prefType;
	String otp;
}

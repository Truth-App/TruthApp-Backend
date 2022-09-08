package com.tech.truthapp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document
@Setter
@Getter
@NoArgsConstructor
public class OtpAuthentication extends BaseModel{

	@Id
	String otpRequestId;
	String mobile;
	String otpHash;
	String truthAppId;
	String status;
	String userType;
	Boolean isRegistered;
	String intialPrefId;
	String intialPrefType;
	
	@Override
    public int hashCode() {
        return 42;
    }
	
	@Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        OtpAuthentication other = (OtpAuthentication) obj;
        if (otpRequestId == null) {
            return false;
        } else if (!otpRequestId.equals(other.otpRequestId))
            return false;
        return true;
    }

	@Override
	public String toString() {
		return "OtpAuthentication [otpRequestId=" + otpRequestId + ", mobile=" + mobile + ", otpHash=" + otpHash
				+ ", truthAppId=" + truthAppId + ", status=" + status + ", userType=" + userType + ", isRegistered="
				+ isRegistered + ", intialPrefId=" + intialPrefId + ", intialPrefType=" + intialPrefType + "]";
	}
}

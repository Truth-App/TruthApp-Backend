package com.tech.truthapp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Document
@Getter
@Setter
@NoArgsConstructor
public class PrayerResponse extends BaseModel{

	@Id
	Integer id;
	Boolean reviewFalg;
	String userId;
	String reviewedBy;
	String answerUserId;
	String remarks;
	

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
        PrayerResponse other = (PrayerResponse) obj;
        if (id == null) {
            return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

	@Override
	public String toString() {
		return "PrayerResponse [id=" + id + ", reviewFalg=" + reviewFalg + ", userId=" + userId + ", reviewedBy="
				+ reviewedBy + ", answerUserId=" + answerUserId + ", remarks=" + remarks + "]";
	}
}

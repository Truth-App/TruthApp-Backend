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
public class Testimony extends BaseModel{

	@Id
	Integer id;
	String category;
	Boolean reviewFalg;
	String remarks;
	String reviewedBy;
	
	
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
        PrayerRequest other = (PrayerRequest) obj;
        if (id == null) {
            return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

	@Override
	public String toString() {
		return "Testimony [id=" + id + ", category=" + category + ", reviewFalg=" + reviewFalg + ", remarks=" + remarks
				+ ", reviewedBy=" + reviewedBy + "]";
	}
}

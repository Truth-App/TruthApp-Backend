package com.tech.truthapp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(indexName = "preferences")
@Setter
@Getter
@NoArgsConstructor
public class Preferences {

	@Id
	String prefId;
	String prefType;
	String prefValue;
	Boolean isActive;
	
	
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
        Preferences other = (Preferences) obj;
        if (prefId == null) {
            return false;
        } else if (!prefId.equals(other.prefId))
            return false;
        return true;
    }

	@Override
	public String toString() {
		return "Preferences [prefId=" + prefId + ", prefType=" + prefType + ", prefValue=" + prefValue + ", isActive="
				+ isActive + "]";
	}
}

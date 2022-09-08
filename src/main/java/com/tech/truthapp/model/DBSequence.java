package com.tech.truthapp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "truth_sequence")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DBSequence {

	@Id
    private String id;
    private Integer sequence;
}

package com.mallireddy.templategenerator.domain.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "template_generator")
public class TemplateGenerator extends BaseEntity {
	
	private String projectName;
	private String createBy;
	private String team;
}
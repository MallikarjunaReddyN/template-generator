package com.mallireddy.templategenerator.repository;

import com.mallireddy.templategenerator.domain.entity.TemplateGenerator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateGeneratorRepository extends JpaRepository<TemplateGenerator, Integer> {
	
}

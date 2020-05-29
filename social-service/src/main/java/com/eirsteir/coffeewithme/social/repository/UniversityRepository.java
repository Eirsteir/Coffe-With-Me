package com.eirsteir.coffeewithme.social.repository;

import com.eirsteir.coffeewithme.social.domain.university.University;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UniversityRepository extends JpaRepository<University, Long> {
}

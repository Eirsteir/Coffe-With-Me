package com.eirsteir.coffeewithme.social.repository;

import com.eirsteir.coffeewithme.social.domain.university.Campus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampusRepository extends JpaRepository<Campus, Long> {
}

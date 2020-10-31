package com.eirsteir.coffeewithme.social.web.api.university;

import com.eirsteir.coffeewithme.social.repository.UniversityRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/universities")
public class UniversityController {

  @Autowired private UniversityRepository universityRepository;

  @GetMapping
  @ResponseBody
  ResponseEntity<List<?>> all(@RequestParam boolean includeCampuses) {
    if (includeCampuses) return ResponseEntity.ok(universityRepository.findAll());

    return ResponseEntity.ok(universityRepository.findAllExcludeCampuses());
  }
}

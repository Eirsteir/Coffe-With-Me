package com.eirsteir.coffeewithme.social.web.api.university;


import com.eirsteir.coffeewithme.social.repository.UniversityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/universities")
public class UniversityController {

    @Autowired
    private UniversityRepository universityRepository;

    @GetMapping
    @ResponseBody
    ResponseEntity<List<?>> all(@RequestParam boolean includeCampuses) {
        if (includeCampuses)
            return ResponseEntity.ok(universityRepository.findAll());

        return ResponseEntity.ok(universityRepository.findAllExcludeCampuses());
    }

}

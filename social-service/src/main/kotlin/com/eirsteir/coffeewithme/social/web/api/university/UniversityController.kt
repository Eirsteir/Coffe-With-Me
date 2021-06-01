package com.eirsteir.coffeewithme.social.web.api.university

import com.eirsteir.coffeewithme.social.repository.UniversityRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/universities")
class UniversityController(private val universityRepository: UniversityRepository) {

    @GetMapping
    @ResponseBody
    fun all(@RequestParam includeCampuses: Boolean): ResponseEntity<List<*>> =
        if (includeCampuses)
            ResponseEntity.ok(universityRepository.findAll())
        else ResponseEntity.ok(
            universityRepository.findAllExcludeCampuses()
        )
}
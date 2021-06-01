package com.eirsteir.coffeewithme.social.web.api.user

import com.eirsteir.coffeewithme.commons.dto.UserDetailsDto
import com.eirsteir.coffeewithme.commons.security.UserDetailsImpl
import com.eirsteir.coffeewithme.social.domain.user.User
import com.eirsteir.coffeewithme.social.repository.rsql.RqslVisitorImpl
import com.eirsteir.coffeewithme.social.service.user.UserService
import cz.jirutka.rsql.parser.RSQLParser
import cz.jirutka.rsql.parser.ast.Node
import mu.KotlinLogging
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

private val logger = KotlinLogging.logger { }

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {

    @GetMapping("/{id}")
    @ResponseBody
    fun user(@PathVariable id: Long, @AuthenticationPrincipal principal: UserDetailsImpl) =
        userService.findUserById(id, principal.id)

    @GetMapping
    fun search(@RequestParam search: String): List<UserDetailsDto> {
        val rootNode: Node = RSQLParser().parse(search)
        val spec: Specification<User> = rootNode.accept(RqslVisitorImpl())
        val results = userService.searchUsers(spec)

        if (results.isEmpty())
            throw ResponseStatusException(
            HttpStatus.NO_CONTENT, "Search query '$search' yielded no results"
        )
        return results
    }
}
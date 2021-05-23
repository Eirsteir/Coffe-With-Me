package com.eirsteir.coffeewithme.config

import com.eirsteir.coffeewithme.social.service.coffeebreak.CoffeeBreakService
import org.springframework.context.annotation.Bean

@TestConfiguration
class FriendshipControllerTestConfiguration {
    @MockBean
    private val coffeeBreakRepository: CoffeeBreakRepository? = null

    @MockBean
    private val userRepository: UserRepository? = null

    @MockBean
    private val campusRepository: CampusRepository? = null

    @MockBean
    private val friendshipRepository: FriendshipRepository? = null

    @MockBean
    private val universityRepository: UniversityRepository? = null
    @Bean
    fun friendshipService(): FriendshipService? {
        return Mockito.mock(FriendshipService::class.java)
    }

    @Bean
    fun userService(): UserService? {
        return Mockito.mock(UserService::class.java)
    }

    @Bean
    fun coffeeBreakService(): CoffeeBreakService? {
        return Mockito.mock(CoffeeBreakService::class.java)
    }
}
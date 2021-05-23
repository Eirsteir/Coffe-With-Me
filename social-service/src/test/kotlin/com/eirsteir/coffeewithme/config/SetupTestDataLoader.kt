package com.eirsteir.coffeewithme.config

import com.eirsteir.coffeewithme.social.domain.friendship.FriendshipStatus
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Slf4j
@Component
class SetupTestDataLoader : ApplicationListener<ContextRefreshedEvent?> {
    private var alreadySetup = false

    @Autowired
    private val userRepository: UserRepository? = null
    @Transactional
    override fun onApplicationEvent(event: ContextRefreshedEvent?) {
        if (alreadySetup) {
            return
        }
        val defaultUser: User = User.builder().id(1L).email("user").nickname("default").build()
        val requester: User = User.builder().id(2L).email(REQUESTER_EMAIL).nickname(REQUESTER_NICKNAME).build()
        val addressee: User = User.builder().id(3L).email(ADDRESSEE_EMAIL).nickname(ADDRESSEE_NICKNAME).build()
        val otherUser: User = User.builder().id(4L).email(OTHER_USER_EMAIL).nickname(OTHER_USER_NICKNAME).build()
        val userJohn: User = User.builder().id(5L).name("John Doe").email(JOHN_EMAIL).nickname("johndoe").build()
        val userTom: User = User.builder().id(6L).name("Tom Doe").email(TOM_EMAIL).build()
        SetupTestDataLoader.log.debug(
            "[x] Preloading {}",
            userRepository.saveAll(Arrays.asList(addressee, otherUser, userJohn, userTom))
        )
        defaultUser.addFriend(addressee, FriendshipStatus.ACCEPTED)
        SetupTestDataLoader.log.debug("[x] Preloading {}", userRepository.save(defaultUser))
        requester.addFriend(addressee, FriendshipStatus.ACCEPTED)
        requester.addFriend(otherUser, FriendshipStatus.REQUESTED)
        SetupTestDataLoader.log.debug("[x] Preloading {}", userRepository.save(requester))
        alreadySetup = true
    }

    companion object {
        private val REQUESTER_EMAIL: String? = "requester@test.com"
        private val ADDRESSEE_EMAIL: String? = "addressee@test.com"
        private val REQUESTER_NICKNAME: String? = "requester"
        private val ADDRESSEE_NICKNAME: String? = "addressee"
        val OTHER_USER_EMAIL: String? = "other-user@test.com"
        val OTHER_USER_NICKNAME: String? = "other-user"
        val JOHN_EMAIL: String? = "john@doe.com"
        val TOM_EMAIL: String? = "tom@doe.com"
    }
}
package com.eirsteir.coffeewithme.commons.domain.account

import com.eirsteir.coffeewithme.commons.domain.user.UserEvent

open class AbstractUserAccountEvent(var accountId: Long) : UserEvent
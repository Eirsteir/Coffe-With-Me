package com.eirsteir.coffeewithme.commons.domain.user

import com.eirsteir.coffeewithme.commons.domain.account.AbstractUserAccountEvent

class UserCreatedEvent(accountId: Long) : AbstractUserAccountEvent(accountId)
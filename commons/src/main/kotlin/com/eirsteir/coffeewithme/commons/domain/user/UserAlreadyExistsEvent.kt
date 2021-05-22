package com.eirsteir.coffeewithme.commons.domain.user

import com.eirsteir.coffeewithme.commons.domain.account.AbstractUserAccountEvent

class UserAlreadyExistsEvent(accountId: Long) : AbstractUserAccountEvent(accountId)
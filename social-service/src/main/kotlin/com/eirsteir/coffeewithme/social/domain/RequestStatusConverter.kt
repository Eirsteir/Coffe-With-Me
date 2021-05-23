package com.eirsteir.coffeewithme.social.domain

import javax.persistence.AttributeConverter

interface RequestStatusConverter<T : RequestStatus, I : Number> : AttributeConverter<T, I>
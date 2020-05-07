package com.eirsteir.coffeewithme.domain.request;

import javax.persistence.AttributeConverter;

public interface RequestStatusConverter<T extends RequestStatus, I extends Number> extends AttributeConverter<T, I> {

}

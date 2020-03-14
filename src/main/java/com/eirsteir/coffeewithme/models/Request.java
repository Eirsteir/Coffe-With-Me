package com.eirsteir.coffeewithme.models;

import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

abstract class Request extends CreatedUpdatedDateBaseModel {

    @OneToOne
    private User from;

    @OneToOne
    private User to;

    @NotNull
    private RequestStatus status;
}

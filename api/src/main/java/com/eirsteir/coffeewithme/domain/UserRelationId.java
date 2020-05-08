package com.eirsteir.coffeewithme.domain;

import com.eirsteir.coffeewithme.domain.user.User;
import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Data
@Embeddable
public class UserRelationId implements Serializable {

    private static final long serialVersionUID = 3966996285633364115L;

    @ManyToOne
    private User requester;

    @ManyToOne
    private User addressee;

}

package com.eirsteir.coffeewithme.domain;

import com.eirsteir.coffeewithme.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class FriendshipId implements Serializable {

    private static final long serialVersionUID = 3966996285633364115L;

    @ManyToOne
    private User requester;

    @ManyToOne
    private User addressee;

}
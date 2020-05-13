package com.eirsteir.coffeewithme.domain.friendship;

import com.eirsteir.coffeewithme.domain.user.User;
import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class FriendshipId implements Serializable {

    private static final long serialVersionUID = 3966996285633364115L;

    @ManyToOne
    User requester;

    @ManyToOne
    User addressee;

}

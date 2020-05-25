package com.eirsteir.coffeewithme.social.domain.friendship;

import com.eirsteir.coffeewithme.social.domain.user.User;
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
    private User requester;

    @ManyToOne
    private User addressee;

}

package com.eirsteir.coffeewithme.domain.friendship;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class FriendshipId implements Serializable {

    private static final long serialVersionUID = 3966996285633364115L;

    @Column(name = "requester_id")
    private Long requesterId;

    @Column(name = "addressee_id")
    private Long addresseeId;

}

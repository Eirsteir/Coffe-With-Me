package com.eirsteir.coffeewithme.domain.friendship;

import com.eirsteir.coffeewithme.domain.CreatedUpdatedDateTimeBaseModel;
import com.eirsteir.coffeewithme.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@Accessors(chain = true)
@Entity
public class Friendship extends CreatedUpdatedDateTimeBaseModel {

    @EmbeddedId
    private FriendshipId id;

    @ManyToOne
    @MapsId("requester_id")
    @JoinColumn(name = "requester_id")
    private User requester;

    @ManyToOne
    @MapsId("addressee_id")
    @JoinColumn(name = "addressee_id")
    private User addressee;

    private FriendshipStatus status;

}

package com.eirsteir.coffeewithme.domain.friendship;

import com.eirsteir.coffeewithme.domain.CreatedUpdatedDateTimeBaseModel;
import com.eirsteir.coffeewithme.domain.UserRelationId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@Entity
public class Friendship extends CreatedUpdatedDateTimeBaseModel {

    @EmbeddedId
    private UserRelationId id;

    private FriendshipStatus status;

}

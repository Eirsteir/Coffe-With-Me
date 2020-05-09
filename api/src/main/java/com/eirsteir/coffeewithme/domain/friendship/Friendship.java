package com.eirsteir.coffeewithme.domain.friendship;

import com.eirsteir.coffeewithme.domain.CreatedUpdatedDateTimeBaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

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

    private FriendshipStatus status;

}

package com.eirsteir.coffeewithme.domain.meeting;


import com.eirsteir.coffeewithme.domain.CreatedUpdatedDateTimeBaseModel;
import com.eirsteir.coffeewithme.domain.friendship.FriendshipId;
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
public class Meeting extends CreatedUpdatedDateTimeBaseModel {

    @EmbeddedId
    private FriendshipId id;

    private MeetingStatus status;

}


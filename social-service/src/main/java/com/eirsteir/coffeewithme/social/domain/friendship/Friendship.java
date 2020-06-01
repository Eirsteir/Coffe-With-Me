package com.eirsteir.coffeewithme.social.domain.friendship;

import com.eirsteir.coffeewithme.commons.domain.friendship.FriendRequestAcceptedEvent;
import com.eirsteir.coffeewithme.commons.domain.friendship.FriendRequestEvent;
import com.eirsteir.coffeewithme.commons.domain.user.UserDetails;
import com.eirsteir.coffeewithme.social.domain.user.User;
import io.eventuate.tram.events.publisher.ResultWithEvents;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Collections;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "friendships")
@AssociationOverrides({
        @AssociationOverride(name = "id.requester", joinColumns = @JoinColumn(name ="requester_id")),
        @AssociationOverride(name = "id.addressee", joinColumns = @JoinColumn(name ="addressee_id"))
})
public class Friendship {

    @EmbeddedId
    private FriendshipId id;

    @Transient
    public User getRequester() {
        return id.getRequester();
    }

    @Transient
    public User getAddressee() {
        return id.getAddressee();
    }

    private FriendshipStatus status;

    @CreationTimestamp
    private Date createdDateTime;

    @UpdateTimestamp
    private Date updatedDateTime;

    public static ResultWithEvents<Friendship> createFriendRequest(Friendship friendship, UserDetails user) {

        FriendRequestEvent friendRequestEvent = new FriendRequestEvent(friendship.getAddressee().getId(), user);
        return new ResultWithEvents<>(friendship, Collections.singletonList(friendRequestEvent));
    }

    public static ResultWithEvents<Friendship> createFriendRequestAccepted(Friendship friendship, UserDetails user) {

        FriendRequestAcceptedEvent friendRequestAcceptedEvent = new FriendRequestAcceptedEvent(
                friendship.getRequester().getId(), user);
        return new ResultWithEvents<>(friendship, Collections.singletonList(friendRequestAcceptedEvent));
    }

    public static class FriendshipBuilder {
        private FriendshipId id = new FriendshipId();

        public FriendshipBuilder requester(User requester) {
            this.id.setRequester(requester);
            return this;
        }

        public FriendshipBuilder addressee(User addressee) {
            this.id.setAddressee(addressee);
            return this;
        }
    }
}



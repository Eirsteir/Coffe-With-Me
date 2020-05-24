package com.eirsteir.coffeewithme.api.domain.friendship;

import com.eirsteir.coffeewithme.api.domain.user.User;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
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



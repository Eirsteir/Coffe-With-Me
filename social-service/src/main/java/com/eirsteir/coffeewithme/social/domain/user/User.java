package com.eirsteir.coffeewithme.social.domain.user;


import com.eirsteir.coffeewithme.social.domain.CreatedUpdatedDateTimeBaseModel;
import com.eirsteir.coffeewithme.social.domain.friendship.Friendship;
import com.eirsteir.coffeewithme.social.domain.friendship.FriendshipStatus;
import com.eirsteir.coffeewithme.social.domain.university.University;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


@Getter
@Setter
@Builder
@Accessors(chain = true)
@ToString
@EqualsAndHashCode(of = {"id", "email"}, callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User extends CreatedUpdatedDateTimeBaseModel {

    @Id
    private Long id;

    @Column(unique = true)
    private String email;

    private String nickname;

    private String name;

    private Date lastLogin;

    @OneToOne
    private University university;

    @JsonIgnore
    @Builder.Default
    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "id.requester",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval=true)
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    private List<Friendship> friends = new LinkedList<>();

    @JsonIgnore
    @ToString.Exclude
    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "id.addressee",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval=true)
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    private List<Friendship> friendsOf = new LinkedList<>();

    public Friendship addFriend(User friend, FriendshipStatus status) {
        Friendship friendship = Friendship.builder()
                .requester(this)
                .addressee(friend)
                .status(status)
                .build();

        if (this.friends == null)
            this.friends = new LinkedList<>();

        this.friends.add(friendship);
        friend.addFriendOf(friendship);

        return friendship;
    }

    private void addFriendOf(Friendship friendship) {
        if (this.friendsOf == null)
            this.friendsOf = new LinkedList<>();

        this.friendsOf.add(friendship);
    }

    public void removeFriendship(Friendship friendship) {
        friends.remove(friendship);
        friendship.getAddressee().friendsOf.remove(friendship);
    }
}

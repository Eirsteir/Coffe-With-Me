package com.eirsteir.coffeewithme.domain.user;


import com.eirsteir.coffeewithme.domain.CreatedUpdatedDateTimeBaseModel;
import com.eirsteir.coffeewithme.domain.friendship.Friendship;
import com.eirsteir.coffeewithme.domain.friendship.FriendshipPk;
import com.eirsteir.coffeewithme.domain.friendship.FriendshipStatus;
import com.eirsteir.coffeewithme.domain.role.Role;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;


@Getter
@Setter
@Builder
@Accessors(chain = true)
@ToString
@EqualsAndHashCode(of = {"id", "email"}, callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User extends CreatedUpdatedDateTimeBaseModel implements Serializable {

    private static final long serialVersionUID = 3966996285633364335L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String email;

    private String username;

    @ToString.Exclude
    private String password;

    private String name;

    private String mobileNumber;

    private UserType userType;

    private Date lastLogin;

    @ToString.Exclude
    @Builder.Default
    private Boolean enabled = true;

    @ToString.Exclude
    @Builder.Default
    private Boolean accountExpired = false;

    @ToString.Exclude
    @Builder.Default
    private Boolean accountLocked = false;

    @ToString.Exclude
    @Builder.Default
    private Boolean credentialsExpired = false;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "pk.requester",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval=true)
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    private List<Friendship> friends = new LinkedList<>();

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "pk.addressee",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval=true)
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    private List<Friendship> friendsOf = new LinkedList<>();

    public void addFriend(User friend, FriendshipStatus status) {
        Friendship friendship = Friendship.builder()
                .pk(FriendshipPk.builder()
                            .requester(this)
                            .addressee(friend)
                            .build())
                .requester(this)
                .addressee(friend)
                .status(status)
                .build();

        if (this.friends == null)
            this.friends = new ArrayList<>();

        this.friends.add(friendship);
        friend.addFriendOf(friendship);
    }

    private void addFriendOf(Friendship friendship) {
        if (this.friendsOf == null)
            this.friendsOf = new ArrayList<>();

        this.friendsOf.add(friendship);
    }

    // TODO: 12.05.2020 how should this be used?
    public void removeFriendship(Friendship friendship) {
        friends.remove(friendship);
        friendship.getAddressee().friendsOf.remove(friendship);
    }
}

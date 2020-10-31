package com.eirsteir.coffeewithme.social.domain.user;

import com.eirsteir.coffeewithme.social.domain.CreatedUpdatedDateTimeBaseModel;
import com.eirsteir.coffeewithme.social.domain.friendship.Friendship;
import com.eirsteir.coffeewithme.social.domain.friendship.FriendshipStatus;
import com.eirsteir.coffeewithme.social.domain.university.University;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Cascade;

@Getter
@Setter
@Builder
@Accessors(chain = true)
@ToString
@EqualsAndHashCode(
    of = {"id", "email"},
    callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User extends CreatedUpdatedDateTimeBaseModel {

  @Id private Long id;

  @Column(unique = true)
  private String email;

  private String nickname;

  private String name;

  private Date lastLogin;

  @ManyToOne
  @JoinColumn(name = "university_id")
  private University university;

  @JsonIgnore
  @Builder.Default
  @ToString.Exclude
  @OneToMany(
      fetch = FetchType.LAZY,
      mappedBy = "id.requester",
      cascade = {CascadeType.PERSIST, CascadeType.MERGE},
      orphanRemoval = true)
  @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
  private List<Friendship> friends = new LinkedList<>();

  public Friendship addFriend(User friend, FriendshipStatus status) {
    Friendship friendship =
        Friendship.builder().requester(this).addressee(friend).status(status).build();

    if (this.friends == null) this.friends = new LinkedList<>();

    this.friends.add(friendship);
    return friendship;
  }

  public List<Friendship> getFriendships() {
    return friends;
  }

  public List<User> getFriends() {
    return friends.stream()
        .filter(getFriendshipPredicate(FriendshipStatus.ACCEPTED))
        .map(getFriendshipUserFunction())
        .collect(Collectors.toList());
  }

  private static Predicate<Friendship> getFriendshipPredicate(FriendshipStatus status) {
    return friendship -> friendship.getStatus() == status;
  }

  private Function<Friendship, User> getFriendshipUserFunction() {
    return friendship -> {
      if (friendship.getAddressee().getId().equals(this.id)) return friendship.getRequester();
      return friendship.getAddressee();
    };
  }

  public List<User> getFiendsAtUniversity() {
    return getFriends().stream()
        .filter(user -> user.university.equals(this.university))
        .collect(Collectors.toList());
  }

  public void removeFriendship(Friendship friendship) {
    friends.remove(friendship);
  }
}

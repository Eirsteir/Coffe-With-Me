package com.eirsteir.coffeewithme.social.service.user;

import com.eirsteir.coffeewithme.commons.dto.UserDetailsDto;
import com.eirsteir.coffeewithme.commons.exception.CWMException;
import com.eirsteir.coffeewithme.commons.exception.EntityType;
import com.eirsteir.coffeewithme.commons.exception.ExceptionType;
import com.eirsteir.coffeewithme.commons.security.UserDetailsImpl;
import com.eirsteir.coffeewithme.social.domain.university.University;
import com.eirsteir.coffeewithme.social.domain.user.User;
import com.eirsteir.coffeewithme.social.dto.UserProfile;
import com.eirsteir.coffeewithme.social.repository.FriendshipRepository;
import com.eirsteir.coffeewithme.social.repository.UniversityRepository;
import com.eirsteir.coffeewithme.social.repository.UserRepository;
import com.eirsteir.coffeewithme.social.web.request.UpdateProfileRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {

  private UserRepository userRepository;

  private UniversityRepository universityRepository;

  private FriendshipRepository friendshipRepository;

  private ModelMapper modelMapper;

  @Autowired
  public UserServiceImpl(
      UserRepository userRepository,
      UniversityRepository universityRepository,
      FriendshipRepository friendshipRepository,
      ModelMapper modelMapper) {
    this.userRepository = userRepository;
    this.universityRepository = universityRepository;
    this.friendshipRepository = friendshipRepository;
    this.modelMapper = modelMapper;
  }

  @Override
  public UserDetailsDto findUserByEmail(String email) {
    User userModel =
        userRepository
            .findByEmail(email)
            .orElseThrow(
                () ->
                    CWMException.getException(
                        EntityType.USER, ExceptionType.ENTITY_NOT_FOUND, email));

    return modelMapper.map(userModel, UserDetailsDto.class);
  }

  @Override
  public User findUserById(Long id) {
    return userRepository
        .findById(id)
        .orElseThrow(
            () ->
                CWMException.getException(
                    EntityType.USER, ExceptionType.ENTITY_NOT_FOUND, id.toString()));
  }

  @Override
  public UserDetailsDto findUserById(Long id, Long currentUserId) {
    User user = findUserById(id);
    User currentUser = findUserById(id);
    UserDetailsDto userDetails = modelMapper.map(user, UserDetailsDto.class);
    includeFriendshipProperties(userDetails, user, currentUser);

    return userDetails;
  }

  // TODO: clearly separate this from retrieving other users
  private void includeFriendshipProperties(
      UserDetailsDto userDetails, User otherUser, User currentUser) {
    boolean areFriends = getAreFriends(otherUser, currentUser);
    int friendshipCount = friendshipRepository.countByUserId(currentUser.getId());

    userDetails.setIsFriend(areFriends);
    userDetails.setFriendsCount(friendshipCount);
  }

  private boolean getAreFriends(User otherUser, User currentUser) {
    return currentUser.getFriends().contains(otherUser);
  }

  // TODO: add friends properties
  @Override
  public List<UserDetailsDto> searchUsers(Specification<User> spec) {
    return userRepository.findAll(spec).stream()
        .map(user -> modelMapper.map(user, UserDetailsDto.class))
        .collect(Collectors.toList());
  }

  @Override
  public UserProfile updateProfile(
      UpdateProfileRequest updateProfileRequest, UserDetailsImpl currentUser) {
    User userToUpdate =
        userRepository
            .findById(currentUser.getId())
            .orElseThrow(
                () ->
                    CWMException.getException(
                        EntityType.USER,
                        ExceptionType.ENTITY_NOT_FOUND,
                        currentUser.getId().toString()));

    Optional<University> university =
        universityRepository.findById(updateProfileRequest.getUniversityId());
    university.ifPresent(userToUpdate::setUniversity);

    userToUpdate.setNickname(updateProfileRequest.getNickname());
    log.info("[x] Updated user profile: {}", userToUpdate);

    return modelMapper.map(userRepository.save(userToUpdate), UserProfile.class);
  }

  @Override
  public List<User> findByIdIn(List<Long> friendsIds) {
    return userRepository.findAllById(friendsIds);
  }
}

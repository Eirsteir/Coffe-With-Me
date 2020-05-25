package com.eirsteir.coffeewithme.api.web.api.friendship;

import com.eirsteir.coffeewithme.api.domain.notification.NotificationType;
import com.eirsteir.coffeewithme.api.domain.user.User;
import com.eirsteir.coffeewithme.api.dto.FriendshipDto;
import com.eirsteir.coffeewithme.api.service.friendship.FriendshipService;
import com.eirsteir.coffeewithme.api.service.user.UserService;
import com.eirsteir.coffeewithme.api.web.request.FriendRequest;
import com.eirsteir.coffeewithme.api.web.request.NotificationRequest;
import com.eirsteir.coffeewithme.commons.dto.UserDetails;
import com.eirsteir.coffeewithme.commons.security.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;


@Slf4j
@RestController
public class FriendshipController {

    @Autowired
    private FriendshipService friendshipService;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private WebClient webClient;

    @GetMapping("/{id}/friends")
    @ResponseBody
    Collection<UserDetails> getFriends(@PathVariable Long id) {
        UserDetails userDetails = modelMapper.map(userService.findUserById(id), UserDetails.class);
        List<UserDetails> friends = friendshipService.getFriends(userDetails);

        if (friends.isEmpty())
            throw new ResponseStatusException(
                    HttpStatus.NO_CONTENT, "User with email - " +  userDetails.getEmail() + " has no friends");

        return friends;
    }

    @PostMapping("/friends")
    @ResponseStatus(HttpStatus.CREATED)
    FriendshipDto addFriend(@RequestParam("to_friend") Long toFriend,
                            @AuthenticationPrincipal UserDetailsImpl principal) {

        User currentUser = userService.findUserById(principal.getId());
        if (currentUser.getId().equals(toFriend))
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Cannot send friend requests to yourself");

        FriendshipDto friendshipDto = friendshipService.registerFriendship(FriendRequest.builder()
                                                                                   .requesterId(currentUser.getId())
                                                                                   .addresseeId(toFriend)
                                                                                   .build());

        NotificationRequest notificationRequest = NotificationRequest.builder()
                .subjectId(toFriend)
                .type(NotificationType.FRIENDSHIP_REQUESTED)
                .userId(currentUser.getId())
                .name(currentUser.getName())
                .username(currentUser.getUsername())
                .build();

        String response = webClient.post()
                .uri("/notifications")
                .body(BodyInserters.fromPublisher(Mono.just(notificationRequest), NotificationRequest.class))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .acceptCharset(StandardCharsets.UTF_8)
                .exchange()
                .block()
                .bodyToMono(String.class)
                .block();

         log.debug("[x] Received response from notification api: {}", response);

//        notificationService.notify(friendshipDto.getAddresseeId(),
//                                   principal.getUser(),
//                                   NotificationType.FRIENDSHIP_REQUESTED);

        return friendshipDto;
    }

//    @PutMapping("/friends")
//    FriendshipDto updateFriendship(@RequestBody @Valid FriendshipDto friendshipDto,
//                                   @AuthenticationPrincipal UserPrincipalImpl principal) {
//        validateFriendshipDto(friendshipDto, principal);
//        FriendshipDto updatedFriendshipDto = friendshipService.updateFriendship(friendshipDto);
//
////        if (updatedFriendshipDto.getStatus() == FriendshipStatus.ACCEPTED)
////            notificationService.notify(principal.getUser().getId(),
////                                       principal.getUser(),
////                                       NotificationType.FRIENDSHIP_ACCEPTED);
//
//        return  updatedFriendshipDto;
//    }

//    @DeleteMapping("/friends")
//    void deleteFriendship(@RequestBody @Valid FriendshipDto friendshipDto,
//                                   @AuthenticationPrincipal UserPrincipalImpl principal) {
//        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
//    }
//
//    @GetMapping("/friends/requests")
//    List<UserDto> getFriendRequests(@AuthenticationPrincipal UserPrincipalImpl principal) {
//        UserDto userDto = modelMapper.map(principal.getUser(), UserDto.class);
//        List<UserDto> friendRequests = friendshipService.getFriendsOfWithStatus(userDto,
//                                                                                  FriendshipStatus.REQUESTED);
//
//        if (friendRequests.isEmpty())
//            throw new ResponseStatusException(
//                    HttpStatus.NO_CONTENT, "User with email - " +  userDto.getEmail() + " has no friend requests");
//
//
//        return friendRequests;
//    }

//    private void validateFriendshipDto(FriendshipDto friendshipDto, UserPrincipalImpl principal) {
//        Long requesterId = friendshipDto.getRequesterId();
//        Long addresseeId = friendshipDto.getAddresseeId();
//        Long principalId = principal.getUser().getId();
//
//        if (addresseeId.equals(principalId))
//            return;
//        else if (requesterId.equals(principalId))
//            throw new ResponseStatusException(
//                    HttpStatus.BAD_REQUEST, "Cannot accept friend request sent by yourself");
//
//        throw new ResponseStatusException(
//                HttpStatus.BAD_REQUEST, "Friendship does not belong to current user");
//    }
}

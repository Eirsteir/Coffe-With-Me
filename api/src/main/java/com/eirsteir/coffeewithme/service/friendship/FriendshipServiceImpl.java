package com.eirsteir.coffeewithme.service.friendship;

import com.eirsteir.coffeewithme.domain.friendship.Friendship;
import com.eirsteir.coffeewithme.domain.friendship.FriendshipPk;
import com.eirsteir.coffeewithme.domain.friendship.FriendshipStatus;
import com.eirsteir.coffeewithme.domain.user.User;
import com.eirsteir.coffeewithme.dto.FriendshipDto;
import com.eirsteir.coffeewithme.dto.UserDto;
import com.eirsteir.coffeewithme.exception.CWMException;
import com.eirsteir.coffeewithme.exception.EntityType;
import com.eirsteir.coffeewithme.exception.ExceptionType;
import com.eirsteir.coffeewithme.repository.FriendshipRepository;
import com.eirsteir.coffeewithme.service.user.UserService;
import com.eirsteir.coffeewithme.web.request.FriendRequest;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class FriendshipServiceImpl implements FriendshipService {

    @Autowired
    private UserService userService;

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public List<UserDto> getFriends(User user) {

        return user.getFriends()
                .stream()
                .map(Friendship::getAddressee)
                .map(friend -> modelMapper.map(friend, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public FriendshipDto registerFriendship(FriendRequest friendRequest) {
        User requester = modelMapper.map(userService.findUserById(friendRequest.getRequesterId()), User.class);
        User addressee = modelMapper.map(userService.findUserById(friendRequest.getAddresseeId()), User.class);

        if (friendshipExists(requester, addressee))
            throw CWMException.getException(EntityType.FRIENDSHIP,
                                            ExceptionType.DUPLICATE_ENTITY,
                                            requester.getId().toString(),
                                            addressee.getId().toString());

        Friendship friendship = requester.addFriend(addressee, FriendshipStatus.REQUESTED);
        return modelMapper.map(friendship, FriendshipDto.class);
    }

    private boolean friendshipExists(User requester, User addressee) {
        return friendshipRepository.existsByPkRequesterAndPkAddressee(requester, addressee);
    }

    @Override
    public void removeFriendship(FriendshipDto friendshipDto) {
        FriendshipPk id = modelMapper.map(friendshipDto.getId(), FriendshipPk.class);

        if (friendshipExists(id)) {
            friendshipRepository.deleteById(id);
            log.info("[x] Removed friendship: {}", friendshipDto);
        }

        throw CWMException.getException(EntityType.FRIENDSHIP,
                                        ExceptionType.ENTITY_NOT_FOUND,
                                        friendshipDto.getId().toString());
    }

    private boolean friendshipExists(FriendshipPk friendshipPk) {
        return friendshipRepository.existsById(friendshipPk);
    }

    @Override
    public FriendshipDto updateFriendship(FriendshipDto friendshipDto) {
        Friendship friendshipToUpdate = getFriendshipToUpdate(friendshipDto);

        if (isValidStatusChange(friendshipToUpdate.getStatus(), friendshipDto.getStatus()))
            return updateFriendship(friendshipDto, friendshipToUpdate);

        throw CWMException.getException(EntityType.FRIENDSHIP,
                                        ExceptionType.INVALID_STATUS_CHANGE,
                                        friendshipDto.getId().toString());
    }

    private Friendship getFriendshipToUpdate(FriendshipDto friendshipDto) {
        Long requesterId = friendshipDto.getId().getRequester().getId();
        Long addresseeId = friendshipDto.getId().getAddressee().getId();

        return friendshipRepository.
                findByPkRequesterIdAndPkAddresseeId(requesterId, addresseeId)
                    .orElseThrow(() -> CWMException.getException(EntityType.FRIENDSHIP,
                                                                 ExceptionType.ENTITY_NOT_FOUND,
                                                                 friendshipDto.getId().toString()));
    }

    private boolean isValidStatusChange(FriendshipStatus oldStatus, FriendshipStatus newStatus) {
        if (oldStatus == FriendshipStatus.REQUESTED)
            return true;

        return oldStatus == FriendshipStatus.ACCEPTED && newStatus == FriendshipStatus.BLOCKED;
    }

    private FriendshipDto updateFriendship(FriendshipDto friendshipDto, Friendship friendshipToUpdate) {
        friendshipToUpdate.setStatus(friendshipDto.getStatus());
        Friendship updatedFriendship = friendshipRepository.save(friendshipToUpdate);

        log.info("[x] Friendship was updated to {}: {}", friendshipDto.getStatus(), friendshipToUpdate);
        return modelMapper.map(updatedFriendship, FriendshipDto.class);
    }

}

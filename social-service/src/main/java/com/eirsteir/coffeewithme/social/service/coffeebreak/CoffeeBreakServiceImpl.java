package com.eirsteir.coffeewithme.social.service.coffeebreak;

import com.eirsteir.coffeewithme.commons.domain.coffeebreak.CoffeeBreakDetails;
import com.eirsteir.coffeewithme.commons.security.UserDetailsImpl;
import com.eirsteir.coffeewithme.social.domain.coffeebreak.CoffeeBreak;
import com.eirsteir.coffeewithme.social.domain.university.Campus;
import com.eirsteir.coffeewithme.social.domain.user.User;
import com.eirsteir.coffeewithme.social.dto.FriendshipDto;
import com.eirsteir.coffeewithme.social.repository.CampusRepository;
import com.eirsteir.coffeewithme.social.repository.CoffeeBreakRepository;
import com.eirsteir.coffeewithme.social.service.friendship.FriendshipService;
import com.eirsteir.coffeewithme.social.service.user.UserService;
import com.eirsteir.coffeewithme.social.web.request.CoffeeBreakRequest;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.events.publisher.ResultWithEvents;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class CoffeeBreakServiceImpl implements CoffeeBreakService {

    private DomainEventPublisher domainEventPublisher;

    @Autowired
    private CoffeeBreakRepository coffeeBreakRepository;

    @Autowired
    private FriendshipService friendshipService;

    @Autowired
    private UserService userService;

    @Autowired
    private CampusRepository campusRepository;

    @Autowired
    private ModelMapper modelMapper;

    public CoffeeBreakServiceImpl(DomainEventPublisher domainEventPublisher,
                                 CoffeeBreakRepository coffeeBreakRepository) {
        this.domainEventPublisher = domainEventPublisher;
        this.coffeeBreakRepository = coffeeBreakRepository;
    }

    @Override
    public CoffeeBreakDetails registerCoffeeBreak(CoffeeBreakRequest coffeeBreakRequest, UserDetailsImpl currentUser) {
        CoffeeBreak coffeeBreak = createCoffeeBreak(coffeeBreakRequest, currentUser.getId());
        coffeeBreak = coffeeBreakRepository.save(coffeeBreak);
        log.info("[x] Registered coffee break: {}", coffeeBreak);

        // TODO: 29.05.2020 notify addressees
        CoffeeBreakDetails coffeeBreakDetails = getCoffeeBreakDetails(coffeeBreak);
        ResultWithEvents<CoffeeBreakDetails> coffeeBreakWithEvents = CoffeeBreak.createCoffeeBreak(coffeeBreakDetails);
        publish(coffeeBreakWithEvents);

        return modelMapper.map(coffeeBreak, CoffeeBreakDetails.class);
    }

    private CoffeeBreakDetails getCoffeeBreakDetails(CoffeeBreak coffeeBreak) {
        return modelMapper.map(coffeeBreak, CoffeeBreakDetails.class);
    }

    private CoffeeBreak createCoffeeBreak(CoffeeBreakRequest coffeeBreakRequest, Long currentUserId) {
        User requester = userService.findUserById(currentUserId);
        List<FriendshipDto> addressees = friendshipService.findFriendshipsAtUniversity(requester);
        Campus campus = campusRepository.findById(coffeeBreakRequest.getCampusId()).orElse(null);
        LocalTime scheduledTo = getScheduledToFromNow(coffeeBreakRequest.getScheduledToInMinutes());
        List<User> addresseesAsUsers = getAddresseesAsUsersFrom(addressees, requester);

        return CoffeeBreak.builder()
                .requester(requester)
                .addressees(addresseesAsUsers)
                .campus(campus)
                .scheduledTo(scheduledTo)
                .build();
    }

    private List<User> getAddresseesAsUsersFrom(List<FriendshipDto> addressees, User requester) {
        List<Long> friendsIds = addressees.stream()
                .map(friendshipDto -> {
                    if (friendshipDto.getAddressee().getId()
                            .equals(requester.getId()))
                        return friendshipDto.getRequester().getId();

                    return friendshipDto.getAddressee().getId();
                })
                .collect(Collectors.toList());

        return userService.findByIdIn(friendsIds);
    }

    private LocalTime getScheduledToFromNow(Long scheduledToInMinutes) {
        LocalTime now = LocalTime.now();
        if (scheduledToInMinutes == null)
            return now;

        return now.plusMinutes(scheduledToInMinutes);
    }


    private void publish(ResultWithEvents<CoffeeBreakDetails> coffeeBreakWithEvents) {
        log.info("[x] Publishing {} to {}", coffeeBreakWithEvents, CoffeeBreak.class);
        domainEventPublisher.publish(CoffeeBreak.class, coffeeBreakWithEvents, coffeeBreakWithEvents.events);
    }

}

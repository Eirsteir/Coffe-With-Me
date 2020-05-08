package com.eirsteir.coffeewithme.service.util;

import com.eirsteir.coffeewithme.domain.friendship.Friendship;
import com.eirsteir.coffeewithme.web.dto.FriendshipDto;
import com.eirsteir.coffeewithme.web.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class CWMModelMapper extends ModelMapper {

    public FriendshipDto map(@NotNull Friendship source) {
        UserDto requester = super.map(source.getId().getRequester(), UserDto.class);
        UserDto addressee = super.map(source.getId().getAddressee(), UserDto.class);
        
        return FriendshipDto.builder()
            .requester(requester)
            .addressee(addressee)
            .build();
    }

}

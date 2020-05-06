package com.eirsteir.coffeewithme.service.util;

import com.eirsteir.coffeewithme.domain.request.FriendRequest;
import com.eirsteir.coffeewithme.domain.request.Friendship;
import com.eirsteir.coffeewithme.web.dto.FriendRequestDto;
import com.eirsteir.coffeewithme.web.dto.FriendshipDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class CWMModelMapper extends ModelMapper {

    public FriendRequestDto map(@NotNull FriendRequest source) {
        return FriendRequestDto.builder()
            .id(source.getId())
            .from(source.getFrom().getId())
            .to(source.getTo().getId())
            .status(source.getStatus())
            .build();
    }

    public FriendshipDto map(@NotNull Friendship source) {
        return FriendshipDto.builder()
            .id(source.getId())
            .from(source.getFrom().getId())
            .to(source.getTo().getId())
            .build();
    }

}

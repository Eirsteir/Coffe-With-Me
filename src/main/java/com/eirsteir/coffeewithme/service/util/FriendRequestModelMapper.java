package com.eirsteir.coffeewithme.service.util;

import com.eirsteir.coffeewithme.domain.request.FriendRequest;
import com.eirsteir.coffeewithme.web.dto.FriendRequestDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.util.Assert;
import org.springframework.stereotype.Component;

@Component
public class FriendRequestModelMapper extends ModelMapper {

    public FriendRequestDto map(FriendRequest source) {
        Assert.notNull(source, "source");

        return FriendRequestDto.builder()
            .id(source.getId())
            .from(source.getFrom().getId())
            .to(source.getTo().getId())
            .status(source.getStatus())
            .build();
    }

}

package com.eirsteir.coffeewithme.domain.meeting;

import com.eirsteir.coffeewithme.domain.RequestStatusConverter;

import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true) // automatically apply the conversion logic to all mapped attributes of a FriendRequestStatus type.
public class MeetingStatusConverter implements RequestStatusConverter<MeetingStatus, Integer> {

    public Integer convertToDatabaseColumn(MeetingStatus meetingStatus) {
        return meetingStatus == null ? null : meetingStatus.getValue();
    }

    public MeetingStatus convertToEntityAttribute(Integer value) {
        if  (value == null)
            return null;

        return Stream.of(MeetingStatus.values())
                .filter(v -> v.getValue().equals(value))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}

package com.talentboard.mapper;

import com.talentboard.dto.response.InterviewResponse;
import com.talentboard.entity.Interview;
import org.springframework.stereotype.Component;

@Component
public class InterviewMapper {

    public InterviewResponse toResponse(Interview interview) {
        return InterviewResponse.builder()
                .id(interview.getId())
                .applicationId(interview.getApplication().getId())
                .scheduledDate(interview.getScheduledDate())
                .scheduledTime(interview.getScheduledTime())
                .type(interview.getType())
                .responsibleId(interview.getResponsible().getId())
                .responsibleName(interview.getResponsible().getFullName())
                .result(interview.getResult())
                .observations(interview.getObservations())
                .build();
    }
}

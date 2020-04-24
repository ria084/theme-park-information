package com.ria084.themeparkinformation.batch.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@NoArgsConstructor
public class OpeningHoursNode {

    private List<DetailNode> detailNodeList;

    @Data
    @NoArgsConstructor
    public static class DetailNode {

        @NonNull
        private String targetDate;

        private TimeDetail detail;
    }

    @NoArgsConstructor
    @Data
    public static class TimeDetail {
        private String openTime;

        private String closeTime;

        private String note;

    }
}

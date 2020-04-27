package com.ria084.themeparkinformation.batch.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 運営時間情報を保持する
 */
@Component
@Data
@NoArgsConstructor
public class OpeningHoursNode {

    /**
     * 運営時間情報全体
     */
    private List<DetailNode> detailNodeList;

    /**
     * 日付とそれに対応する運営時間情報
     */
    @Data
    @NoArgsConstructor
    public static class DetailNode {

        /**
         * 日付
         */
        @NonNull
        private String targetDate;

        /**
         * 運営時間情報
         */
        private TimeDetail detail;
    }

    /**
     * 運営時間情報
     */
    @NoArgsConstructor
    @Data
    public static class TimeDetail {
        /**
         * 開園時間
         */
        private String openTime;

        /**
         * 閉園時間
         */
        private String closeTime;

        /**
         * 備考
         */
        private String note;

    }
}

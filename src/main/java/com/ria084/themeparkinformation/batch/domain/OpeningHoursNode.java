package com.ria084.themeparkinformation.batch.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

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
    private Map<String, TimeDetail> detailNodeList;

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

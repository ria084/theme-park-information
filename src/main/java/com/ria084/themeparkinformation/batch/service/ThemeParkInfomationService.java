package com.ria084.themeparkinformation.batch.service;

import com.ria084.themeparkinformation.batch.domain.OptionModel;
import com.ria084.themeparkinformation.batch.exception.ThemeParkInformationException;
import com.ria084.themeparkinformation.batch.service.openinghours.OpeningHoursService;
import com.ria084.themeparkinformation.batch.service.option.OptionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 運営情報を取得するためのサービスクラス
 * <p>
 * Applicationクラスから直接呼び出される、全体の流れをコントロールするクラス
 */
@Slf4j
@AllArgsConstructor
@Service
public class ThemeParkInfomationService {
    /**
     * 起動引数の判定処理を行うクラス
     */
    private final OptionService optionService;

    /**
     * 起動引数の情報を保持するクラス
     */
    private final OptionModel optionModel;

    /**
     * 運営時間情報を取得するクラス
     */
    private final OpeningHoursService openingHoursService;

    /**
     * 基本処理
     *
     * @param args 起動引数
     */
    public void run(String... args) {

        // 引数の判定
        optionService.parseOption(args);

        if (optionModel.getStartDate().isAfter(optionModel.getEndDate())) {
            throw new IllegalArgumentException("開始・終了日の設定が不正です。終了日は開始日より後の日を指定してください");
        }

        try {
            openingHoursService.generateOpeningInformation("LAND");

            openingHoursService.generateOpeningInformation("SEA");
        } catch (ThemeParkInformationException e) {
            log.warn("処理を終了します。エラーメッセージ: " + e.getMessage());
        }

    }

}

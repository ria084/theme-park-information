package com.ria084.themeparkinformation.batch.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * 起動に必要な要素を保持するためのクラス
 * <p>
 * 基本的には起動引数をもとに設定される
 */
@NoArgsConstructor
@Component
@Data
public class OptionModel {

    /**
     * 取得開始日
     */
    private LocalDate startDate;

    /**
     * 取得終了日
     */
    private LocalDate endDate;

}

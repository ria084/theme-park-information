package com.ria084.themeparkinformation.batch.service.option;

import com.ria084.themeparkinformation.batch.domain.OptionModel;
import com.ria084.themeparkinformation.batch.util.UtilDate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;

/**
 * 起動引数の解析と設定を行うクラス
 */
@Service
@AllArgsConstructor
@Slf4j
public class OptionService {
    /**
     * 引数の情報を保持するモデルクラス
     */
    private OptionModel optionModel;

    /**
     * デフォルトの取得月数 (起動日から6か月)
     */
    private static final int ACQUISITION_PERIOD_MONTH = 6;

    /**
     * 起動引数の解析を行い、モデルクラスに設定する
     *
     * @param args 起動引数
     * @throws IllegalArgumentException 引数の設定が間違っている場合
     */
    public void parseOption(String... args) throws IllegalArgumentException {
        // 起動イメージ
        // java -jar xxx.jar -startMonth 取得開始月 -endMonth 取得終了月
        // java -jar xxx.jar -startDate 取得開始日 -endDate 取得終了日

        String startDate = "";
        String endDate = "";
        String startMonth = "";
        String endMonth = "";

        // 起動引数からオプション設定を取得
        for (int i = 0; i < args.length; i++) {
            // 開始日
            if (args[i].contains("startDate")) {
                startDate = args[i + 1];
            }

            if (args[i].contains("startMonth")) {
                startMonth = args[i + 1];
            }

            // 終了日
            if (args[i].contains("endDate")) {
                endDate = args[i + 1];
            }

            if (args[i].contains("endMonth")) {
                endMonth = args[i + 1];
            }
        }

        setStartDate(startDate, startMonth);
        setEndDate(endDate, endMonth);
    }

    /**
     * 取得期間開始日を定義し、モデルクラスに設定する
     *
     * @param inputStartDate  引数 -startDate に設定された文字列
     * @param inputStartMonth 引数 -startMonth に設定された文字列
     * @throws IllegalArgumentException 日付の指定が間違っていた場合
     */
    private void setStartDate(String inputStartDate, String inputStartMonth) throws IllegalArgumentException {

        // 両方指定されていない場合は、実行日を指定
        if (inputStartDate.isEmpty() && inputStartMonth.isEmpty()) {
            log.info("開始日の指定がありませんでした。現在日を開始日として設定します");
            optionModel.setStartDate(LocalDate.now());
            return;
        }

        // 両方指定されている場合は、startDateのほうを優先する
        if (!inputStartDate.isEmpty() && !inputStartMonth.isEmpty()) {
            log.info("開始日と開始月が指定されました。開始日の設定を優先して適用します");
            optionModel.setStartDate(UtilDate.parseStringToDate(inputStartDate));
            return;
        }

        // 開始日のみの場合は、日付の妥当性チェックのみ行う
        if (!inputStartDate.isEmpty()) {
            LocalDate startDate = UtilDate.parseStringToDate(inputStartDate);

            if (startDate.isAfter(LocalDate.now()) || startDate.equals(LocalDate.now())) {
                // 指定日付が現在日付より後または同日の場合はそのまま設定
                optionModel.setStartDate(startDate);
                return;
            } else { // それ以外の場合はエラーに落とす
                throw new IllegalArgumentException("開始日の指定が不正です。現在日以降の日付・月を選択してください");
            }
        }

        // 開始月のみの場合は指定された開始月に応じて、取得開始日を設定
        LocalDate startMonth1stDate = UtilDate.get1stDateOfMonth(inputStartMonth);
        if (startMonth1stDate.isAfter(LocalDate.now())) {
            // 指定日付が現在日付より後の場合はそのまま設定
            optionModel.setStartDate(startMonth1stDate);
        } else if (startMonth1stDate.isEqual(UtilDate.get1stDateOfMonth(YearMonth.now()))) {
            // 指定された開始月の1日と現在日付の1日が同一日なら = 指定月と現在月が同じなら、現在日付を設定
            optionModel.setStartDate(LocalDate.now());
        } else { // それ以外の場合はエラーに落とす
            throw new IllegalArgumentException("開始月の指定が不正です。現在日以降の月を選択してください");
        }
    }

    /**
     * 取得期間終了日を定義し、モデルクラスに設定する
     *
     * @param inputEndDate  引数 -endDate に設定された文字列
     * @param inputEndMonth 引数 -endMonth に設定された文字列
     * @throws IllegalArgumentException 日付の指定が間違っていた場合
     */
    private void setEndDate(String inputEndDate, String inputEndMonth) throws IllegalArgumentException {

        LocalDate defaultEndDate = UtilDate.getLastDateOfMonth(YearMonth.now().plusMonths(ACQUISITION_PERIOD_MONTH));

        // 両方指定されていない場合は、6か月後の最終日を設定する
        if (inputEndDate.isEmpty() && inputEndMonth.isEmpty()) {
            log.info("終了日の指定がありませんでした。現在日+6か月後の最終日を終了日として設定します");
            optionModel.setEndDate(defaultEndDate);
            return;
        }

        // 両方指定されている場合は、endDateのほうを優先する
        if (!inputEndDate.isEmpty() && !inputEndMonth.isEmpty()) {
            log.info("終了日と終了月が指定されました。終了日の設定を優先して適用します");
            optionModel.setEndDate(UtilDate.parseStringToDate(inputEndDate));
            return;
        }

        // 終了日のみの場合は、日付の妥当性チェックのみ行う
        if (!inputEndDate.isEmpty()) {
            LocalDate endDate = UtilDate.parseStringToDate(inputEndDate);

            if (endDate.isAfter(LocalDate.now()) || endDate.equals(LocalDate.now())) {
                // 指定日付が現在日付より後または同日の場合はそのまま設定
                optionModel.setEndDate(endDate);
                return;
            } else { // それ以外の場合はエラーに落とす
                throw new IllegalArgumentException("終了日の指定が不正です。現在日以降の日付・月を選択してください");
            }
        }

        // 開始月のみの場合は指定された終了月に応じて、取得終了日を設定
        LocalDate inputEndLastDate = UtilDate.getLastDateOfMonth(inputEndMonth);

        if (inputEndLastDate.isBefore(defaultEndDate)) {
            // 指定日付がデフォルト最終日より前の場合はそのまま設定
            optionModel.setEndDate(inputEndLastDate);
        } else { // それ以外の場合はエラーに落とす
            throw new IllegalArgumentException("終了月の指定が不正です。現在日以降の月を選択してください");
        }
    }
}

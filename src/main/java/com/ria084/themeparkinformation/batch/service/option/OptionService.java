package com.ria084.themeparkinformation.batch.service.option;

import com.ria084.themeparkinformation.batch.domain.OptionModel;
import com.ria084.themeparkinformation.batch.util.UtilDate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;

@Service
@AllArgsConstructor
@Slf4j
public class OptionService {
    private OptionModel optionModel;

    private static final int ACQUISITION_PERIOD_MONTH = 6;

    public void parseOption(String... args) throws IllegalArgumentException {
        // 起動イメージ
        // java -jar xxx.jar -startMonth 取得開始月 -endMonth 取得終了月

        String startDate = "";
        String endDate = "";

        // 起動引数からオプション設定を取得
        for (int i = 0; i < args.length; i++) {
            // 開始日
            if (args[i].contains("startMonth")) {
                startDate = args[i + 1];
            }

            // 終了日
            if (args[i].contains("endMonth")) {
                endDate = args[i + 1];
            }
        }

        setStartDate(startDate);
        setEndDate(endDate);
    }

    private void setStartDate(String startMonth) throws IllegalArgumentException {

        // 開始日の指定がない場合、実行日を設定
        if(startMonth.isEmpty()){
            log.info("開始日の指定がありませんでした。現在日を開始日として設定します");
            optionModel.setStartDate(LocalDate.now());
            return;
        }

        // 指定された開始月に応じて、取得開始日を設定
        LocalDate inputStartDate = UtilDate.get1stDateOfMonth(startMonth);

        if (inputStartDate.isAfter(LocalDate.now())) {
            // 指定日付が現在日付より後の場合はそのまま設定
            optionModel.setStartDate(inputStartDate);
        } else if (inputStartDate.atStartOfDay().isEqual(LocalDate.now().atStartOfDay())) {
            // 指定された開始月の1日と現在日付の1日が同一日なら = 指定月と現在月が同じなら、現在日付を設定
            optionModel.setStartDate(LocalDate.now());
        } else { // それ以外の場合はエラーに落とす
            throw new IllegalArgumentException("開始月の指定が不正です。現在日以降の月を選択してください");
        }
    }

    private void setEndDate(String endMonth) throws IllegalArgumentException {
        LocalDate defaultEndDate = UtilDate.getLastDateOfMonth(YearMonth.now().plusMonths(ACQUISITION_PERIOD_MONTH));

        // 終了日の設定がない場合、6か月後の最終日を設定する
        if(endMonth.isEmpty()){
            optionModel.setEndDate(defaultEndDate);
            return;
        }

        // 指定された開始月に応じて、取得開始日を設定
        LocalDate inputEndDate = UtilDate.getLastDateOfMonth(endMonth);

        if (inputEndDate.isBefore(defaultEndDate)) {
            // 指定日付が現在日付より前の場合はそのまま設定
            optionModel.setStartDate(inputEndDate);
        } else if (inputEndDate.atStartOfDay().isEqual(LocalDate.now().atStartOfDay())) {
            // 指定された開始月の1日と現在日付の1日が同一日なら = 指定月と現在月が同じなら、現在日付を設定
            optionModel.setStartDate(LocalDate.now());
        } else { // それ以外の場合はエラーに落とす
            throw new IllegalArgumentException("終了月の指定が不正です。現在日以降の月を選択してください");
        }
    }
}

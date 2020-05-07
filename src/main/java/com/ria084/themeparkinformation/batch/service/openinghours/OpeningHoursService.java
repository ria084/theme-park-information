package com.ria084.themeparkinformation.batch.service.openinghours;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ria084.themeparkinformation.batch.domain.OpeningHoursNode;
import com.ria084.themeparkinformation.batch.domain.OptionModel;
import com.ria084.themeparkinformation.batch.exception.ThemeParkInformationException;
import com.ria084.themeparkinformation.batch.util.UtilDate;
import com.ria084.themeparkinformation.batch.util.UtilFile;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class OpeningHoursService {

    /**
     * 起動引数の情報を保持するクラス
     */
    private final OptionModel optionModel;

    /**
     * 規定の待機時間
     */
    private static final long SLEEP_MILLISECONDS = 3000;

    /**
     * ベースとなるURL
     */
    private static final Map<String, String> BASE_URL_SET =
            Collections.unmodifiableMap(new HashMap<String, String>() {
                {
                    put("LAND", "https://www.tokyodisneyresort.jp/tdl/daily/calendar/%d/");
                    put("SEA", "https://www.tokyodisneyresort.jp/tds/daily/calendar/%d/");
                }
            });

    /**
     * リクエスト時の User-Agent
     */
    private static final String USER_AGENT_CHROME = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.163 Safari/537.36";

    /**
     * 運営時間情報を取得し、ファイルへの出力を行う
     *
     * @param key ベースURLに対応したキー名
     * @throws ThemeParkInformationException sleep処理に失敗した場合、jsonへの変換に失敗した場合
     */
    public void generateOpeningInformation(String key) throws ThemeParkInformationException {
        Map<String, OpeningHoursNode.TimeDetail> detailNodeList = new HashMap<>();

        for (LocalDate targetDate = optionModel.getStartDate(); !targetDate.isAfter(optionModel.getEndDate()); targetDate = targetDate.plusDays(1)) {
            OpeningHoursNode.TimeDetail detail = getOpeningHours(key, UtilDate.parseLocalDateToString(targetDate));
            detailNodeList.put(UtilDate.parseLocalDateToString(targetDate), detail);
            // スリープ
            try {
                Thread.sleep(SLEEP_MILLISECONDS);
            } catch (InterruptedException e) {
                throw new ThemeParkInformationException("sleep処理に失敗しました", e);
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        String json;
        try {
            json = mapper.writeValueAsString(detailNodeList);
        } catch (JsonProcessingException e) {
            throw new ThemeParkInformationException("jsonへの変換に失敗しました", e);
        }

        // 出力処理
        UtilFile.generateFile(json, key);

    }

    /**
     * 運営時間情報を取得する(1日分)
     *
     * @param key        ベースURLに対応したキー名
     * @param targetDate 取得対象の日付(uuuuMMdd)
     * @return 運営時間情報
     * @throws ThemeParkInformationException 情報取得に失敗した場合
     */
    private OpeningHoursNode.TimeDetail getOpeningHours(String key, String targetDate) throws ThemeParkInformationException {
        // 結果格納用オブジェクトを生成
        OpeningHoursNode.TimeDetail detail = new OpeningHoursNode.TimeDetail();

        // 取得先urlの日付部分を置換
        String url = String.format(BASE_URL_SET.get(key), Integer.parseInt(targetDate));

        // 取得処理
        Document document = null;
        try {
            document = Jsoup.connect(url).userAgent(USER_AGENT_CHROME).get();
        } catch (IOException e) {
            throw new ThemeParkInformationException("情報取得処理に失敗しました", e);
        }
        String element = document.getElementsByClass("time").first().text();

        // :, - が含まれる場合は、開園時間・閉園時間を抽出してセット
        if (element.contains("-") && element.contains(":")) {
            String[] time = element.split("-");
            detail.setOpenTime(time[0].trim());
            detail.setCloseTime(time[1].trim());
        } else { // :, - が含まれない場合は、開園時間・閉園時間はnullにして、値をnoteにいれる
            detail.setNote(element);
        }

        return detail;
    }
}

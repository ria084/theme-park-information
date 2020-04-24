package com.ria084.themeparkinformation.batch.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ria084.themeparkinformation.batch.domain.OpeningHoursNode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@AllArgsConstructor
public class ThemeParkInfomationService {

    private static final String BASE_URL = "https://www.tokyodisneyresort.jp/tdl/daily/calendar/%d/";
    private static final int ACQUISITION_PERIOD_MONTH = 6;
    private static final long SLEEP_MILLISECONDS = 3000;
    private static final String USER_AGENT_CHROME = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.163 Safari/537.36";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuuMMdd");

    public static void run(String... args) throws IOException, InterruptedException {

        List<OpeningHoursNode.DetailNode> detailNodeList = new ArrayList<>();

        // 取得開始日 = 実行した日 を取得
        LocalDate startDate = LocalDate.now();

        // 取得最終日を取得 いったん6か月後の月を取得してから最終日の日付を出す
        LocalDate endBaseDate = LocalDate.now().plusMonths(ACQUISITION_PERIOD_MONTH);
//        LocalDate endDate = LocalDate.of(endBaseDate.getYear(), endBaseDate.getMonth(), endBaseDate.lengthOfMonth());

        LocalDate endDate = LocalDate.now();

        OpeningHoursNode.DetailNode node = new OpeningHoursNode.DetailNode();

        for (LocalDate targetDate = startDate; !targetDate.isAfter(endDate); targetDate = targetDate.plusDays(1)) {

            // 結果格納用オブジェクトを生成
            node.setTargetDate(targetDate.format(formatter));
            OpeningHoursNode.TimeDetail detail = new OpeningHoursNode.TimeDetail();

            // 取得先urlの日付部分を置換
            String url = String.format(BASE_URL, Integer.parseInt(targetDate.format(formatter)));

            // 取得処理
            Document document = Jsoup.connect(url).userAgent(USER_AGENT_CHROME).get();
            String element = document.getElementsByClass("time").first().text();

            // 出力処理
            log.info(element);

            // :, - が含まれる場合は、開園時間・閉園時間を抽出してセット
            if (element.contains("-") && element.contains(":")) {
                String[] time = element.split("-");
                detail.setOpenTime(time[0].trim());
                detail.setCloseTime(time[1].trim());
            } else { // :, - が含まれない場合は、開園時間・閉園時間はnullにして、値をnoteにいれる
                detail.setNote(element);
            }
            node.setDetail(detail);
            detailNodeList.add(node);
        }

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(detailNodeList);
        log.info(json);

        // スリープ
        Thread.sleep(SLEEP_MILLISECONDS);
    }
}

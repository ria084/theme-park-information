package com.ria084.themeparkinformation.batch.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@AllArgsConstructor
public class ThemeParkInfomationService {

    private static final String BASE_URL = "https://www.tokyodisneyresort.jp/tdl/daily/calendar/%d/";
    private static final int ACQUISITION_PERIOD_MONTH = 6;
    private static final long SLEEP_MILLISECONDS = 3000;
    private static final String USER_AGENT_CHROME = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.163 Safari/537.36";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuuMMdd");

    public static void run(String... args) throws IOException, InterruptedException {

        // 取得開始日 = 実行した日 を取得
        LocalDate startDate = LocalDate.now();

        // 取得最終日を取得 いったん6か月後の月を取得してから最終日の日付を出す
        LocalDate endBaseDate = LocalDate.now().plusMonths(ACQUISITION_PERIOD_MONTH);
//        LocalDate endDate = LocalDate.of(endBaseDate.getYear(), endBaseDate.getMonth(), endBaseDate.lengthOfMonth());

        LocalDate endDate = LocalDate.now();

        for(LocalDate targetDate = startDate; !targetDate.isAfter(endDate); targetDate = targetDate.plusDays(1)){
            // 取得先urlの日付部分を置換
            String url = String.format(BASE_URL, Integer.parseInt(targetDate.format(formatter)));

            // 取得処理
            Document document = Jsoup.connect(url).userAgent(USER_AGENT_CHROME).get();
            Elements elements = document.getElementsByClass("time ");

            // 出力処理
            for (Element element : elements) {
                log.info(element.text());
            }
            // スリープ
            Thread.sleep(SLEEP_MILLISECONDS);
        }


    }
}

package com.ria084.themeparkinformation.batch.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ria084.themeparkinformation.batch.domain.OpeningHoursNode;
import com.ria084.themeparkinformation.batch.domain.OptionModel;
import com.ria084.themeparkinformation.batch.service.option.OptionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class ThemeParkInfomationService {
    private final OptionService optionService;
    private final OptionModel optionModel;

    private static final String BASE_URL = "https://www.tokyodisneyresort.jp/tdl/daily/calendar/%d/";
    private static final long SLEEP_MILLISECONDS = 3000;
    private static final String USER_AGENT_CHROME = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.163 Safari/537.36";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuuMMdd");

    public void run(String... args) throws IOException, InterruptedException, IllegalArgumentException {

        // 引数の判定
        optionService.parseOption(args);

        if (optionModel.getStartDate().isAfter(optionModel.getEndDate())) {
            throw new IllegalArgumentException("開始・終了日の設定が不正です。終了日は開始日より後の日を指定してください");
        }

        List<OpeningHoursNode.DetailNode> detailNodeList = new ArrayList<>();
        OpeningHoursNode.DetailNode node = new OpeningHoursNode.DetailNode();

        for (LocalDate targetDate = optionModel.getStartDate(); !targetDate.isAfter(optionModel.getEndDate()); targetDate = targetDate.plusDays(1)) {

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

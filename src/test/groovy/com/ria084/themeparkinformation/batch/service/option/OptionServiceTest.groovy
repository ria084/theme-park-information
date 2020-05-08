package com.ria084.themeparkinformation.batch.service.option

import com.ria084.themeparkinformation.batch.domain.OptionModel
import com.ria084.themeparkinformation.batch.util.UtilDate
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import java.time.LocalDate
import java.time.YearMonth

@SpringBootTest
class OptionServiceTest extends Specification {
    def optionModel
    def optionService

    LocalDate startDate = LocalDate.now()
    String startDateString = startDate.format(UtilDate.datetimeFormatter)

    YearMonth startMonth = YearMonth.now()
    String startMonthString = startMonth.format(UtilDate.yearmonthFormatter)

    LocalDate endDate = LocalDate.now().plusDays(5)
    String endDateString = endDate.format(UtilDate.datetimeFormatter)

    YearMonth endMonth = YearMonth.now().plusMonths(2)
    String endMonthString = endMonth.format(UtilDate.yearmonthFormatter)

    String argsFormat_DM = "startDate %s endMonth %s"
    String argsFormat_MD = "startMonth %s endDate %s"

    private static final int ACQUISITION_PERIOD_MONTH = 6;

    void setup() {
        optionModel = Spy(OptionModel)
        optionService = Spy(OptionService, constructorArgs: [optionModel])
    }

    def "ParseOption_startDate_endMonth_通常パターン"() {
        given:
        String[] args = String.format(argsFormat_DM, startDateString, endMonthString).split(" ")

        when:
        optionService.parseOption(args)

        then:
        optionModel.getStartDate() == startDate
        optionModel.getEndDate() == endMonth.atEndOfMonth()
    }

    def "ParseOption_startMonth_endDate_通常パターン"() {
        given:
        String[] args = String.format(argsFormat_MD, startMonthString, endDateString).split(" ")

        when:
        optionService.parseOption(args)

        then:
        optionModel.getStartDate() == startDate
        optionModel.getEndDate() == endDate
    }

    def "ParseOption_指定なし"() {
        given:
        String[] args = ""

        when:
        optionService.parseOption(args)

        then:
        optionModel.getStartDate() == LocalDate.now()
        optionModel.getEndDate() == YearMonth.now().plusMonths(ACQUISITION_PERIOD_MONTH).atEndOfMonth()

    }

    def "ParseOption_startMonth_startDate_両方指定"() {
        given:
        String[] args = ["startDate", startDateString, "startMonth", startMonthString]

        when:
        optionService.parseOption(args)

        then:
        optionModel.getStartDate() == startDate
        optionModel.getEndDate() == YearMonth.now().plusMonths(ACQUISITION_PERIOD_MONTH).atEndOfMonth()

    }

    def "ParseOption_startDate_過去日指定"() {
        given:
        def oldDate = LocalDate.now().minusDays(3)
        def oldDateString = oldDate.format(UtilDate.datetimeFormatter)
        String[] args = ["startDate", oldDateString]

        when:
        optionService.parseOption(args)

        then:
        thrown(IllegalArgumentException)

    }

    def "ParseOption_startMonth_現在日より後指定"() {
        given:
        def date = YearMonth.now().plusMonths(3)
        def dateString = date.format(UtilDate.yearmonthFormatter)
        String[] args = ["startMonth", dateString]

        when:
        optionService.parseOption(args)

        then:
        optionModel.getStartDate() == date.atDay(1)
        optionModel.getEndDate() == YearMonth.now().plusMonths(ACQUISITION_PERIOD_MONTH).atEndOfMonth()
    }

    def "ParseOption_startMonth_過去月指定"() {
        given:
        def oldDate = YearMonth.now().minusMonths(1)
        def oldDateString = oldDate.format(UtilDate.yearmonthFormatter)
        String[] args = ["startMonth", oldDateString]

        when:
        optionService.parseOption(args)

        then:
        thrown(IllegalArgumentException)
    }

    def "ParseOption_endMonth_endDate_両方指定"() {
        given:
        String[] args = ["endDate", endDateString, "endMonth", endMonthString]

        when:
        optionService.parseOption(args)

        then:
        optionModel.getStartDate() == LocalDate.now()
        optionModel.getEndDate() == endDate

    }

    def "ParseOption_endDate_過去日指定"() {
        given:
        def oldDate = LocalDate.now().minusDays(1)
        def oldDateString = oldDate.format(UtilDate.datetimeFormatter)
        String[] args = ["endDate", oldDateString]

        when:
        optionService.parseOption(args)

        then:
        thrown(IllegalArgumentException)
    }

    def "ParseOption_endMonth_7か月後指定"() {
        given:
        def date = YearMonth.now().plusMonths(ACQUISITION_PERIOD_MONTH + 1)
        def dateString = date.format(UtilDate.yearmonthFormatter)
        String[] args = ["endMonth", dateString]

        when:
        optionService.parseOption(args)

        then:
        thrown(IllegalArgumentException)
    }
}

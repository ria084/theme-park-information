package com.ria084.themeparkinformation.batch.util

import spock.lang.Specification

import java.time.LocalDate
import java.time.YearMonth

class UtilDateTest extends Specification {

    def "Get1stDateOfMonth"() {
        given:
        String yearMonth = "202012"
        LocalDate expect = LocalDate.of(2020, 12, 1)

        when:
        LocalDate result = UtilDate.get1stDateOfMonth(yearMonth)

        then:
        result == expect
    }

    def "TestGet1stDateOfMonth"() {
        given:
        YearMonth yearMonth = YearMonth.of(2020, 12)
        LocalDate expect = LocalDate.of(2020, 12, 1)

        when:
        LocalDate result = UtilDate.get1stDateOfMonth(yearMonth)

        then:
        result == expect
    }

    def "GetLastDateOfMonth"() {
        given:
        String yearMonth = "202002"
        LocalDate expect = LocalDate.of(2020, 2, 29)

        when:
        LocalDate result = UtilDate.getLastDateOfMonth(yearMonth)

        then:
        result == expect
    }

    def "TestGetLastDateOfMonth"() {
        given:
        YearMonth yearMonth = YearMonth.of(2020, 2)
        LocalDate expect = LocalDate.of(2020, 2, 29)

        when:
        LocalDate result = UtilDate.getLastDateOfMonth(yearMonth)

        then:
        result == expect
    }

    def "ParseStringToDate"() {
        given:
        String localDate = "20200415"
        LocalDate expect = LocalDate.of(2020, 4, 15)

        when:
        LocalDate result = UtilDate.parseStringToDate(localDate)

        then:
        result == expect
    }

    def "ParseLocalDateToString"() {
        given:
        LocalDate localDate = LocalDate.of(2020, 4, 15)
        String expect = "20200415"

        when:
        String result = UtilDate.parseLocalDateToString(localDate)

        then:
        result == expect
    }
}

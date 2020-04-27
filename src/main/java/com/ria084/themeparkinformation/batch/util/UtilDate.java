package com.ria084.themeparkinformation.batch.util;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Service
public class UtilDate {

    public static final DateTimeFormatter yearmonthFormatter = DateTimeFormatter.ofPattern("uuuuMM");

    public static LocalDate get1stDateOfMonth(String yearMonth){
        return get1stDateOfMonth(YearMonth.parse(yearMonth, yearmonthFormatter));

    }

    public static LocalDate get1stDateOfMonth(YearMonth yearMonth){
        return yearMonth.atDay(1);
    }

    public static LocalDate getLastDateOfMonth(String yearMonth){
        return getLastDateOfMonth(YearMonth.parse(yearMonth, yearmonthFormatter));

    }

    public static LocalDate getLastDateOfMonth(YearMonth yearMonth){
        return yearMonth.atEndOfMonth();
    }


}

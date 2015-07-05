package com.ets.util;

import com.ets.Application;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Yusuf
 */
public class DateUtil {

    private static final SimpleDateFormat dfInput = new SimpleDateFormat("yyMMdd");
    private static final SimpleDateFormat dfOutput = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat dfInput1 = new SimpleDateFormat("ddMMMyy");
    private static Calendar cal = Calendar.getInstance();

    public static Date minusDays(Date dateInstance, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(dateInstance);
        c.add(Calendar.DATE, days * -1);
        Date date = c.getTime();
        return date;
    }

    public static Date yyMMddToDate(String yyMMdd) {

        Date date = null;
        try {
            date = dfOutput.parse(dfOutput.format(dfInput.parse(yyMMdd)));
        } catch (ParseException ex) {
            Logger.getLogger(DateUtil.class.getName()).log(Level.SEVERE, null, ex);
        }

        return date;
    }

    /**
     * If current month is less then provided month then provided month is
     * actually in next year.
     *
     * @param ddmm
     * @return
     */
    public static Date ddmmToDate(String ddmm) {
        Integer current_year = cal.get(Calendar.YEAR);
        Integer current_month = cal.get(Calendar.MONTH);
        Integer provided_month = stringToMonthValue(ddmm.substring(2));

        if (provided_month < current_month) {
            current_year++;
        }

        String tempDate = ddmm.concat(current_year.toString());
        SimpleDateFormat dfIn = new SimpleDateFormat("ddMMMyyyy");
        SimpleDateFormat dfOut = new SimpleDateFormat("yyyy-MM-dd");
        String finalDate = null;
        Date date = null;
        try {
            finalDate = dfOut.format(dfIn.parse(tempDate));
            date = dfOut.parse(finalDate);
        } catch (ParseException ex) {
            System.out.println("Exception parsing date..." + ex);
        }
        return date;
    }

    public static String dateTOddmm(Date date) {
        String _date = dateToString(date, "ddMMMyyyy");
        return _date.substring(0, 5).toUpperCase();
    }

    public static Date refundDate(String dateString) {

        Date date = null;

        try {
            String d = dfOutput.format(dfInput1.parse(dateString));
            date = dfOutput.parse(d);

        } catch (ParseException ex) {
            Logger.getLogger(DateUtil.class.getName()).log(Level.SEVERE, null, ex);
        }

        return date;
    }

    public static String dateToString(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static Date stringToDate(String dateString, String dateStringFormat) {
        DateFormat dateFormat = new SimpleDateFormat(dateStringFormat);
        Date date = null;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException ex) {
            Logger.getLogger(DateUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }

    public static String dateToString(Date date) {
        return dateToString(date, Application.get("dateformat"));
    }

    public static Date stringToDate(String dateString) {
        return stringToDate(dateString, Application.get("dateformat"));
    }

    public static Integer getYY() {
        SimpleDateFormat dfYear = new SimpleDateFormat("yy");
        String year = dfYear.format(cal.getTime());
        Integer currentYear = Integer.valueOf(year);
        return currentYear;
    }

    public static int stringToMonthValue(String mm) {

        switch (mm) {
            case "JAN":
                return 0;
            case "FEB":
                return 1;
            case "MAR":
                return 2;
            case "APR":
                return 3;
            case "MAY":
                return 4;
            case "JUN":
                return 5;
            case "JUL":
                return 6;
            case "AUG":
                return 7;
            case "SEP":
                return 8;
            case "OCT":
                return 9;
            case "NOV":
                return 10;
            case "DEC":
                return 11;
            default:
                return -1;
        }
    }
}

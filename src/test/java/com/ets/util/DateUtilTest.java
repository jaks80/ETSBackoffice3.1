package com.ets.util;

import java.util.Date;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Yusuf
 */
public class DateUtilTest {
    
    public DateUtilTest() {
    }
    
    @Before
    public void setUp() {
    }

    //@Test
    public void testYyMMddToDate() {
        System.out.println("yyMMddToDate");
        String yyMMdd = "";
        Date expResult = null;
        Date result = DateUtil.yyMMddToDate(yyMMdd);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * This test passes but can not leave enabled because its date specific test.
     * This one will fail next year;
     */
    //@Test
    public void testDdmmToDate() {
        System.out.println("ddmmToDate");
        String ddmm = "23JAN";
        Date expResult = DateUtil.stringToDate("23JAN2016", "ddMMMyyyy");
        Date result = DateUtil.ddmmToDate(ddmm);
        assertEquals(expResult, result);   
        
        ddmm = "01SEP";
        expResult = DateUtil.stringToDate("01SEP2015", "ddMMMyyyy");
        result = DateUtil.ddmmToDate(ddmm);
        assertEquals(expResult, result);
        
        String MMM = DateUtil.dateTOddmm(DateUtil.stringToDate("01SEP2015", "ddMMMyyyy"));
        String expMMM = "SEP";
        assertEquals(MMM, expMMM);
        
        MMM = DateUtil.dateTOddmm(DateUtil.stringToDate("23JAN2016", "ddMMMyyyy"));
        expMMM = "JAN";
        assertEquals(MMM, expMMM);
    }

    //@Test
    public void testRefundDate() {
        System.out.println("refundDate");
        String dateString = "";
        Date expResult = null;
        Date result = DateUtil.refundDate(dateString);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    //@Test
    public void testDateToString_Date_String() {
        System.out.println("dateToString");
        Date date = null;
        String format = "";
        String expResult = "";
        String result = DateUtil.dateToString(date, format);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    //@Test
    public void testStringToDate_String_String() {
        System.out.println("stringToDate");
        String dateString = "";
        String dateStringFormat = "";
        Date expResult = null;
        Date result = DateUtil.stringToDate(dateString, dateStringFormat);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    //@Test
    public void testDateToString_Date() {
        System.out.println("dateToString");
        Date date = null;
        String expResult = "";
        String result = DateUtil.dateToString(date);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    //@Test
    public void testStringToDate_String() {
        System.out.println("stringToDate");
        String dateString = "";
        Date expResult = null;
        Date result = DateUtil.stringToDate(dateString);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    //@Test
    public void testGetYY() {
        System.out.println("getYY");
        Integer expResult = null;
        Integer result = DateUtil.getYY();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testStringToMonthValue() {
        System.out.println("stringToMonthValue");
        String mm = "AUG";
        int expResult = 7;
        int result = DateUtil.stringToMonthValue(mm);
        assertEquals(expResult, result);
        
        expResult = 11;
        mm = "DEC";
        
        result = DateUtil.stringToMonthValue(mm);
        assertEquals(expResult, result);
        
    }
    
}

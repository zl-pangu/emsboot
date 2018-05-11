package com.zhph.util;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

public class DateUtilTest {

	@Test
	public void testGetFirstDayStrOfTheYear() throws Exception {
		System.out.println("the last day of this year::"+DateUtil.getFirstDayStrOfTheYear(new Date(), "yyyy-MM-dd"));
		System.out.println("the last day of this year::"+DateUtil.getFirstDayStrOfTheYear(new Date(80,12,22), "yyyy-MM-dd"));
		}

	@Test
	public void testGetLastDayStrOfTheYear() throws Exception {
		System.out.println("the first day of this year::"+DateUtil.getLastDayStrOfTheYear(new Date(), "yyyy-MM-dd"));
		System.out.println("the first day of this year::"+DateUtil.getLastDayStrOfTheYear(new Date(80,12,22), "yyyy-MM-dd"));

	}

}

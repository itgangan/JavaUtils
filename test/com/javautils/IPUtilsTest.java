package com.javautils;

import org.junit.Assert;
import org.junit.Test;

public class IPUtilsTest {

	public void testFindGeography() {
		String address = "211.157.164.1";
		String result = IPUtils.findGeography(address);
		System.out.println(result);

	}

	@Test
	public void testStringIPToLong() {
		String stringIP = "219.239.110.138";
		long expected = 3689901706L;
		long actual = IPUtils.StringIPToLong(stringIP);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testLongIPtoString() {
		long longIP = 3689901706L;
		String expected = "219.239.110.138";
		String actual = IPUtils.LongIPToString(longIP);

		Assert.assertEquals(expected, actual);
	}
}

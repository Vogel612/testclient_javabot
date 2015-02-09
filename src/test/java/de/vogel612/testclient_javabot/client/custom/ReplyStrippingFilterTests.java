package de.vogel612.testclient_javabot.client.custom;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ReplyStrippingFilterTests {

	private ReplyStrippingFilter cut = new ReplyStrippingFilter();

	@Test
	public void doesNotStripInternalNumber() {
		final String original = "some random text with :352 reply-like content";
		final String result = cut.filter(original);

		assertEquals(original, result);
	}

	@Test
	public void stripsSingleDigitReplies() {
		final String original = ":1 Some random text";
		final String result = cut.filter(original);

		assertEquals(original.substring(3), result);
	}

	@Test
	public void stripsMultipleDigitReplies() {
		final String original = ":124 Some random text";
		final String result = cut.filter(original);

		assertEquals(original.substring(5), result);
	}
}

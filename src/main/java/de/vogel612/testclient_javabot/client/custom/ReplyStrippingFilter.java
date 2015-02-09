package de.vogel612.testclient_javabot.client.custom;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReplyStrippingFilter implements DisplayFilter {

	private static final Pattern REPLY_PATTERN = Pattern.compile("^:\\d*? ");

	@Override
	public String filter(String original) {
		Matcher m = REPLY_PATTERN.matcher(original);
		return m.replaceAll("");
	}
}

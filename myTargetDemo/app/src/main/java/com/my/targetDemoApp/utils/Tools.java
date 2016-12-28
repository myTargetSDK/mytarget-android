package com.my.targetDemoApp.utils;

import java.util.ArrayList;
import java.util.List;

public class Tools
{
	public static String[] split(final String str, final String separatorChars)
	{
		return splitWorker(str, separatorChars, -1, false);
	}

	private static String[] splitWorker(final String str,
	                                    final String separatorChars,
	                                    final int max,
	                                    final boolean preserveAllTokens)
	{
		if (str == null)
		{
			return null;
		}
		final int len = str.length();
		if (len == 0)
		{
			return new String[0];
		}
		final List<String> list = new ArrayList<String>();
		int sizePlus1 = 1;
		int i = 0, start = 0;
		boolean match = false;
		boolean lastMatch = false;
		if (separatorChars == null)
		{
			// Null separator means use whitespace
			while (i < len)
			{
				if (Character.isWhitespace(str.charAt(i)))
				{
					if (match || preserveAllTokens)
					{
						lastMatch = true;
						if (sizePlus1++ == max)
						{
							i = len;
							lastMatch = false;
						}
						list.add(str.substring(start, i));
						match = false;
					}
					start = ++i;
					continue;
				}
				lastMatch = false;
				match = true;
				i++;
			}
		} else if (separatorChars.length() == 1)
		{
			// Optimise 1 character case
			final char sep = separatorChars.charAt(0);
			while (i < len)
			{
				if (str.charAt(i) == sep)
				{
					if (match || preserveAllTokens)
					{
						lastMatch = true;
						if (sizePlus1++ == max)
						{
							i = len;
							lastMatch = false;
						}
						list.add(str.substring(start, i));
						match = false;
					}
					start = ++i;
					continue;
				}
				lastMatch = false;
				match = true;
				i++;
			}
		} else
		{
			// standard case
			while (i < len)
			{
				if (separatorChars.indexOf(str.charAt(i)) >= 0)
				{
					if (match || preserveAllTokens)
					{
						lastMatch = true;
						if (sizePlus1++ == max)
						{
							i = len;
							lastMatch = false;
						}
						list.add(str.substring(start, i));
						match = false;
					}
					start = ++i;
					continue;
				}
				lastMatch = false;
				match = true;
				i++;
			}
		}
		if (match || preserveAllTokens && lastMatch)
		{
			list.add(str.substring(start, i));
		}
		return list.toArray(new String[list.size()]);
	}
}

package com.cube.storm.language.lib.processor;

import android.support.annotation.NonNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to deal with methods on variable localisations
 *
 * @author Callum Taylor
 * @project LightningLanguage
 */
public class MethodProcessor
{
	private static final Pattern pattern = Pattern.compile("\\{[0-9a-zA-Z_\\-\\?\\.\\(\\)\\'\\\"\\<\\>\\,\\\\\\/\\!\\@\\$\\%\\^\\&\\*]+((\\.)[0-9a-zA-Z]+)+\\}");
	private static final Pattern methods = Pattern.compile("((\\.)[0-9a-zA-Z]+)");

	/**
	 * Processes {@param variable} based on methods attached to it
	 *
	 * @param variable
	 */
	@NonNull
	public String process(String mappedVariable, @NonNull String string)
	{
		Matcher matcher = pattern.matcher(string);
		String originalString = new String(mappedVariable);

		while (matcher.find())
		{
			String found = matcher.group();
			Matcher methodMatcher = methods.matcher(found);

			while (methodMatcher.find())
			{
				String method = methodMatcher.group();

				if (method.equals(".upperCase"))
				{
					mappedVariable = upperCase(mappedVariable);
				}
			}
		}

		string = string.replaceAll("\\{" + originalString + "(\\.[0-9a-zA-Z]+)\\}", mappedVariable);
		string = string.replaceAll("\\{" + originalString + "\\}", originalString);

		return string;
	}

	protected String upperCase(String input)
	{
		return input.toUpperCase();
	}
}

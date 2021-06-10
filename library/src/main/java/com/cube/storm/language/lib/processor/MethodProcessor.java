package com.cube.storm.language.lib.processor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to deal with methods and placeholder tidyup on variable localisations.
 * <br />
 * Methods are processed
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
	 * @param mappedVariable The replacement variable for the localisation
	 * @param string The whole string including the replaced variables. String may look like {@code {replaced variable}}
	 * 				 or {@code {replaced variable.methodOne.methodTwo}}
	 *
	 * @return The processed string with all placeholder text removed and methods processed
	 */
	@NonNull
	public String process(@Nullable String mappedVariable, @NonNull String string)
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
				mappedVariable = matchMethod(method, mappedVariable);
			}
		}

		string = string.replaceAll("\\{" + Pattern.quote(originalString) + "(\\.[0-9a-zA-Z]+)\\}", mappedVariable);
		string = string.replaceAll("\\{" + Pattern.quote(originalString) + "\\}", originalString);

		return string;
	}

	/**
	 * Matches the method param for actual java executable method
	 *
	 * @param method The method to match. Will be prefixed with a period and may contain parenthesis with arguments
	 * @param variable The variable to operate on
	 *
	 * @return The processed string
	 */
	protected String matchMethod(@NonNull String method, @Nullable String variable)
	{
		if (method.equals(".upperCase"))
		{
			return upperCase(variable);
		}

		return variable;
	}

	/**
	 * Uppercases the input string
	 *
	 * @param input The string to uppercase
	 *
	 * @return The upper cased string or empty string
	 */
	@NonNull
	protected String upperCase(@Nullable String input)
	{
		return input == null ? "" : input.toUpperCase();
	}
}

package com.cube.storm.language.lib.processor;

import androidx.annotation.Nullable;

import com.cube.storm.LanguageSettings;
import com.cube.storm.util.lib.processor.Processor;

/**
 * Processes a string input into it's matching localisation in the default language or fallback language
 * if one is set.
 * <p/>
 * This class should be included with your LightningUi settings module by using the {@code textProcessor(Processor)} method in {@code UiSettings}
 *
 * @author Callum Taylor
 * @project LightningLanguage
 */
public class LanguageTextProcessor extends Processor<String, String>
{
	/**
	 * Processes a string (as a key) and finds the value from the {@link com.cube.storm.language.lib.manager.LanguageManager}.
	 *
	 * @param input The key to look up
	 *
	 * @return The found string, or null/empty if the value wasn't found
	 */
	@Nullable @Override public String process(@Nullable String input)
	{
		if (input == null)
		{
			return "";
		}

		return LanguageSettings.getInstance().getLanguageManager().getValue(input);
	}
}

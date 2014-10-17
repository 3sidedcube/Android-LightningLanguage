package com.cube.storm.language.lib.processor;

import android.support.annotation.Nullable;

import com.cube.storm.LanguageSettings;
import com.cube.storm.util.lib.processor.Processor;

/**
 * Processes a string input into it's matching localisation in the default language or fallback language
 * if one is set
 *
 * @author Callum Taylor
 * @project StormLanguage
 */
public class LanguageTextProcessor extends Processor<String, String>
{
	@Nullable @Override public String process(@Nullable String input)
	{
		if (input == null)
		{
			return "";
		}

		return LanguageSettings.getInstance().getLanguageManager().getValue(input);
	}
}

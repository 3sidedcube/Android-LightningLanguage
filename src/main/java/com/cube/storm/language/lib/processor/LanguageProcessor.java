package com.cube.storm.language.lib.processor;

import com.cube.storm.language.data.Language;
import com.cube.storm.util.lib.processor.GsonProcessor;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Base processor class for matching a string with one defined in the language map
 *
 * @author Callum Taylor
 * @project StormLanguage
 */
public class LanguageProcessor extends GsonProcessor<Language>
{
	@Override public Language deserialize(JsonElement json, Type arg1, JsonDeserializationContext arg2) throws JsonParseException
	{
		Language language = new Language();

		if (json != null && json.isJsonObject())
		{
			Map<String, String> decoded = new Gson().fromJson(json, new TypeToken<Map<String, String>>(){}.getType());
			language.setValues(decoded);

			return language;
		}

		return null;
	}
}

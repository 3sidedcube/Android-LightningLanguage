package com.cube.storm.language.lib.processor;

import android.support.annotation.Nullable;

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
 * Base processor class for inflating a language file into a {@link com.cube.storm.language.data.Language} object.
 *
 * @author Callum Taylor
 * @project LightningLanguage
 */
public class LanguageProcessor extends GsonProcessor<Language>
{
	@Nullable
	@Override public Language deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException
	{
		Language language = new Language();

		if (jsonElement != null && jsonElement.isJsonObject())
		{
			Map<String, String> decoded = new Gson().fromJson(jsonElement, new TypeToken<Map<String, String>>(){}.getType());
			language.setValues(decoded);

			return language;
		}

		return null;
	}
}

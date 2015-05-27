package com.cube.storm.language.lib.parser;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cube.storm.LanguageSettings;
import com.cube.storm.language.data.Language;
import com.cube.storm.language.lib.processor.LanguageProcessor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

/**
 * Language parser used to process the json files into models
 *
 * @author Callum Taylor
 * @project LightningLanguage
 */
public abstract class LanguageBuilder
{
	private static Gson languageBuilder;

	/**
	 * Required to include view overrides
	 */
	public void rebuild()
	{
		languageBuilder = null;
		getGson();
	}

	/**
	 * Gets the gson object with the registered storm view type adapters. Use {@link #build(JsonElement, Class)} or
	 * {@link #build(String, Class)} to build your page/view objects.
	 *
	 * @return The gson object
	 */
	private Gson getGson()
	{
		if (languageBuilder == null)
		{
			GsonBuilder builder = new GsonBuilder();
			builder.registerTypeAdapter(Language.class, new LanguageProcessor());

			languageBuilder = builder.create();
		}

		return languageBuilder;
	}

	/**
	 * Builds a Page object from a file Uri
	 *
	 * @param fileUri The file Uri to load from
	 *
	 * @return The page data or null
	 */
	@Nullable
	public Language buildLanguage(@NonNull Uri fileUri)
	{
		try
		{
			InputStream stream = LanguageSettings.getInstance().getFileFactory().loadFromUri(fileUri);

			if (stream != null)
			{
				Language language = getGson().fromJson(new InputStreamReader(new BufferedInputStream(stream, 8192)), Language.class);
				language.setSourceUri(fileUri.toString());

				return language;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Builds a class from a json string input
	 *
	 * @param input The json string input to build from
	 * @param outClass The out class type
	 * @param <T> The type of class returned
	 *
	 * @return The built object, or null
	 */
	@Nullable
	public <T> T build(String input, Class<T> outClass)
	{
		return outClass.cast(getGson().fromJson(input, outClass));
	}

	/**
	 * Builds a class from a json element input
	 *
	 * @param input The json element input to build from
	 * @param outClass The out class type
	 * @param <T> The type of class returned
	 *
	 * @return The built object, or null
	 */
	@Nullable
	public <T> T build(JsonElement input, Class<T> outClass)
	{
		return outClass.cast(getGson().fromJson(input, outClass));
	}

	/**
	 * Builds a class from a json string input
	 *
	 * @param input The json string input to build from
	 * @param outClass The out class type
	 * @param <T> The type of class returned
	 *
	 * @return The built object, or null
	 */
	@Nullable
	public <T> T build(String input, Type outClass)
	{
		return getGson().fromJson(input, outClass);
	}

	/**
	 * Builds a class from a json element input
	 *
	 * @param input The json element input to build from
	 * @param outClass The out class type
	 * @param <T> The type of class returned
	 *
	 * @return The built object, or null
	 */
	@Nullable
	public <T> T build(JsonElement input, Type outClass)
	{
		return getGson().fromJson(input, outClass);
	}
}

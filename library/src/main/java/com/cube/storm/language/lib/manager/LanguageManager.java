package com.cube.storm.language.lib.manager;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.cube.storm.LanguageSettings;
import com.cube.storm.language.data.Language;
import com.cube.storm.language.lib.processor.LanguageProcessor;
import com.cube.storm.util.lib.resolver.Resolver;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

/**
 * Manager which controls the lookup and parsing of language files.
 * <p/>
 * Access this class via {@link com.cube.storm.LanguageSettings#getLanguageManager()}. Do not instantiate this class directly
 *
 * @author Callum Taylor
 * @project LightningLanguage
 */
public abstract class LanguageManager
{
	/**
	 * Gets a string value from the selected language or falls back to android String resources if not
	 * found using the same key
	 *
	 * @param context The context of the resources to fall back to
	 * @param key The key of the string to lookup
	 *
	 * @return The string, or an empty string
	 */
	@NonNull
	public String getValue(@NonNull Context context, @NonNull String key)
	{
		if (LanguageSettings.getInstance().getDefaultLanguage().hasValue(key)
		|| (LanguageSettings.getInstance().getFallbackLanguage() != null && LanguageSettings.getInstance().getFallbackLanguage().hasValue(key)))
		{
			return getValue(key);
		}
		else
		{
			int resource = context.getResources().getIdentifier(key.toLowerCase(Locale.ENGLISH), "string", context.getPackageName());

			if (resource > 0)
			{
				return context.getString(resource);
			}

			return "";
		}
	}

	/**
	 * Gets a string value from the selected language
	 *
	 * @param key The key of the string to lookup
	 *
	 * @return The string, or an empty string
	 */
	@NonNull
	public String getValue(@NonNull String key)
	{
		String value = LanguageSettings.getInstance().getDefaultLanguage().getValue(key);

		if (TextUtils.isEmpty(value) && LanguageSettings.getInstance().getFallbackLanguage() != null)
		{
			value = LanguageSettings.getInstance().getFallbackLanguage().getValue(key);
		}

		return value;
	}

	/**
	 * Gets the locale of the device. Note: this does not return deprecated language codes.
	 *
	 * The following codes are converted:
	 * <ul>
	 * <li>iw -&gt; he</li>
	 * <li>in -&gt; id</li>
	 * <li>ji -&gt; yi</li>
	 * </ul>
	 *
	 * @param context The context to use to find the locale.
	 *
	 * @return The locale in the format `xxx_xx` (3 letter CC, 2 letter language)
	 */
	@NonNull
	public String getLocale(@NonNull Context context)
	{
		String region = context.getResources().getConfiguration().locale.getISO3Country().toLowerCase();
		String locale = context.getResources().getConfiguration().locale.getLanguage();
		String languageSuffix = TextUtils.isEmpty(locale) ? "_en" : "_" + locale.toLowerCase();

		if (languageSuffix.toLowerCase().contains("iw"))
		{
			languageSuffix = languageSuffix.replace("iw", "he");
		}
		else if (languageSuffix.toLowerCase().contains("in"))
		{
			languageSuffix = languageSuffix.replace("in", "id");
		}
		else if (languageSuffix.toLowerCase().contains("ji"))
		{
			languageSuffix = languageSuffix.replace("ji", "yi");
		}

		return region + languageSuffix;
	}

	/**
	 * Loads a language from the given Uri
	 *
	 * @param context The context to use to load the language
	 * @param languageUri The uri of the language to load
	 */
	@NonNull
	public Language loadLanguage(@NonNull Context context, @NonNull Uri languageUri)
	{
		Gson gson = new GsonBuilder().registerTypeAdapter(Language.class, new LanguageProcessor()).create();

		for (String protocol : LanguageSettings.getInstance().getUriResolvers().keySet())
		{
			if (protocol.equalsIgnoreCase(languageUri.getScheme()))
			{
				try
				{
					Resolver resolver = LanguageSettings.getInstance().getUriResolvers().get(protocol);

					if (resolver != null)
					{
						byte[] languageData = resolver.resolveFile(languageUri);

						if (languageData != null)
						{
							Language language = gson.fromJson(new String(languageData, "UTF-8"), Language.class);
							language.setSourceUri(languageUri.toString());

							return language;
						}
					}
				}
				catch (UnsupportedEncodingException e)
				{
					e.printStackTrace();
				}

				break;
			}
		}

		return new Language();
	}
}

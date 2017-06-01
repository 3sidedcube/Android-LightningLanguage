package com.cube.storm.language.lib.manager;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.cube.storm.LanguageSettings;
import com.cube.storm.language.data.Language;
import com.cube.storm.language.lib.helper.LanguageHelper;

import java.util.Locale;

import static com.cube.storm.LanguageSettings.getInstance;

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
		if (getInstance().getDefaultLanguage().hasValue(key)
		|| (getInstance().getLocaleLanguage() != null && getInstance().getLocaleLanguage().hasValue(key)))
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
	 * Gets string from {@link LanguageSettings#getLocaleLanguage()} or falls back to {@link LanguageSettings#getDefaultLanguage()}.
	 *
	 * @param key The key of the string to lookup
	 *
	 * @return The string, or an empty string
	 */
	@NonNull
	public String getValue(@NonNull String key)
	{
		String value = null;

		if (getInstance().getLocaleLanguage() != null && getInstance().getLocaleLanguage().hasValue(key))
		{
			value = getInstance().getLocaleLanguage().getValue(key);
		}

		if (getInstance().getDefaultLanguage() != null && getInstance().getDefaultLanguage().hasValue(key))
		{
			value = getInstance().getDefaultLanguage().getValue(key);
		}

		return value == null ? "" : value;
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
	 * @return The locale in the format `xxx_xxx` (3 letter CC, 3 letter language)
	 */
	@NonNull
	public String getLocale(@NonNull Context context)
	{
		return LanguageHelper.getLocale(context);
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
	 * This method is deprecated, you should use {@link #getLocale(Context)} instead that returns a 3 letter language code
	 *
	 * @param context The context to use to find the locale.
	 *
	 * @return The locale in the format `xxx_xxx` (3 letter CC, 2 letter language)
	 */
	@NonNull @Deprecated
	public String getOldLocale(@NonNull Context context)
	{
		return LanguageHelper.getOldLocale(context);
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
		Language language = getInstance().getLanguageBuilder().buildLanguage(languageUri);

		if (language != null)
		{
			return language;
		}

		return new Language();
	}
}

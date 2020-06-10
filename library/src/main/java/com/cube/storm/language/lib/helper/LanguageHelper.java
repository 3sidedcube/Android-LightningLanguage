package com.cube.storm.language.lib.helper;

import android.content.Context;
import androidx.annotation.NonNull;
import android.text.TextUtils;

/**
 * Helper class for getting details around a user's device language/locale
 */
public class LanguageHelper
{
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
	public static String getLocale(@NonNull Context context)
	{
		String region = context.getResources().getConfiguration().locale.getISO3Country().toLowerCase();
		String locale = context.getResources().getConfiguration().locale.getISO3Language().toLowerCase();
		String languageSuffix = TextUtils.isEmpty(locale) ? "_eng" : "_" + locale.toLowerCase();

		return region + languageSuffix;
	}

	/**
	 * Gets the language of the device. Note: this does not return deprecated language codes.
	 *
	 * @param context The context to use to find the locale.
	 *
	 * @return The locale in the format `xxx` (3 letter language)
	 */
	@NonNull
	public static String getLanguage(@NonNull Context context)
	{
		String locale = context.getResources().getConfiguration().locale.getISO3Language().toLowerCase();
		String language = TextUtils.isEmpty(locale) ? "eng" : locale.toLowerCase();

		return language;
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
	public static String getOldLocale(@NonNull Context context)
	{
		String region = context.getResources().getConfiguration().locale.getISO3Country().toLowerCase();
		String locale = context.getResources().getConfiguration().locale.getLanguage().toLowerCase();
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
}

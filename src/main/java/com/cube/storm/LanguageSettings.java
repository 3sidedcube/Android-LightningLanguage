package com.cube.storm;

import android.content.Context;

/**
 * This is the entry point class of the library. To enable the use of the library, you must instantiate
 * a new {@link com.cube.storm.LanguageSettings.Builder} object in your {@link android.app.Application} singleton class.
 *
 * This class should not be directly instantiated.
 *
 * @author Callum Taylor
 * @project StormLanguage
 */
public class LanguageSettings
{
	/**
	 * The singleton instance of the settings
	 */
	private static LanguageSettings instance;

	/**
	 * Gets the instance of the {@link com.cube.storm.LanguageSettings} class
	 * Throws a {@link IllegalAccessError} if the singleton has not been instantiated properly
	 *
	 * @return The instance
	 */
	public static LanguageSettings getInstance()
	{
		if (instance == null)
		{
			throw new IllegalAccessError("You must build the language settings object first using LanguageSettings$Builder");
		}

		return instance;
	}

	/**
	 * Default private constructor
	 */
	private LanguageSettings(){}

	/**
	 * The builder class for {@link com.cube.storm.LanguageSettings}. Use this to create a new {@link com.cube.storm.LanguageSettings} instance
	 * with the customised properties specific for your project.
	 *
	 * Call {@link #build()} to build the settings object.
	 */
	public static class Builder
	{
		/**
		 * The temporary instance of the {@link com.cube.storm.LanguageSettings} object.
		 */
		private LanguageSettings construct;

		private Context context;

		/**
		 * Default constructor
		 */
		public Builder(Context context)
		{
			this.construct = new LanguageSettings();
			this.context = context.getApplicationContext();
		}

		/**
		 * Builds the final settings object and sets its instance. Use {@link #getInstance()} to retrieve the settings
		 * instance.
		 *
		 * @return The newly set {@link com.cube.storm.LanguageSettings} instance
		 */
		public LanguageSettings build()
		{
			return (LanguageSettings.instance = construct);
		}
	}
}

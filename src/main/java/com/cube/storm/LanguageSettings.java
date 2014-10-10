package com.cube.storm;

import android.content.Context;

import com.cube.storm.util.lib.resolver.AssetsResolver;
import com.cube.storm.util.lib.resolver.FileResolver;
import com.cube.storm.util.lib.resolver.Resolver;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Getter;

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
	 * Uri resolver used to load a file based on it's protocol.
	 */
	@Getter private Map<String, Resolver> uriResolvers = new LinkedHashMap<String, Resolver>(2);

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

			registerUriResolver("file", new FileResolver());
			registerUriResolver("assets", new AssetsResolver(context));
		}

		/**
		 * Registers a uri resolver to use
		 *
		 * @param protocol The string protocol to register
		 * @param resolver The resolver to use for the registered protocol
		 *
		 * @return The {@link com.cube.storm.LanguageSettings.Builder} instance for chaining
		 */
		public Builder registerUriResolver(String protocol, Resolver resolver)
		{
			construct.uriResolvers.put(protocol, resolver);
			return this;
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

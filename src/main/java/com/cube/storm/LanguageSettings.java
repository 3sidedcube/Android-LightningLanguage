package com.cube.storm;

import android.content.Context;
import android.net.Uri;

import com.cube.storm.language.data.Language;
import com.cube.storm.language.lib.manager.LanguageManager;
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
	 * Language manager used to resolve
	 */
	@Getter private LanguageManager languageManager;

	/**
	 * Uri resolver used to load a file based on it's protocol.
	 */
	@Getter private Map<String, Resolver> uriResolvers = new LinkedHashMap<String, Resolver>(2);

	/**
	 * Default loaded language. This will default to what ever the device's locale currently is
	 */
	@Getter private Language defaultLanguage;

	/**
	 * The fallback language to use if a key wasn't found in the default language, or if the default
	 * language was not loaded
	 */
	@Getter private Language fallbackLanguage;

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

		/**
		 * The context of the builder
		 */
		private Context context;

		/**
		 * Temporary language Uris. Gets loaded when {@link #build()} is called
		 */
		private Uri defaultLanguage, fallbackLanguage;

		/**
		 * Default constructor
		 */
		public Builder(Context context)
		{
			this.construct = new LanguageSettings();
			this.context = context.getApplicationContext();

			languageManager(LanguageManager.getInstance());

			registerUriResolver("file", new FileResolver());
			registerUriResolver("assets", new AssetsResolver(this.context));

			defaultLanguage(Uri.parse("assets://languages/" + LanguageManager.getInstance().getLocale(context) + ".json"));
		}

		/**
		 * Sets the default language manager
		 *
		 * @param manager The language manager
		 *
		 * @return The {@link com.cube.storm.LanguageSettings.Builder} instance for chaining
		 */
		public Builder languageManager(LanguageManager manager)
		{
			construct.languageManager = manager;
			return this;
		}

		/**
		 * Sets the default language uri to load
		 *
		 * @param languageUri The language Uri
		 *
		 * @return The {@link com.cube.storm.LanguageSettings.Builder} instance for chaining
		 */
		public Builder defaultLanguage(Uri languageUri)
		{
			this.defaultLanguage = languageUri;
			return this;
		}

		/**
		 * Sets the fallback language uri to load
		 *
		 * @param languageUri The language Uri
		 *
		 * @return The {@link com.cube.storm.LanguageSettings.Builder} instance for chaining
		 */
		public Builder fallbackLanguage(Uri languageUri)
		{
			this.fallbackLanguage = languageUri;
			return this;
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
		 * Registers a uri resolvers
		 *
		 * @param resolvers The map of resolvers to register
		 *
		 * @return The {@link com.cube.storm.LanguageSettings.Builder} instance for chaining
		 */
		public Builder registerUriResolver(Map<String, Resolver> resolvers)
		{
			construct.uriResolvers.putAll(resolvers);
			return this;
		}

		/**
		 * Builds the final settings object and sets its instance. Use {@link #getInstance()} to retrieve the settings
		 * instance.
		 *
		 * The languages set by {@link #defaultLanguage(android.net.Uri)} and {@link #fallbackLanguage(android.net.Uri)} are
		 * loaded at this point.
		 *
		 * @return The newly set {@link com.cube.storm.LanguageSettings} instance
		 */
		public LanguageSettings build()
		{
			LanguageSettings.instance = construct;
			construct.defaultLanguage = LanguageManager.getInstance().loadLanguage(context, defaultLanguage);

			if (construct.fallbackLanguage != null)
			{
				construct.fallbackLanguage = LanguageManager.getInstance().loadLanguage(context, fallbackLanguage);
			}

			return LanguageSettings.instance;
		}
	}
}

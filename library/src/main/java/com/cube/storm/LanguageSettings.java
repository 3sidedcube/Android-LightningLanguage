package com.cube.storm;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cube.storm.language.data.Language;
import com.cube.storm.language.lib.manager.LanguageManager;
import com.cube.storm.language.lib.processor.MethodProcessor;
import com.cube.storm.util.lib.resolver.AssetsResolver;
import com.cube.storm.util.lib.resolver.FileResolver;
import com.cube.storm.util.lib.resolver.Resolver;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * This is the entry point class of the library. To enable the use of the library, you must instantiate
 * a new {@link com.cube.storm.LanguageSettings.Builder} object in your {@link android.app.Application} singleton class.
 * <p/>
 * This class should not be directly instantiated.
 * <p/>
 * Example code.
 * <pre>
 languageSettings = new LanguageSettings.Builder(this)
	.registerUriResolver("cache", ContentSettings.getInstance().getUriResolvers().get("cache"))
	.defaultLanguage(Uri.parse("cache://languages/gbr_en.json"))
	.fallbackLanguage(Uri.parse("cache://languages/gbr_es.json"))
	.build();
 * </pre>
 * In order for the module to work correctly with {@code LightningContent} and standard Storm bundles, you must include
 * {@code registerUriResolver("cache", ContentSettings.getInstance().getUriResolvers().get("cache"))} as part of your
 * settings builder code.
 * <p/>
 * A {@link com.cube.storm.util.lib.processor.Processor} class is supplied for automatically converting Storm Text strings
 * into localised strings. By default, Storm bundles use a key-value lookup for all strings, to convert from the key to the
 * value, you must supply your {@code UiSettings} builder with {@link com.cube.storm.language.lib.processor.LanguageTextProcessor}.
 * <p/>
 * Example
 * <pre>
 uiSettings = new UiSettings.Builder(this)
	.registerUriResolver("cache", ContentSettings.getInstance().getUriResolvers().get("cache"))
	.textProcessor(new LanguageTextProcessor())
 	.build();
 * </pre>
 *
 * @author Callum Taylor
 * @project LightningLanguage
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
	 * Reloads the default and fallback languages if they have been previously set using the same Uri as defined in
	 * {@link com.cube.storm.LanguageSettings.Builder#defaultLanguage(android.net.Uri)} and {@link com.cube.storm.LanguageSettings.Builder#fallbackLanguage(android.net.Uri)}
	 *
	 * @param context The context to use to load the language
	 */
	public void reloadLanguage(@NonNull Context context)
	{
		if (getDefaultLanguage() != null && getDefaultLanguage().getSourceUri() != null)
		{
			this.defaultLanguage = getLanguageManager().loadLanguage(context, Uri.parse(getDefaultLanguage().getSourceUri()));
		}

		if (getFallbackLanguage() != null && getFallbackLanguage().getSourceUri() != null)
		{
			this.fallbackLanguage = getLanguageManager().loadLanguage(context, Uri.parse(getFallbackLanguage().getSourceUri()));
		}
	}

	/**
	 * Language manager used to resolve
	 */
	@Getter @Setter private LanguageManager languageManager;

	/**
	 * Uri resolver used to load a file based on it's protocol.
	 */
	@Getter @Setter private Map<String, Resolver> uriResolvers = new LinkedHashMap<String, Resolver>(2);

	/**
	 * Default loaded language. This will default to what ever the device's locale currently is
	 */
	@Getter @Setter private Language defaultLanguage;

	/**
	 * The fallback language to use if a key wasn't found in the default language, or if the default
	 * language was not loaded
	 */
	@Getter @Setter private Language fallbackLanguage;

	/**
	 * Method processor class used to process methods part of variable localisations
	 */
	@Getter @Setter private MethodProcessor methodProcessor;

	/**
	 * Loads a language from the uri to set for {@link #defaultLanguage}
	 *
	 * @param context The context to use to load the language
	 * @param languageUri The language Uri to load
	 */
	public void setDefaultLanguage(@NonNull Context context, @NonNull Uri languageUri)
	{
		defaultLanguage = getLanguageManager().loadLanguage(context, languageUri);
	}

	/**
	 * Loads a language from the uri to set for {@link #fallbackLanguage}
	 *
	 * @param context The context to use to load the language
	 * @param languageUri The language Uri to load
	 */
	public void setFallbackLanguage(@NonNull Context context, @Nullable Uri languageUri)
	{
		fallbackLanguage = getLanguageManager().loadLanguage(context, languageUri);
	}

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
		private Uri defaultLanguageUri, fallbackLanguageUri;

		/**
		 * Default constructor
		 */
		public Builder(@NonNull Context context)
		{
			this.construct = new LanguageSettings();
			this.context = context.getApplicationContext();

			languageManager(new LanguageManager(){});

			registerUriResolver("file", new FileResolver());
			registerUriResolver("assets", new AssetsResolver(this.context));

			defaultLanguage(Uri.parse("assets://languages/" + this.construct.getLanguageManager().getLocale(context) + ".json"));
			methodProcessor(new MethodProcessor());
		}

		/**
		 * Sets the default language manager
		 *
		 * @param manager The language manager
		 *
		 * @return The {@link com.cube.storm.LanguageSettings.Builder} instance for chaining
		 */
		public Builder languageManager(@NonNull LanguageManager manager)
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
		public Builder defaultLanguage(@NonNull Uri languageUri)
		{
			this.defaultLanguageUri = languageUri;
			return this;
		}

		/**
		 * Sets the fallback language uri to load
		 *
		 * @param languageUri The language Uri. Can be null to
		 *
		 * @return The {@link com.cube.storm.LanguageSettings.Builder} instance for chaining
		 */
		public Builder fallbackLanguage(@Nullable Uri languageUri)
		{
			this.fallbackLanguageUri = languageUri;
			return this;
		}

		/**
		 * Sets the method processor to use when dealing with variable localisations
		 *
		 * @param processor The method processor
		 *
		 * @return The {@link com.cube.storm.LanguageSettings.Builder} instance for chaining
		 */
		public Builder methodProcessor(@NonNull MethodProcessor processor)
		{
			construct.methodProcessor = processor;
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
			construct.defaultLanguage = construct.getLanguageManager().loadLanguage(context, defaultLanguageUri);

			if (fallbackLanguageUri != null)
			{
				construct.fallbackLanguage = construct.getLanguageManager().loadLanguage(context, fallbackLanguageUri);
			}

			return LanguageSettings.instance;
		}
	}
}

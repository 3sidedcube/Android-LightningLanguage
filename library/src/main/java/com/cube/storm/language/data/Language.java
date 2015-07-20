package com.cube.storm.language.data;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * Basic language model with a map of key/value pairs for the language.
 * <p/>
 * This model contains a map of all the key/value localsations stored in a localisation file.
 * The entire localisation language is loaded into one of these models. Be careful not to have a language
 * pack that is too large else you may experience memory problems.
 *
 * @author Callum Taylor
 * @project LightningLanguage
 */
public class Language implements Serializable
{
	/**
	 * Source Uri of the language object
	 */
	@Getter @Setter protected String sourceUri;

	/**
	 * Values of the language file
	 */
	@Getter @Setter protected Map<String, String> values = new HashMap<String, String>(0);

	/**
	 * Gets the language value from a String key
	 *
	 * @param id The ID of the string
	 *
	 * @return The language translation or an empty string
	 */
	@NonNull
	public String getValue(@NonNull String id)
	{
		return values.get(id) == null ? "" : values.get(id);
	}

	/**
	 * Checks for ID in the translation list
	 *
	 * @param id The ID of the string to check
	 *
	 * @return true if found, false if not
	 */
	public boolean hasValue(@NonNull String id)
	{
		return values.containsKey(id);
	}
}

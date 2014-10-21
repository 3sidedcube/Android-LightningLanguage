package com.cube.storm.language.data;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * Basic language model with a map of key/value pairs for the language.
 *
 * @author Callum Taylor
 * @project StormLanguage
 */
public class Language implements Serializable
{
	@Getter @Setter protected Map<String, String> values = new HashMap<String, String>(0);

	/**
	 * Gets the language value from a String key
	 *
	 * @param id The ID of the string
	 *
	 * @return The language translation
	 */
	@NonNull
	public String getValue(@NonNull String id)
	{
		return String.valueOf(values.get(id));
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

package com.cube.storm.language.lib.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Localise annotation to use on variables when injecting localisations from variables.
 * <p/>
 * You do not need to include the braces for the value in the localise annotation, only the key of the localisation
 *
 * @author Callum Taylor
 * @project LightningLanguage
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Localise
{
	String value();
}

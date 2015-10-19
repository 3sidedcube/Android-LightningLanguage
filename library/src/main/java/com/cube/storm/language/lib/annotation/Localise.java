package com.cube.storm.language.lib.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Localise annotation to use on variables when injecting localisations from variables
 *
 * @author Callum Taylor
 * @project LightningLanguage
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Localise
{
	String value();
}

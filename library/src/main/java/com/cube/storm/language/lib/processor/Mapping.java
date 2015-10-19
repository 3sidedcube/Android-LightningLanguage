package com.cube.storm.language.lib.processor;

import lombok.Getter;

/**
 * Class for mapping key and value for localisations
 *
 * @author Callum Taylor
 * @project LightningLanguage
 */
public class Mapping
{
	@Getter private String key;
	@Getter private String value;

	public Mapping(String k, Object v)
	{
		this.key = k;
		this.value = String.valueOf(v);
	}
}

package com.cube.storm.language.lib.processor;

import android.support.annotation.NonNull;

import com.cube.storm.language.lib.annotation.Localise;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

/**
 * Class for mapping key and value for localisations. Keys do not require the inclusion of `{}`
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

	/**
	 * Gets a list of {@link Localise} tagged variables to use as replacements for variable localisations
	 *
	 * @param cls The class to scan
	 *
	 * @return The list of {@link Mapping}. Can be empty.
	 */
	@NonNull
	public static List<Mapping> getTaggedLocalisations(@NonNull Object cls)
	{
		List<Mapping> mappings = new ArrayList<>();

		if (cls.getClass().getDeclaredFields() != null)
		{
			ArrayList<Method> methods = new ArrayList<Method>();
			ArrayList<Field> fields = new ArrayList<Field>();
			Class objOrSuper = cls.getClass();

			while (objOrSuper != null)
			{
				for (Field field : objOrSuper.getDeclaredFields())
				{
					if (field.isAnnotationPresent(Localise.class))
					{
						fields.add(field);
					}
				}

				objOrSuper = objOrSuper.getSuperclass();
			}

			for (Field field : fields)
			{
				if (field.isAnnotationPresent(Localise.class))
				{
					Localise variable = (Localise)field.getAnnotation(Localise.class);

					try
					{
						field.setAccessible(true);

						String key = ((Localise)variable).value();
						Object value = field.get(cls);

						mappings.add(new Mapping(key, value));
					}
					catch (IllegalAccessException e)
					{
						e.printStackTrace();
					}
				}
			}
		}

		return mappings;
	}
}

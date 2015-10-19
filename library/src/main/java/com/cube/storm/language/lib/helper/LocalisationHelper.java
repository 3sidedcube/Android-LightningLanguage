package com.cube.storm.language.lib.helper;

import android.app.Activity;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.cube.storm.LanguageSettings;
import com.cube.storm.language.lib.annotation.Localise;
import com.cube.storm.language.lib.processor.Mapping;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Helper class for localising views in an activity/fragment/view group
 * <p/>
 * This is a convenience class for quick localisations of strings. You can use this class, or {@link com.cube.storm.LanguageSettings#getLanguageManager()} to localise
 * your strings.
 * <p/>
 * You can localise on a string-by-string basis, or you can include the localisation keys directly in your XML in either a {@link android.widget.TextView}'s {@link android.widget.TextView#setText} method
 * or a {@link android.widget.EditText#setHint} method. All subclasses of {@link android.widget.TextView} will be included, such as {@link android.widget.Button}
 *
 * @author Callum Taylor
 * @project LightningLanguage
 */
public class LocalisationHelper
{
	/**
	 * Localises a string from the key.
	 *
	 * @param key The key to look up
	 *
	 * @return The mapped value, or the key if the value was empty
	 */
	@NonNull
	public static String localise(@NonNull String key, Mapping... mappings)
	{
		String value = LanguageSettings.getInstance().getLanguageManager().getValue(key);

		if (!TextUtils.isEmpty(value))
		{
			if (mappings != null)
			{
				for (Mapping mapping : mappings)
				{
					value = value.replaceAll("(\\{" + mapping.getKey() + "\\})", String.valueOf(mapping.getValue()));
				}
			}

			return value;
		}

		return key;
	}

	/**
	 * Loops through an {@link android.app.Activity}'s content view and localises the {@link android.widget.TextView} and subclasses of {@link android.widget.TextView}
	 *
	 * @param activity The {@link android.app.Activity} to localise
	 */
	public static void localise(@NonNull Activity activity, Mapping... mappings)
	{
		List<Mapping> mappingsList = getTaggedLocalisations(activity);
		mappingsList.addAll(new ArrayList<>(Arrays.asList(mappings)));

		localise((ViewGroup)activity.findViewById(android.R.id.content), mappingsList.toArray(new Mapping[mappingsList.size()]));
	}

	/**
	 * Loops through a fragment's content view and localises the {@link android.widget.TextView}
	 *
	 * @param fragment The fragment to localise
	 */
	public static void localise(@NonNull Fragment fragment, Mapping... mappings)
	{
		List<Mapping> mappingsList = getTaggedLocalisations(fragment);
		mappingsList.addAll(new ArrayList<>(Arrays.asList(mappings)));

		localise((ViewGroup)fragment.getView(), mappings);
	}

	/**
	 * Localises a {@link android.view.View}, or loops through if the {@link android.view.View} is a {@link android.view.ViewGroup} and localises any {@link android.widget.TextView} or {@link android.widget.EditText}
	 *
	 * @param view The view to localise
	 */
	public static void localise(@NonNull View view, Mapping... mappings)
	{
		if (view instanceof ViewGroup)
		{
			localise((ViewGroup)view, mappings);
		}
		else if (view instanceof TextView)
		{
			TextView textView = (TextView)view;
			String key = textView.getText().toString();
			String value = localise(key, mappings);

			textView.setText(value);

			if (EditText.class.isAssignableFrom(textView.getClass()) && !TextUtils.isEmpty(textView.getHint()))
			{
				String hintKey = textView.getHint().toString();
				String hintValue = localise(hintKey, mappings);

				textView.setHint(hintValue);
			}
		}
	}

	/**
	 * Loops through a {@link android.view.ViewGroup}'s children and localises the {@link android.widget.TextView}
	 *
	 * @param rootView The root view to start looping through
	 */
	public static void localise(@NonNull ViewGroup rootView, Mapping... mappings)
	{
		ArrayList<? extends TextView> textViews = (ArrayList<? extends TextView>)findAllChildrenByInstance(rootView, TextView.class);

		for (TextView textView : textViews)
		{
			localise(textView, mappings);
		}
	}

	/**
	 * Gets all views of a parent that match an class (recursive)
	 *
	 * @param parent The parent view
	 * @param instance The class to check by instance
	 *
	 * @return An array of views
	 */
	@NonNull
	private static <T extends View> List<T> findAllChildrenByInstance(@NonNull ViewGroup parent, @NonNull Class<T> instance)
	{
		List<View> views = new ArrayList<View>();
		int childCount = parent.getChildCount();

		for (int childIndex = 0; childIndex < childCount; childIndex++)
		{
			View child = parent.getChildAt(childIndex);

			if (child != null && instance.isAssignableFrom(child.getClass()))
			{
				views.add(child);
			}

			if (child instanceof ViewGroup)
			{
				views.addAll(findAllChildrenByInstance((ViewGroup)child, instance));
			}
		}

		return (List<T>)views;
	}

	/**
	 * Gets a list of @Localise tagged variables to use as replacements for variable localisations
	 *
	 * @param cls The class to scan
	 *
	 * @return The list of {@link Mapping}. Can be empty.
	 */
	@NonNull
	private static List<Mapping> getTaggedLocalisations(@NonNull Object cls)
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

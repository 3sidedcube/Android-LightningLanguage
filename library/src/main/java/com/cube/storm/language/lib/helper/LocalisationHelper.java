package com.cube.storm.language.lib.helper;

import android.app.Activity;
import android.app.Fragment;
import android.preference.Preference;
import android.preference.PreferenceGroup;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.cube.storm.LanguageSettings;
import com.cube.storm.language.lib.annotation.Localise;
import com.cube.storm.language.lib.processor.Mapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helper class for localising views in an activity/fragment/view group
 * <p/>
 * This is a convenience class for quick localisations of strings. You can use this class, or {@link com.cube.storm.LanguageSettings#getLanguageManager()} to localise
 * your strings.
 * <p/>
 * You can localise on a string-by-string basis, or you can include the localisation keys directly in your XML in either a {@link android.widget.TextView}'s {@link android.widget.TextView#setText} method
 * or a {@link android.widget.EditText#setHint} method. All subclasses of {@link android.widget.TextView} will be included, such as {@link android.widget.Button}
 * <p/>
 * Localisation variables are supported with the format of {@code {KEY}} in localisations. You can use the {@link Mapping} class as a KV param for the localise
 * methods, or you can use the {@link Localise} annotation to automatically use when populating localisations via {@link LocalisationHelper#localise(Activity, Mapping...)},
 * {@link LocalisationHelper#localise(Fragment, Mapping...)}, or {@link LocalisationHelper#localise(View, Mapping...)}
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
	 * @param mappings Collection of mappings for variables
	 *
	 * @return The mapped value, or the key if the value was empty
	 */
	@NonNull
	public static String localise(@NonNull String key, @NonNull Collection<Mapping> mappings)
	{
		return localise(key, mappings.toArray(new Mapping[mappings.size()]));
	}

	/**
	 * Localises a string from the key.
	 *
	 * @param key The key to look up
	 * @param mappings Optional array of mappings for variables
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
					Pattern compile = Pattern.compile("(\\{" + mapping.getKey() + "(\\}?))");
					Matcher matcher = compile.matcher(value);

					while (matcher.find())
					{
						String found = matcher.group();
						String replacement = "{" + mapping.getValue();

						if (found.endsWith("}"))
						{
							replacement += "}";
						}

						value = value.replace(matcher.group(), replacement);
					}

					value = LanguageSettings.getInstance().getMethodProcessor().process(mapping.getValue(), value);
				}
			}

			return value;
		}

		return key;
	}

	/**
	 * Loops through a menu and attempts to localise the titles of each item
	 *
	 * @param menu The menu to localise
	 * @param mappings Collection of mappings for variables
	 */
	public static void localise(@NonNull Menu menu, @NonNull Collection<Mapping> mappings)
	{
		localise(menu, mappings.toArray(new Mapping[mappings.size()]));
	}

	/**
	 * Loops through a menu and attempts to localise the titles of each item
	 *
	 * @param menu The menu to localise
	 * @param mappings Optional array of mappings for variables
	 */
	public static void localise(@NonNull Menu menu, Mapping... mappings)
	{
		for (int index = 0; index < menu.size(); index++)
		{
			if (menu.getItem(index).hasSubMenu())
			{
				localise((Menu)menu.getItem(index).getSubMenu(), mappings);
			}

			if (!TextUtils.isEmpty(menu.getItem(index).getTitle()))
			{
				String localised = localise(menu.getItem(index).getTitle().toString(), mappings);
				menu.getItem(index).setTitle(localised);
			}
		}
	}

	/**
	 * Loops through an {@link android.app.Activity}'s content view and localises the {@link android.widget.TextView} and subclasses of {@link android.widget.TextView}
	 *
	 * @param activity The {@link android.app.Activity} to localise
	 * @param mappings Collection of mappings for variables
	 */
	public static void localise(@NonNull Activity activity, @NonNull Collection<Mapping> mappings)
	{
		localise(activity, mappings.toArray(new Mapping[mappings.size()]));
	}

	/**
	 * Loops through an {@link android.app.Activity}'s content view and localises the {@link android.widget.TextView} and subclasses of {@link android.widget.TextView}
	 *
	 * @param activity The {@link android.app.Activity} to localise
	 * @param mappings Optional array of mappings for variables
	 */
	public static void localise(@NonNull Activity activity, Mapping... mappings)
	{
		List<Mapping> mappingsList = Mapping.getTaggedLocalisations(activity);
		mappingsList.addAll(new ArrayList<>(Arrays.asList(mappings)));

		localise((ViewGroup)activity.findViewById(android.R.id.content), mappingsList.toArray(new Mapping[mappingsList.size()]));
	}

	/**
	 * Loops through a fragment's content view and localises the {@link android.widget.TextView}
	 *
	 * @param fragment The fragment to localise
	 * @param mappings Collection of mappings for variables
	 */
	public static void localise(@NonNull Fragment fragment, @NonNull Collection<Mapping> mappings)
	{
		localise(fragment, mappings.toArray(new Mapping[mappings.size()]));
	}

	/**
	 * Loops through a fragment's content view and localises the {@link android.widget.TextView}
	 *
	 * @param fragment The fragment to localise
	 * @param mappings Optional array of mappings for variables
	 */
	public static void localise(@NonNull Fragment fragment, Mapping... mappings)
	{
		List<Mapping> mappingsList = Mapping.getTaggedLocalisations(fragment);
		mappingsList.addAll(new ArrayList<>(Arrays.asList(mappings)));

		localise((ViewGroup)fragment.getView(), mappings);
	}

	/**
	 * Localises a {@link android.preference.Preference}, or loops through a {@link android.preference.PreferenceGroup} and localises each child {@link android.preference.Preference} in the group.
	 *
	 * @param preference The {@link android.preference.Preference} or {@link android.preference.PreferenceGroup} that you want to localise
	 * @param mappings Collection of mappings for variables
	 */
	public static void localise(@NonNull Preference preference, @NonNull Collection<Mapping> mappings)
	{
		localise(preference, mappings.toArray(new Mapping[mappings.size()]));
	}

	/**
	 * Localises a {@link android.preference.Preference}, or loops through a {@link android.preference.PreferenceGroup} and localises each child {@link android.preference.Preference} in the group.
	 *
	 * @param preference The {@link android.preference.Preference} or {@link android.preference.PreferenceGroup} that you want to localise
	 * @param mappings Optional array of mappings for variables
	 */
	public static void localise(@NonNull Preference preference, Mapping... mappings)
	{
		CharSequence title = preference.getTitle();
		if (!TextUtils.isEmpty(title))
		{
			preference.setTitle(LocalisationHelper.localise(title.toString(), mappings));
		}

		CharSequence summary = preference.getSummary();
		if (!TextUtils.isEmpty(summary))
		{
			preference.setSummary(LocalisationHelper.localise(summary.toString(), mappings));
		}

		if (preference instanceof PreferenceGroup)
		{
			PreferenceGroup preferenceGroup = (PreferenceGroup)preference;
			int preferenceCount = preferenceGroup.getPreferenceCount();

			for (int index = 0; index < preferenceCount; index++)
			{
				localise(preferenceGroup.getPreference(index), mappings);
			}
		}
	}

	/**
	 * Localises a {@link android.view.View}, or loops through if the {@link android.view.View} is a {@link android.view.ViewGroup} and localises any {@link android.widget.TextView} or {@link android.widget.EditText}
	 *
	 * @param view The view to localise
	 * @param mappings Collection of mappings for variables
	 */
	public static void localise(@NonNull View view, @NonNull Collection<Mapping> mappings)
	{
		localise(view, mappings.toArray(new Mapping[mappings.size()]));
	}

	/**
	 * Localises a {@link android.view.View}, or loops through if the {@link android.view.View} is a {@link android.view.ViewGroup} and localises any {@link android.widget.TextView} or {@link android.widget.EditText}
	 *
	 * @param view The view to localise
	 * @param mappings Optional array of mappings for variables
	 */
	public static void localise(@NonNull View view, Mapping... mappings)
	{
		if (view instanceof ViewGroup)
		{
			localise((ViewGroup)view, mappings);
		}
		else if (view instanceof TextView)
		{
			List<Mapping> mappingsList = Mapping.getTaggedLocalisations(view.getContext());
			mappingsList.addAll(new ArrayList<>(Arrays.asList(mappings)));
			mappings = mappingsList.toArray(new Mapping[mappingsList.size()]);

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
	 * @param mappings Collection of mappings for variables
	 */
	public static void localise(@NonNull ViewGroup rootView, @NonNull Collection<Mapping> mappings)
	{
		localise(rootView, mappings.toArray(new Mapping[mappings.size()]));
	}

	/**
	 * Loops through a {@link android.view.ViewGroup}'s children and localises the {@link android.widget.TextView}
	 *
	 * @param rootView The root view to start looping through
	 * @param mappings Optional array of mappings for variables
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
}

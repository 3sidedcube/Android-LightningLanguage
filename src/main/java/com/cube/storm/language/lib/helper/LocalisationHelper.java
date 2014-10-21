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
import com.cube.storm.language.lib.manager.LanguageManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for localising views in an activity/fragment/view group
 *
 * @author Callum Taylor
 * @project StormLanguage
 */
public class LocalisationHelper
{
	/**
	 * Localises a string from the key.
	 *
	 * @param key The key to look up
	 *
	 * @return The mapped value, or the key
	 */
	@NonNull
	public static String localise(@NonNull String key)
	{
		String value = LanguageSettings.getInstance().getLanguageManager().getValue(key);

		if (!TextUtils.isEmpty(value))
		{
			return value;
		}

		return key;
	}

	/**
	 * Loops through an activity's content view and localises the TextViews
	 *
	 * @param activity The activity to localise
	 */
	public static void localise(@NonNull Activity activity)
	{
		localise((ViewGroup)activity.findViewById(android.R.id.content));
	}

	/**
	 * Loops through a fragment's content view and localises the TextViews
	 *
	 * @param fragment The fragment to localise
	 */
	public static void localise(@NonNull Fragment fragment)
	{
		localise((ViewGroup)fragment.getView());
	}

	/**
	 * Localises a view, or loops through if the view is a ViewGroup
	 *
	 * @param view The view to localise
	 */
	public static void localise(@NonNull View view)
	{
		if (view instanceof ViewGroup)
		{
			localise((ViewGroup)view);
		}
		else if (view instanceof TextView)
		{
			TextView textView = (TextView)view;
			String key = textView.getText().toString();
			String value = LanguageManager.getInstance().getValue(textView.getContext(), key);

			textView.setText(value);

			if (EditText.class.isAssignableFrom(textView.getClass()) && !TextUtils.isEmpty(textView.getHint()))
			{
				String hintKey = textView.getHint().toString();
				String hintValue = LanguageManager.getInstance().getValue(textView.getContext(), hintKey);

				textView.setHint(hintValue);
			}
		}
	}

	/**
	 * Loops through a ViewGroup's children and localises the text views
	 *
	 * @param rootView The root view to start looping through
	 */
	public static void localise(@NonNull ViewGroup rootView)
	{
		ArrayList<? extends TextView> textViews = (ArrayList<? extends TextView>)findAllChildrenByInstance(rootView, TextView.class);

		for (TextView textView : textViews)
		{
			localise(textView);
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

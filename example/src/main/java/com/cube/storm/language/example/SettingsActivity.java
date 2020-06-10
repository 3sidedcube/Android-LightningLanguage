package com.cube.storm.language.example;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;
import com.cube.storm.language.lib.helper.LocalisationHelper;
import junit.framework.Assert;

/**
 * @author Jamie Cruwys
 * @project LightningUtil
 */
public class SettingsActivity extends AppCompatActivity
{
	@Override protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_settings);

		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.add(R.id.container, new SettingsActivity.SettingsFragment());
		transaction.commit();
	}

	public static class SettingsFragment extends PreferenceFragmentCompat
	{
		@Override
		public void onCreatePreferences(
			Bundle savedInstanceState,
			String rootKey
		)
		{
			addPreferencesFromResource(R.xml.preferences);

			PreferenceScreen preferenceScreen = getPreferenceScreen();
			LocalisationHelper.localise(preferenceScreen);

			Preference firstPreference = preferenceScreen.getPreference(0);
			Assert.assertTrue(firstPreference instanceof PreferenceGroup);

			PreferenceGroup preferenceGroup = (PreferenceGroup)firstPreference;
			Assert.assertEquals("Test Category", preferenceGroup.getTitle());

			Assert.assertTrue(preferenceGroup.getPreferenceCount() == 2);

			Preference firstSubPreference = preferenceGroup.getPreference(0);
			Assert.assertEquals("Set language", firstSubPreference.getTitle());
			Assert.assertEquals("Set the locale to use", firstSubPreference.getSummary());

			Preference secondSubPreference = preferenceGroup.getPreference(1);
			Assert.assertEquals("Enable Goat Mode", secondSubPreference.getTitle());
			Assert.assertEquals("Turns on Goat Mode for use with Goat Simulator", secondSubPreference.getSummary());
		}
	}
}

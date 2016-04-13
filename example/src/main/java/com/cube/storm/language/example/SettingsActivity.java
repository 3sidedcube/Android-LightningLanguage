package com.cube.storm.language.example;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.preference.PreferenceScreen;

import com.cube.storm.language.lib.helper.LocalisationHelper;

import junit.framework.Assert;

/**
 * @author Jamie Cruwys
 * @project LightningUtil
 */
public class SettingsActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example_layout);

		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.add(R.id.container, new SettingsActivity.SettingsFragment());
		transaction.commit();
    }

    public static class SettingsFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            PreferenceScreen preferenceScreen = getPreferenceScreen();
            LocalisationHelper.localise(preferenceScreen);

            Preference firstPreference = preferenceScreen.getPreference(0);
            Assert.assertTrue(firstPreference instanceof PreferenceGroup);

            PreferenceGroup preferenceGroup = (PreferenceGroup) firstPreference;
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

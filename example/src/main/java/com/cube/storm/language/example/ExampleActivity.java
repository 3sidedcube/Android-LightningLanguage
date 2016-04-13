package com.cube.storm.language.example;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.cube.storm.LanguageSettings;
import com.cube.storm.language.lib.annotation.Localise;
import com.cube.storm.language.lib.helper.LocalisationHelper;
import com.cube.storm.language.lib.processor.Mapping;

import junit.framework.Assert;

/**
 * @author Callum Taylor
 * @project LightningUtil
 */
public class ExampleActivity extends Activity
{
	@Localise("VARIABLE") private String count = "two";

	@Override protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		new LanguageSettings.Builder(this)
			.defaultLanguage(Uri.parse("assets://gbr_en.json"))
			.build();

		// Standard localisation
		String value1 = LocalisationHelper.localise("string1");
		Assert.assertEquals(value1, "String 1");

		// View localisation with variable
		TextView text = new TextView(this);
		text.setText("string2");
		setContentView(text);

		LocalisationHelper.localise(this);
		Assert.assertEquals(text.getText().toString(), "String two");

		// Manual variable localisation
		String value3 = LocalisationHelper.localise("string3", new Mapping("VARIABLE", "Localisation 3"));
		Assert.assertEquals(value3, "Localisation 3");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menu_settings) {
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
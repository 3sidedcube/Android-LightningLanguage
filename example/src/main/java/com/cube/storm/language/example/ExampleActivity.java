package com.cube.storm.language.example;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.cube.storm.LanguageSettings;
import com.cube.storm.language.lib.annotation.Localise;
import com.cube.storm.language.lib.helper.LocalisationHelper;

import junit.framework.Assert;

/**
 * @author Callum Taylor
 * @project LightningUtil
 */
public class ExampleActivity extends Activity
{
	@Localise("VARIABLE") private int count = 2;

	@Override protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		new LanguageSettings.Builder(this)
			.defaultLanguage(Uri.parse("assets://gbr_en.json"))
			.build();

		String value1 = LocalisationHelper.localise("string1");
		Assert.assertEquals(value1, "String 1");

		TextView text = new TextView(this);
		text.setText("string2");
		setContentView(text);

		LocalisationHelper.localise(this);
//		Assert.assertEquals(text.getText().toString(), "String 2");
	}
}

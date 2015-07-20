package com.cube.storm.ui.example;

import android.app.Activity;
import android.os.Bundle;

import com.cube.storm.util.lib.debug.Debug;
import com.cube.storm.util.lib.manager.FileManager;

import junit.framework.Assert;

/**
 * @author Callum Taylor
 * @project LightningUtil
 */
public class ExampleActivity extends Activity
{
	@Override protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		exampleDebug();
		exampleFilemanager();
	}

	/**
	 * This is a demo method using the {@link com.cube.storm.util.lib.debug.Debug} class
	 */
	private void exampleDebug()
	{
		// Dump an array
		Debug.out(new String[]
		{
			"First output",
			"Second output",
			"Third output"
		});
	}

	/**
	 * This is a demo method using the {@link com.cube.storm.util.lib.manager.FileManager} class
	 */
	private void exampleFilemanager()
	{
		String file = getFilesDir().getAbsolutePath() + "/tmp";

		if (FileManager.getInstance().fileExists(file))
		{
			String read = FileManager.getInstance().readFileAsString(file);

			Assert.assertNotNull(read);
		}
		else
		{
			FileManager.getInstance().writeFile(file, "Some string".getBytes());
		}
	}
}

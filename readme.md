#Storm Library - Module Language

Storm is a collection of libraries that helps make mobile and desktop applications easy to create using a high quality WYSIACATWYG editor.

This module's purpose is to help localise an app using key/value pairing. It works by detecting the language and locale of the device and loading an appropriate language, if one is available.

#Usage

##Gradle

Simply include the following for your gradle dependencies `com.3sidedcube.storm:language:0.1a`.

**Note** The versioning of the library will always be as follows:

`Major version.Minor version.Bug fix`

It is safe to use `+` in part of of the `Bug fix` version, but do not trust it 100%. Always use a *specific* version to prevent regression errors.

##Code

Include the following into your main application singleton

```java
LanguageSettings.Builder languageSettings = new LanguageSettings.Builder(this).build();
```

The default language that gets loaded by the module will be located in assets `assets://languages/xxx_xx.json` You will need to override this to provide your own default language path by using the `defaultLanguage()` method in `LanguageSettings$Builder`.

Example:

```java
languageSettings = new LanguageSettings.Builder(this)
	.registerUriResolver("cache", ContentSettings.getInstance().getDefaultResolver()) // You can include this line if you are depending on ContentSettings
	.defaultLanguage(Uri.parse("cache://languages/" + LanguageManager.getInstance().getLocale(this) + ".json"))
	.build();
```

#Documentation

See the [Javadoc](http://3sidedcube.github.io/Android-LightningLanguage/) for full in-depth code-level documentation

#Contributors

[Callum Taylor (9A8BAD)](http://keybase.io/scruffyfox), [Tim Mathews (5C4869)](https://keybase.io/timxyz), [Matt Allen (DB74F5)](https://keybase.io/mallen), [Alan Le Fournis (067EA0)](https://keybase.io/alan3sc)

#License

See LICENSE.md

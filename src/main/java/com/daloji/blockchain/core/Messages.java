package com.daloji.blockchain.core;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
	/**
	 * the internal BUNDLE_NAME.
	 */
	private static final String BUNDLE_NAME = "com.daloji.blockchain.core.messages"; //$NON-NLS-1$

	/**
	 * the internal RESOURCE_BUNDLE.
	 */
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(Messages.BUNDLE_NAME);

	/**
	 * Get the string value.
	 *
	 * @param key
	 *          the key
	 * @return the value
	 */
	public static String getString(String key)
	{
		try
		{
			return Messages.RESOURCE_BUNDLE.getString(key);
		}
		catch (MissingResourceException e)
		{
			return '!' + key + '!';
		}
	}

	/**
	 * default constructor
	 */
	private Messages()
	{
		// Nothing to do
	}
}

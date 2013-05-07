package de.metux.nanoweb.service;

import java.util.Properties;

/**
 * Interface for a simple properties lookup
 * fetches a property list by name/key and retrieve as key:value list
 */
public interface IPropertiesLookup {
	/**
	 * look for a list given by its key
	 *
	 * if there's no list at all, return null. empty list is represented by null-sized array
	 *
	 * @param key	key/name of the property list to be retrieved
	 * @result	fetched propertylist, null if list not found
	 */
	public Properties lookup(String key)
	throws ServiceFailure;

	/**
	 * set a property
	 *
	 * @param key		key of the property list to be updated
	 * @param property	key of the property to be updated
	 * @param value		new value
	 */
	public boolean update(String key, String property, String value)
	throws ServiceFailure;
}

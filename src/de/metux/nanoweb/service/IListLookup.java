package de.metux.nanoweb.service;

/**
 * Interface for a simple list lookup
 * fetches a list by name/key and retrieve String[]
 */
public interface IListLookup {
	/**
	 * look for a list given by its key
	 *
	 * if there's no list at all, return null. empty list is represented by null-sized array
	 */
	public String[] lookup(String key)
	throws ServiceFailure;
}

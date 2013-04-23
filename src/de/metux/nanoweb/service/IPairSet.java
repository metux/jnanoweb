package de.metux.nanoweb.service;

/**
 * simple pair map, checks whether pair (A,B) is in the set
 *
 */
public interface IPairSet {
	/**
	 * check whether a pair of strings is in the set
	 *
	 * @param a	element A
	 * @param b	element B
	 * @result	true if (A,B) is in the set
	 */
	public boolean check(String a, String b)
	throws ServiceFailure;
}

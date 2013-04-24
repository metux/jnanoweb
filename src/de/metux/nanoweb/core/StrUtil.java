package de.metux.nanoweb.core;

/**
 * little collection of string operation helpers
 */
public class StrUtil {

	/**
	 * checks whether a string is empty (null or "")
	 *
	 * @param s	string to check
	 * @result	returns true if string is null or ""
	 */
	public static boolean isEmpty(String s) {
		return ((s == null) || (s.equals("")));
	}

	/**
	 * fixup a path string: if empty, returns "/", make sure leading "/" exists
	 *
	 * @param s	pathname to fixup
	 * @result	fixed pathname
	 */
	public static String fixup_path(String s) {
		if ((s==null) || (s.equals("")))
			return "/";
		if (!s.startsWith("/"))
			return "/"+s;
		return s;
	}

	/**
	 * add a new pathname element to existing pathname.
	 * oldpath is automatically fixed via fixup_path(), does not add trailing "/"
	 *
	 * @param oldpath	original pathname
	 * @param elem		new path name to be added
	 * @result		the new pathname with the given element added
	 */
	public static String add_path(String oldpath, String elem) {
		/* make sure oldpath is valid */
		oldpath = fixup_path(oldpath);

		/** check for empty elem */
		if ((elem == null) || (elem.equals("")) || (elem.equals("/")))
			return oldpath;

		return oldpath+(oldpath.endsWith("/") ? "" : "/")+elem;
	}

	/**
	 * split a pathname into its elements. empty elements (eg. "//") are skipped.
	 *
	 * @param path		pathname to be splitted
	 * @result		array of pathname element strings
	 */
	public static String[] split_path(String path) {
		if (path==null)
			return new String[0];

		String[] splitted = path.split("/");

		if (splitted.length == 0)
			return splitted;

		int sz = 0;
		for (int x=0; x<splitted.length; x++)
			if (!isEmpty(splitted[x]))
				sz++;

		String[] cleaned = new String[sz];
		sz = 0;
		for (int x=0; x<splitted.length; x++) {
			if (!isEmpty(splitted[x])) {
				cleaned[sz] = splitted[x];
				sz++;
			}
		}

		return cleaned;
	}

	/**
	 * safe pathname normalization, eg. to prevent breaking out of an
	 * web root via malicious URLs
	 *
	 * null/empty pathnames are translated to "/"
	 * elements "." are just dropped
	 * elements ".." cause previous element to be dropped
	 * resulting pathname will be absolute, no leading "../" are left
	 *
	 * @param path	pathname to be normalized
	 * @result	normalized pathname
	 */
	public static String safe_normalize_path(String path) {
		if (isEmpty(path))
			return "/";

		String[] splitted = path.split("/");

		if (splitted.length == 0)
			return "/";

		if (splitted.length == 1) {
			if ((splitted[0].equals("..")) || (splitted[0].equals(".")))
				return "/";
			else
				return "/"+splitted[0];
		}

		int count = 0; /* target counter */
		for (int x=1; x<splitted.length; x++) {
			/** go one element upwards **/
			if (splitted[x].equals("..")) {
				count = ((count == 0) ? 0 : (count-1));
			}
			/** just ignore "." **/
			else if (splitted[x].equals(".") || splitted[x].equals("")) {
			}
			/** accepted element, copy it **/
			else {
				splitted[count] = splitted[x];
				count++;
			}
		}

		if (count==0)
			return "/";

		String res = "";
		for (int x=0; x<count; x++)
			res += "/"+splitted[x];

		return res;
	}
}

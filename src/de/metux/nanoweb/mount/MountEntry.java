package de.metux.nanoweb.mount;

import de.metux.nanoweb.core.IHandler;
import de.metux.nanoweb.core.StrUtil;

/**
 * mount table entry
 */
public class MountEntry {
	public String mountpoint;
	public IHandler handler;
	public MountEntry entries[];

	/**
	 * retrieve a mount entry by name
	 *
	 * @param	name	mount entry name
	 * @result	retrieved mount enty (null if not found)
	 */
	public MountEntry getEntry(String name) {
		if (StrUtil.isEmpty(name))
			return null;

		if (entries == null)
			return null;

		for (int x=0; x<entries.length; x++) {
			if ((entries[x] != null) && (name.equals(entries[x].mountpoint)))
				return entries[x];
		}

		return null;
	}

	/**
	 * mount entry constructor
	 *
	 * @param m	mountpoint
	 * @param h	request handler
	 * @param e	array of sub-entries (may be null)
	 */
	public MountEntry(String m, IHandler h, MountEntry[] e) {
		mountpoint = m;
		handler = h;
		entries = e;
	}

	/**
	 * mount entry constructor (w/o sub entries)
	 *
	 * @param m	mountpoint
	 * @param h	request handler
	 */
	public MountEntry(String m, IHandler h) {
		mountpoint = m;
		handler = h;
		entries = null;
	}
}

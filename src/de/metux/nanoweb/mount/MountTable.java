package de.metux.nanoweb.mount;

import de.metux.nanoweb.core.IHandler;
import de.metux.nanoweb.core.StrUtil;

/**
 * mount table class. handles the actual mounting and pathname processing
 */
public class MountTable {

	/**
	 * return type for pathname mapping query
	 */
	public class Mapping {
		/** absolute mount root (mointpoint) **/
		public String root;
		/** pathname relative to root/mountpoint **/
		public String path;
		/** request handler responsible for the mapped location **/
		public IHandler handler;
	}

	public MountEntry root;

	/**
	 * constructor
	 *
	 * @param	rh	root handler (if no mounting happened)
	 * @param	e	list of root entries (may be null)
	 */
	public MountTable(IHandler rh, MountEntry[] e) {
		root = new MountEntry("/",rh,e);
	}

	/**
	 * find a proper mapping for given pathname
	 *
	 * lookup the right mount entry for the pathname, rewrite the
	 * location and return a Mapping structure
	 *
	 * @param	path	pathname to lookup
	 * @result	Mapping structure
	 */
	public Mapping find(String path) {
		if (path == null)
			return null;

		String pathelems[] = path.split("/");
		MountEntry walk = root;
		for (int x=0; x<pathelems.length; x++) {
			if (!StrUtil.isEmpty(pathelems[x])) {
				MountEntry e = walk.getEntry(pathelems[x]);
				if (e == null) {
					/** didnt find the entry with current name, so we're at the end. **/
					Mapping m = new Mapping();
					m.handler = walk.handler;

					for (int y=0; y<x; y++)
						m.root = StrUtil.add_path(m.root, pathelems[y]);

					for (int y=x; y<pathelems.length; y++)
						m.path = StrUtil.add_path(m.path, pathelems[y]);

					return m;
				} else {
					/** found the entry, so jump to next iteration **/
					walk = e;
				}
			}
		}

		/** full mapping: the complete path is mapped **/
		{
			Mapping m = new Mapping();

			m.handler = walk.handler;
			m.path = "/";
			m.root = "/";

			for (int y=0; y<pathelems.length; y++)
				m.root = StrUtil.add_path(m.root, pathelems[y]);

			return m;
		}
	}
}

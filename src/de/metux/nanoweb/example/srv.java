package de.metux.nanoweb.example;

import de.metux.nanoweb.daemon.MiniServer;
import de.metux.nanoweb.mount.MountEntry;
import de.metux.nanoweb.mount.MountingHandler;

public class srv {
	public static void main(String argv[]) {
		DummyHandler d = new DummyHandler();

		MiniServer srv = new MiniServer(
		    8088,
		    new MountingHandler(
		        d,
		new MountEntry[] {
			new MountEntry("foo", d),
			new MountEntry("bar", d,
			new MountEntry[]{
				new MountEntry("knollo", d)
			}
			              )
		}
		    )
		);

		srv.run();
	}
}

package biz.vnc.jilter;


public class JilterLog {
	public static boolean edebug_enabled = false;

	public static void debug(String str) {
		System.out.println(str);
	}

	public static void edebug(String str) {
		if (edebug_enabled)
			System.out.println(str);
	}

	public static void edebug(String str, Exception e) {
		if (edebug_enabled) {
			System.out.println(str);
			System.out.println(e);
			e.printStackTrace();
		}
	}

	public static void error(String str) {
		System.err.println(str);
	}

	public static void error(String str, Exception e) {
		System.err.println(str);
		System.err.println(e);
		e.printStackTrace();
	}
}

package EmailSystem;

import annotations.low;

public class Util {
	public static void prompt(@low String msg) {
		System.out.println(msg);
	}

	public static void prompt(@low int msg) {
		prompt(String.valueOf(msg));
	}
}

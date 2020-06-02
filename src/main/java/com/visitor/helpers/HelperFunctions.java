package com.visitor.helpers;

import java.util.function.Supplier;

public class HelperFunctions {

	public static void runIfNotNull (Object object, Runnable runnable, Runnable elseRun) {
		if (object != null) {
			runnable.run();
		} else {
			System.out.println("Trying to run following on null\n O: " + object.toString()
					+ "\n R: " + runnable.toString());
			elseRun.run();
		}
	}

	public static <T> T runIfNotNull (Object object, Supplier<T> supplier, Supplier<T> elseRun) {
		if (object != null) {
			return supplier.get();
		} else {
			System.out.println("Trying to run following on null\n O: " + object.toString()
					+ "\n R: " + supplier.toString());
			return elseRun.get();
		}
	}

	public static void runIfNotNull (Object object, Runnable runnable) {
		runIfNotNull(object, runnable, () -> {
		});
	}

	public static <T> T runIfNotNull (Object object, Supplier<T> supplier) {
		return runIfNotNull(object, supplier, () -> null);
	}
}

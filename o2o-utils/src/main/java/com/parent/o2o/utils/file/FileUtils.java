package com.parent.o2o.utils.file;

import java.io.File;

public class FileUtils {

	private FileUtils() {
	};
	
	public static boolean exists(String path) {
		File file = new File(path);
		if (file.exists()) {
			return true;
		}
		return false;
	}

	public static boolean existsAll(String... paths) {
		File file = null;
		for (String path : paths) {
			file = new File(path);
			if (!file.exists()) {
				return false;
			}
		}
		return true;
	}

	public static boolean notExistsAll(String... paths) {
		File file = null;
		for (String path : paths) {
			file = new File(path);
			if (!file.exists()) {
				return true;
			}
		}
		return false;
	}

}

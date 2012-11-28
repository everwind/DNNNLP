package utility;

import java.io.File;

public class FileUtil {

	public static void deleteDirectory(File path, boolean delPath) {
		if (path.exists()) {
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i], true);
				} else {
					files[i].delete();
				}
			}
		}
		
		if(delPath)
			path.delete();
	}
}

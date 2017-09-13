package com.android.library.Utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class Files {
	public static boolean deleteFile(String path) {
		File file = new File(path);
		return file.exists() && !file.isDirectory() && file.delete();
	}

	public static File getDirectory(Context context, String folder, boolean isInternal) {
		File dir;
		if(isInternal) {
			dir = context.getDir(folder, Context.MODE_PRIVATE);
		}
		else {
			dir = new File(Environment.getExternalStorageDirectory().toString() + "/Android/data/" + context.getPackageName() + "/files/" + folder);
		}
		if(!dir.exists() && dir.mkdirs()) {
			return dir;
		}
		return dir;
	}
}
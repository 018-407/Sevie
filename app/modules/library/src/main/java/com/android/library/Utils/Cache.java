package com.android.library.Utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;

import com.android.library.Sqlite.SQLiteAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.Hashtable;

public class Cache {
	private static final Hashtable<String, ImageLoader> IMAGE_LOADER = new Hashtable<>();
	private static final Hashtable<String, SQLiteAdapter> SQLITE_ADAPTER = new Hashtable<>();
	private static final Hashtable<String, Typeface> TYPEFACE = new Hashtable<>();

	public static ImageLoader getImageLoader(Context context) {
		String key = "IMAGE_LOADER";
		synchronized(IMAGE_LOADER) {
			if(!IMAGE_LOADER.containsKey(key)) {
				ImageLoader imageLoader = ImageLoader.getInstance();
				imageLoader.init(ImageLoaderConfiguration.createDefault(context));
				IMAGE_LOADER.put(key, imageLoader);
			}
			ImageLoader imageLoader = Cache.IMAGE_LOADER.get(key);
			if(!imageLoader.isInited()) {
				imageLoader.init(ImageLoaderConfiguration.createDefault(context));
				IMAGE_LOADER.put(key, imageLoader);
			}
		}
		return IMAGE_LOADER.get(key);
	}

	public static SQLiteAdapter getSQLiteAdapter(Context context, String database, int version) {
		synchronized(SQLITE_ADAPTER) {
			if(!SQLITE_ADAPTER.containsKey(database)) {
				SQLITE_ADAPTER.put(database, new SQLiteAdapter(context, database, version));
			}
		}
		return SQLITE_ADAPTER.get(database);
	}

	public static Typeface getTypeface(AssetManager manager, String typeface) {
		synchronized(TYPEFACE) {
			if(!TYPEFACE.containsKey(typeface)) {
				TYPEFACE.put(typeface, Typeface.createFromAsset(manager, typeface));
			}
		}
		return TYPEFACE.get(typeface);
	}
}
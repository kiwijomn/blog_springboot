package com.study.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;

public class MediaUtils {

	private static Map<String, MediaType> mediaMap;

	static {
		mediaMap = new HashMap<String, MediaType>();
		mediaMap.put("JPG", MediaType.IMAGE_JPEG);
		mediaMap.put("JPEG", MediaType.IMAGE_JPEG);
		mediaMap.put("GIF", MediaType.IMAGE_GIF);
		mediaMap.put("PNG", MediaType.IMAGE_PNG);
	}

	public static MediaType getMediaType(String type) {
		return mediaMap.get(type.toUpperCase());
	}

	public static boolean imageCheck(String ext) {
		String str = ext.toUpperCase();
		if (str.equals("JPG") || str.equals("JPEG") || str.equals("PNG") || str.equals("GIF")) {
			return true;
		}
		return false;
	}

}
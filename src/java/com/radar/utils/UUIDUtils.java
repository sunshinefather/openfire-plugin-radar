package com.radar.utils;

import java.util.UUID;
public class UUIDUtils {
	   public static synchronized String getUUID() {
		   return   String.valueOf(UUID.randomUUID()).replaceAll("-","");
		  }
}

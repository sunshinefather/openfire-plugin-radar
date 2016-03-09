package com.radar.cache;

import java.util.List;

public interface ICacheService {
	  public abstract void set(String paramString, Object paramObject);

	  public abstract void setMap(String paramString1, String paramString2, Object paramObject);

	  public abstract <T> T get(String paramString, Class<T> paramClass);

	  public abstract <T> T get(String paramString1, String paramString2, Class<T> paramClass);

	  public abstract <T> List<T> getMapValue(String paramString, Class<T> paramClass);

	  public abstract Long remove(String paramString);

	  public abstract Long removeMap(String paramString1, String paramString2);
}

package com.radar.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * 模拟redis
 * @ClassName:  TestCacheService   
 *    
 * @author: sunshine  
 * @date:   2015年1月29日 上午11:52:24
 */
public class TestCacheService implements ICacheService {
	private static final String PREFIX = "openfire_";
	private static final Map<String,Object> client= new Hashtable<String,Object>();
	
	@Override
	public <T> T get(String name, Class<T> cls) {
	    try
	    {
	      Object value = client.get(PREFIX + name);
	      return (T)value;
	    }
	    catch (Exception e) {
	    }
	    return null;
	}

	@Override
	public <T> T get(String name, String mapKey, Class<T> t) {
		Map<String,Object> map=null;
	    try
	    {
	      Object obj = client.get(PREFIX + name);
	      if(obj!=null ){
	    	  map=(Map)obj;
	      }
	    }
	    catch (Exception e)
	    {
	      return null;
	    }

		    if (map == null)
		    {
		      return null;
		    }
		    return (T)map.get(mapKey);
	}
	
	@Override
	public void setMap(String name, String mapKey, Object value) {
		try
	    {
		   Map<String, Object> map=get(name,Map.class);
		   if(map==null){
			   map=new HashMap<String, Object>();
		   }
		   map.put(mapKey, value);
	      client.put(PREFIX + name,map);
	    }
	    catch (Exception localException)
	    {
	    }
	}

	@Override
	public <T> List<T> getMapValue(String name, Class<T> t) {
		Map<String,Object> map=null;
	    List<T> rList = new ArrayList<T>();
	    try
	    {
	      Object obj = client.get(PREFIX + name);
	      if(obj!=null ){
	    	  map=(Map)obj;
	      }
	    }
	    catch (Exception e)
	    {
	      return null;
	    }

	    if (map != null)
	    {
	      for (Map.Entry<String, Object> entity: map.entrySet())
	      {
	        rList.add((T)entity.getValue());
	      }
	    }

	    return rList;
	}

	@Override
	public Long remove(String name) {
	    Long result = Long.valueOf(1L);
	    try
	    {
	      client.remove(PREFIX + name);
	    }
	    catch (Exception localException)
	    {
	    }
	    return result;
	}

	@Override
	public Long removeMap(String name, String valueKey) {
	    Long result = Long.valueOf(1L);
		Map<String,Object> map=null;
	    try
	    {
	      Object obj = client.get(PREFIX + name);
	      if(obj!=null )
	       {
	    	  map=(Map)obj;
	       }
	    }
	    catch (Exception e){}

	    if (map != null)
	    {
	    	map.remove(valueKey);
	    }

	    return result;
	}

	@Override
	public void set(String name, Object value) {
	    try
	    {
	      client.put(PREFIX + name,value);
	    }
	    catch (Exception localException)
	    {
	    }

	}
}
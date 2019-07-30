/**********************************************************      
  
 * 类名称：IRedisClient   
 * 类描述：   
 * 创建时间：2019年4月22日 上午10:14:15   
 * 修改备注：   
 *   
 **********************************************************/
package com.parent.o2o.redis;

public interface IRedisClient {
	
	public Long getInc(String key, Long step, long milliSeconds) throws RuntimeException;
	
	public Long getInc(String key, Long step) throws RuntimeException;
	
	public Long getInc(String key) throws RuntimeException;
	
	public long del(String key) throws RuntimeException;
	
	String getString(String key);
	
}

  
/**********************************************************      
  
 * 类名称：IUserService   
 * 类描述：   
 * 创建人：王璐瑶 
 * 创建时间：2018年11月11日 下午8:55:51   
 * 修改备注：   
 *   
 **********************************************************/
package com.parent.o2o.api.rpc;

import java.util.Map;

import javax.ws.rs.PathParam;

public interface IUserService {
	
	/**
	 * ********************************************************<br>      
	 * 方法说明：注册账号 <br>
	 * 参数： @param param
	 * 参数： @return
	 * 参数： @throws RuntimeException <br>   
	 * 返回值： Map<String,Object> <br>   
	 * 创建人：王璐瑶  <br>
	 * 创建时间：2018年11月16日 上午10:28:49 <br>   
	 * 修改备注： <br>   
	   
	 *********************************************************
	 */
	public Map<String, Object> saveUserInfo(Map<String, Object> param) throws RuntimeException;
	
	/**
	 * ********************************************************<br>      
	 * 方法说明：登录验证 <br>
	 * 参数： @param code
	 * 参数： @param pwd
	 * 参数： @return
	 * 参数： @throws RuntimeException <br>   
	 * 返回值： Map<String,Object> <br>   
	 * 创建人：王璐瑶  <br>
	 * 创建时间：2017年3月20日 下午3:20:49 <br>   
	 * 修改备注： <br>   
	   
	 *********************************************************
	 */
//	public Map<String, Object> login(@PathParam("code") String code, @PathParam("pwd") String pwd) throws RuntimeException;
}

  